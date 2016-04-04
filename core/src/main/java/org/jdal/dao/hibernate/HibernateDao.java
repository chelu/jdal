/*
 * Copyright 2009-2015 Jose Luis Martin.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jdal.dao.hibernate;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.impl.CriteriaImpl;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.type.Type;
import org.jdal.beans.PropertyUtils;
import org.jdal.dao.DaoSupport;
import org.jdal.dao.Filter;
import org.jdal.dao.Page;
import org.jdal.hibernate.HibernateUtils;
import org.springframework.beans.PropertyAccessor;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.util.ClassUtils;

/**
 * Hibernate generic DAO implementation. Support pagination of results and filters
 * using the {@link #getPage(Page)} method.
 *
 * @author Jose Luis Martin
 * @see org.jdal.dao.Dao
 * @since 1.0
 */
public class HibernateDao<T, PK extends Serializable> extends DaoSupport<T, PK>{

	private static final Log log = LogFactory.getLog(HibernateDao.class);
	
	private Class<T> entityClass;
	private boolean cachePageQueries = false;
	private HibernateTemplate hibernateTemplate;
	
	/** 
	 * Synchronized map with CriteriaBuilders by name
	 */
	private Map<String, CriteriaBuilder> criteriaBuilderMap = 
		Collections.synchronizedMap(new HashMap<String, CriteriaBuilder>());
	

	public HibernateDao() {
	}
	/**
	 * @param persistentClass
	 */
	public HibernateDao(Class<T> persistentClass) {
		this.entityClass = persistentClass;
	}

	/**
	 * Get Page, apply filter if any.
	 * If Filter is a entity model, use Example to create a criteria.
	 * else enable filter by name on session. 
	 * @param page with page definitions
	 * @return page of results
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <K> Page<K> getPage(Page<K> page) {
		
		List data = null;
		// try named query
		Query query = getQuery(page);
		if (query != null) {
			data = query.list();
		}
		else {
			// try filter, example and criteria builders
			Criteria criteria = getCriteria(page);
			ResultTransformer rt = ((CriteriaImpl) criteria).getResultTransformer(); 
			criteria.setProjection(Projections.rowCount());
			 page.setCount(((Long) criteria.uniqueResult()).intValue());
			// reset criteria
			criteria.setProjection(null);
			criteria.setResultTransformer(rt);
			// set start index and page size
			criteria.setFirstResult(page.getStartIndex())
				.setMaxResults(page.getPageSize());
			applyOrder(page, criteria);
			// run it
			criteria.setCacheable(cachePageQueries);
			data = criteria.list();
		}
		
		page.setData(data);
		
		return page;
	}

	/**
	 * Get Hibernate named Query and configure with filter from page. 
	 * Set the result count on page also. 
	 * @param page page 
	 * @return Hibernate named Query.
	 */
	private Query getQuery(Page<?> page) {
		Object filter = page.getFilter();
		
		try {
			if (filter instanceof Filter) {
				Filter f = (Filter) filter;
				Query query = getSession().getNamedQuery(f.getFilterName());
				Query countQuery = getSession().createQuery(query.getQueryString().replaceFirst("select", "count"));
				query.setProperties(f.getParameterMap());
				query.setMaxResults(page.getPageSize());
				query.setFirstResult(page.getStartIndex());
				page.setCount((Integer) countQuery.uniqueResult());

				return query;
			}
		}
		catch (HibernateException e) {}
		return null;
	}

	/**
	 * Create a Criteria from filter. If filter is a entity class instance, 
	 * return a Criteria with a Example Criterion applied.
	 * If not try four options in order:
	 * 
	 * 	1. if there are a filter with name filter.getName() enable it and return criteria
	 *  2. if there are a criteria builder with name filter.getName() use it to create Critera.
	 *  4. if there are a method named createCritera + filter.getName() invoke it to create Criteria
	 *  5. Return a Criteria for this entity class without Criterion applied.
	 * 
	 * @param filter the filter
	 * @return a new Criteria 
	 */
	protected Criteria getCriteria(Page<?> page) {
		Criteria executableCriteria = getSession().createCriteria(
				getEntityClass());
		Object filter = page.getFilter();
		// apply filter, if any
		if (filter != null) {
			if (ClassUtils.isAssignable(getEntityClass(), filter.getClass())) {
				// try a findByExample...
				executableCriteria.add(Example.create(getEntityClass()));
			} 
			else if (filter instanceof 	Filter) {
				Filter f = (Filter) filter;

				if (!enableFilter(f)) {
					if (log.isDebugEnabled()) 
						log.debug("No hibernate filter found with name: " + f.getFilterName() +
								", try criteria builder.");
					// if no filter, try criteria builder
					if (criteriaBuilderMap.containsKey(f.getFilterName())) {
						CriteriaBuilder cb =  criteriaBuilderMap.get(f.getFilterName());
						if(log.isDebugEnabled())
							log.debug("Found criteria builder with name: " + f.getFilterName() + 
								" - criteria builder class: " + cb.getClass().getSimpleName());
						executableCriteria = cb.build(executableCriteria, f);
					}
					// if no criteria builder try subclass method
					else if (ClassUtils.hasMethod(getClass(), "createCriteria" + f.getFilterName(), new Class[] {Criteria.class})) {
						Method method  = ClassUtils.getMethodIfAvailable(
								getClass(), "createCriteria" + f.getFilterName(), new Class[] {Criteria.class});
						if (method != null) {
							try {
								executableCriteria = (Criteria) method.invoke(this, executableCriteria);
							} catch (Exception e) { 
								log.error(e); 
							}
						}
					}
				}
			}
			else {
				log.warn("Cannot manage filter of type: " + filter.getClass());
			}
		}
		
		return executableCriteria;
	}

