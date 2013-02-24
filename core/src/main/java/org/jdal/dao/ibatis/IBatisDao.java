/*
 * Copyright 2008-2011 the original author or authors.
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
package org.jdal.dao.ibatis;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ClassUtils;
import org.jdal.dao.Dao;
import org.jdal.dao.Page;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.ibatis.sqlmap.engine.impl.SqlMapClientImpl;
import com.ibatis.sqlmap.engine.mapping.result.ResultMap;
import com.ibatis.sqlmap.engine.mapping.result.ResultMapping;

/**
 * Dao implementation that use iBatis 2.x as ORM framework
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
@SuppressWarnings("unchecked")
public class IBatisDao<T, PK extends Serializable> extends SqlMapClientDaoSupport implements Dao<T, PK> {

	private static final String PRIMARY_KEY_PROPERTY_NAME = "id";
	private Class<T> entityClass;

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("rawtypes")
	public <K> Page<K> getPage(Page<K> page) {
		String queryName = getSelectQuery();
		Page<K> queryPage = page;
		
		// FIXME: clone the page here to map order columns from  property name to column name. 
		// review it. Maybe better build a map with page and filter
		if (page.getSortName() != null) {
			queryPage = (Page<K>) page.clone();
			queryPage.setSortName(getColumnName(page.getSortName()));
		}
		
		List result =  getSqlMapClientTemplate().queryForList(queryName, queryPage, 
				page.getStartIndex(), page.getPageSize());	
	
		String countQueryName = queryName +  "Count";
		int count = (Integer) getSqlMapClientTemplate().queryForObject(countQueryName, page);
		
		page.setData(result);
		page.setCount(count);
		
		return page;
	}

	
	// FIXME this is a hack, find another way to do it without breaking SqlMapClient interface
	private String getColumnName(String sortName) {
		
		SqlMapClientImpl impl = (SqlMapClientImpl) getSqlMapClient();
		ResultMap result = impl.delegate.getResultMap(getEntityName() + "." + getEntityName());
		
		for (ResultMapping mapping : result.getResultMappings()) {
			if (mapping.getPropertyName().equals(sortName)) {
				return mapping.getColumnName();
			}
		}
		
		return null;
	}


	public T save(T entity) {
		// FIXME: don't be optimistic
		int i = (Integer) getPrimaryKey(entity);
		if (i == 0) {
			insert(entity);
		}
		else {
			update(entity);
		}
		
		return entity;
	}
	
	public PK insert(T entity) {
		return (PK) getSqlMapClientTemplate().insert(getInsertQuery(), entity);
	}
	
	public void update (T entity) {
		getSqlMapClientTemplate().update(getUpdateQuery(), entity);
	}

	/**
	 * Get the PK from entity
	 * @param entity
	 * @return PK
	 */
	protected PK getPrimaryKey(T entity) {
		BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(entity);
		return (PK) bw.getPropertyValue(getPrimaryKeyPropertyName());
	}

	/**
	 * Gets the PK property name
	 * @return the property name of primary key
	 */
	protected String getPrimaryKeyPropertyName() {
		return PRIMARY_KEY_PROPERTY_NAME;
	}

	public void delete(T entity) {
		deleteById((PK) getPrimaryKey(entity));
		
	}

	public void deleteById(PK id) {
		// TODO Auto-generated method stub
		
	}

	public T get(PK id) throws DataAccessException {
	
		T  entity = (T) getSqlMapClientTemplate().queryForObject(getByIdQuery(), id);
		
		return entity;
	}

	public List<T> getAll() throws DataAccessException {
		return getSqlMapClientTemplate().queryForList(getSelectQuery());
	}
	
	// Lets Subclasses to overwrite named queries

	private String getSelectQuery() {
		return "select" + getEntityName();
	}

	/**
     * Gets name of select by id query in SqlMaps
     * @return
     */
	private String getByIdQuery() {
            return "get" + getEntityName();
    }
    
    /**
     * Gets name of insert query in SqlMaps
     * @return the insert query name in SqlMaps
     */
    protected String getInsertQuery() {
            return "insert" + getEntityName();
    }
    
	/**
     * Gets name of "validate duplicates" query in SqlMaps
     * @return the validate duplictes used in SqlMaps
     */
    protected String getValidateDuplicateQuery() {
            return "validateDuplicates" + getEntityName();
    }
    
    /**
     * Gets name of update query in SqlMaps
     * @return the update query used in SqlMaps
     */
    protected String getUpdateQuery() {
            return "update" + getEntityName();
    }
    
    /**
     * Gets name of delete query in SqlMaps
     * @return the delete query used in SqlMaps
     */
    protected String getDeleteQuery() {
            return "delete" + getEntityName();
    }
    
    /**
     * Gets name of new id query in SqlMaps
     * @return name of the newId query
     */
    protected String getNewIdQuery() {
            return "newId" + getEntityName();
    }

    /** 
     * Return de entity name for build query names
     * @return the entity name used in SqlMaps
     */
    protected String getEntityName() {
    	return ClassUtils.getShortClassName(entityClass);
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


	public boolean exists(PK id) {
		// TODO Auto-generated method stub
		return false;
	}


	public List<T> findByNamedQuery(String queryName,
			Map<String, Object> queryParams) {
		// TODO Auto-generated method stub
		return null;
	}


	public List<T> getAllDistinct() {
		// TODO Auto-generated method stub
		return null;
	}


	public void remove(PK id) {
		// TODO Auto-generated method stub
		
	}


	public List<Serializable> getKeys(Page<T> page) {
		// TODO Auto-generated method stub
		return null;
	}


	public T initialize(T entity) {
		return entity;
	}


	public T initialize(T entity, int depth) {
		return entity;
	}


	/**
	 * {@inheritDoc}
	 */
	public <E> E get(PK id, Class<E> clazz) {
		// TODO Auto-generated method stub
		return null;
	}


	/**
	 * {@inheritDoc}
	 */
	public <E> List<E> getAll(Class<E> clazz) {
		// TODO Auto-generated method stub
		return null;
	}

}