	/**
	 * Enable predefined filter in current session
	 * @param f Filter with filter name and parameters
	 * @return true if hibernate filter exists
	 */
	public boolean enableFilter(Filter f) {
		if (getSessionFactory().getDefinedFilterNames().contains(f.getFilterName())) {
			org.hibernate.Filter hf = getSession().enableFilter(f.getFilterName());
			Map<String, Object> parameterMap = f.getParameterMap();
			for (String key :parameterMap.keySet()) {
				hf.setParameter(key, parameterMap.get(key));
			}
			return true;
		}
		
		return false;
	}

	/**
	 * @return ClassMetadata from entityClass
	 */
	private ClassMetadata getClassMetadata() {
		return getClassMetadata(getEntityClass());
	}
	
	/**
	 * return ClassMetadata from Class
	 * @param clazz the class
	 * @return the ClassMetadata
	 */
	private ClassMetadata getClassMetadata(Class<?> clazz) {
		return getHibernateTemplate().getSessionFactory().
			getClassMetadata(clazz);
	}
	
	/**
	 * Apply Order to Criteria
	 * @param page the page
	 * @param criteria the criteria
	 */
	protected void applyOrder(Page<?> page, Criteria criteria) {
		Order order = createOrder(criteria, page.getSortName(), Page.Order.ASC.equals(page.getOrder()));
		if (order != null) 
			criteria.addOrder(order);
	}

	/** 
	 * Create Order from criteria and property path
	 * @param criteria the hibernate criteria to apply order on
	 * @param propertyPath the property path
	 * @return Order 
	 */
	protected Order createOrder(Criteria criteria, String propertyPath, boolean ascending) {
		Order order = null;
		
		if (propertyPath != null) {
			String sortProperty = PropertyUtils.getPropertyName(propertyPath);
			try {
				if (PropertyUtils.isNested(propertyPath)) {
					String alias = PropertyUtils.getPropertyName(PropertyUtils.getPath(propertyPath));
					// Need to create alias?
					// String alias = HibernateUtils.findAliasForPropertyPath(criteria, propertyPath);
					HibernateUtils.createAlias(criteria, PropertyUtils.getPath(propertyPath));
					sortProperty = alias + PropertyUtils.PROPERTY_SEPARATOR + sortProperty;
				}
				else { // test if property is an entity class
					Type sortType = getClassMetadata().getPropertyType(propertyPath);
					if (sortType.isEntityType()) { // is entity, look for 'name' property
						String[] propertyNames = getClassMetadata(sortType.getReturnedClass()).getPropertyNames();
						for (String name : propertyNames) {
							if ("name".equals(name)) {
								log.info("Found property name on persistent class: " + sortType.getName());
								String newPath = propertyPath + PropertyAccessor.NESTED_PROPERTY_SEPARATOR + "name";
								return createOrder(criteria, newPath, ascending);
							}
						}
					}
				}

				if (log.isDebugEnabled())
					log.debug("Setting order as: " + sortProperty);

				order = ascending ? Order.asc(sortProperty) : Order.desc(sortProperty);
			}
			catch(HibernateException he) {
				log.error("Cannot to create Order for property: " + sortProperty + " for " +
						getEntityClass().getSimpleName(), he);
			}
		}
		else {
			// add default order by id
			ClassMetadata metadata = getClassMetadata();
			if (metadata != null)
				order = Order.asc(metadata.getIdentifierPropertyName());
		}
	
		return order;
		
	}
	
	/**
	 * @return the entityClass
	 */
	public Class<T> getEntityClass() {
		return entityClass;
	}

	/**
	 * @param entityClass the entityClass to set
	 */
	public void setEntityClass(Class<T> entityClass) {
		this.entityClass = entityClass;
	}
	
	/** 
	 * Delete a entity from db
	 * @param entity
	 */
	public void delete(T entity) {
		getHibernateTemplate().delete(entity);
	}
	
	public T save(T entity) {
		getHibernateTemplate().saveOrUpdate(entity);
		return entity;
	}

	
	@SuppressWarnings("unchecked")
	public List<Serializable> getKeys(Page<T> page) {
		Criteria criteria = getCriteria(page);
		return criteria.setProjection(Projections.id()).list();
		
	}

	public void deleteById(PK id) {
		getSession().delete(get(id));
	}
	
	public  T initialize(T entity) {
		getSession().buildLockRequest(LockOptions.NONE).lock(entity);
		HibernateUtils.initialize(getSessionFactory(), entity);
		return entity;
	}
	
	public T initialize(T entity, int depth) {
		getSession().buildLockRequest(LockOptions.NONE).lock(entity);
		HibernateUtils.initialize(getSessionFactory(), entity, depth);

		return entity;
	}

	/**
	 * @return the criteriaBuilderMap
	 */
	public Map<String, CriteriaBuilder> getCriteriaBuilderMap() {
		return criteriaBuilderMap;
	}

	/**
	 * @param criteriaBuilderMap the criteriaBuilderMap to set
	 */
	public void setCriteriaBuilderMap(
			Map<String, CriteriaBuilder> criteriaBuilderMap) {
		this.criteriaBuilderMap.clear();
		this.criteriaBuilderMap.putAll(criteriaBuilderMap);
	}

	/**
     * {@inheritDoc}
     */
    public List<T> getAll() {
        return new ArrayList<T>(getHibernateTemplate().loadAll(this.entityClass));
    }
    
    public List<T> getAllDistinct() {
        Collection<T> result = new LinkedHashSet<T>(getAll());
        return new ArrayList<T>(result);
    }
    
    /** 
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<T> findByNamedQuery(
        String queryName, 
        Map<String, Object> queryParams) {
        String []params = new String[queryParams.size()];
        Object []values = new Object[queryParams.size()];
        int index = 0;
        Iterator<String> i = queryParams.keySet().iterator();
        while (i.hasNext()) {
            String key = i.next();
            params[index] = key;
            values[index++] = queryParams.get(key);
        }
        return (List<T>) getHibernateTemplate().findByNamedQueryAndNamedParam(
            queryName, 
            params, 
            values);
    }
    
    /**
     * {@inheritDoc}
     */
    public T get(PK id) {
        T entity = (T) getHibernateTemplate().get(this.entityClass, id);

        if (entity == null) {
            log.warn("'" + this.entityClass.getSimpleName() + "' object with id '" + id + "' not found...");
            throw new ObjectRetrievalFailureException(this.entityClass, id);
        }

        return entity;
    }

    /**
     * {@inheritDoc}
     */
    public boolean exists(PK id) {
      return getHibernateTemplate().get(this.entityClass, id) != null;
    }

    /**
     * Count rows
     * @return number of rows
     */
    public int count() {
		return ((Long) getSession().createCriteria(entityClass)
			.setProjection(Projections.rowCount())
			.uniqueResult()).intValue();
	}
    
	/**
	 * @return the cachePageQueries
	 */
	public boolean isCachePageQueries() {
		return cachePageQueries;
	}
	
	/**
	 * @param cachePageQueries the cachePageQueries to set
	 */
	public void setCachePageQueries(boolean cachePageQueries) {
		this.cachePageQueries = cachePageQueries;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public <E> E get(PK id, Class<E> clazz) {
		return (E) getSession().get(clazz, id);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public <E> List<E> getAll(Class<E> clazz) {
		return getSession().createCriteria(clazz).list();
	}
	
	
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}
	
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	public final void setSessionFactory(SessionFactory sessionFactory) {
		if (this.hibernateTemplate == null || sessionFactory != this.hibernateTemplate.getSessionFactory()) {
			this.hibernateTemplate = new HibernateTemplate(sessionFactory);
		}
	}
	
	/**
	 * Return the Hibernate SessionFactory
	 */
	public final SessionFactory getSessionFactory() {
		return (this.hibernateTemplate != null ? this.hibernateTemplate.getSessionFactory() : null);
	}
	
	/**
	 * Return current hibernate Session from Hibernate template
	 * @return curren hibernate Session
	 */
	public Session getSession() {
		return hibernateTemplate.getSessionFactory().getCurrentSession();
	}
}
