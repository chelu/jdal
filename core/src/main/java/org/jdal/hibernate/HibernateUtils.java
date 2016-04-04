/*
 * Copyright 2008-2011 Jose Luis Martin Garcia
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
package org.jdal.hibernate;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.EntityMode;
import org.hibernate.Hibernate;
import org.hibernate.LockOptions;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.collection.AbstractPersistentCollection;
import org.hibernate.criterion.Example;
import org.hibernate.engine.PersistenceContext;
import org.hibernate.impl.CriteriaImpl;
import org.hibernate.impl.CriteriaImpl.Subcriteria;
import org.hibernate.impl.SessionImpl;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.persister.collection.CollectionPersister;
import org.jdal.beans.PropertyUtils;
import org.jdal.util.BeanUtils;
import org.springframework.util.StringUtils;

/**
 * Hibernate Utility library
 *
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
@SuppressWarnings("rawtypes")
public abstract class HibernateUtils {
	
	public static final int DEFAULT_DEPTH = 2;
	private static final Log log = LogFactory.getLog(HibernateUtils.class);
	private static final String EXISTS_QUERY = "SELECT 1 from %s x WHERE %s = ?";
		
	/** 
	 * Initialize a Object for use whith closed session. 
	 * Will recurse on properties at maximum of n depth.
	 * 
	 * @param sessionFactory the Hibernate SessionFactory to use
	 * @param obj Object to initialize
	 * @param depth max depth in recursion
	 */
	public static void initialize(SessionFactory sessionFactory, Object obj, 
			int depth) {
		initialize(sessionFactory, obj, new ArrayList<Object>(), depth);
	}
	
	
	/**
	 * Initialize a Object for use with closed sessions, 
	 * Use with care, will recurse on all properties.
	 * 
	 * @param sessionFactory the hibernate SessionFactory
	 * @param obj persistent object to initialize
	 */
	public static void initialize(SessionFactory sessionFactory, Object obj) {
		initialize(sessionFactory, obj, new ArrayList<Object>(), DEFAULT_DEPTH);
	}
	
	
	/** 
	 * Initialize Object for use with closed Session. 
	 * 
	 * @param sessionFactory max depth in recursion
	 * @param obj Object to initialize
	 * @param initializedObjects list with already initialized Objects
	 * @param depth max depth in recursion
	 */
	private static void initialize(SessionFactory sessionFactory, Object obj, 
			List<Object> initializedObjects, int depth) {
		
		// return on nulls, depth = 0 or already initialized objects
		if (obj == null || depth == 0) { 
			return; 
		}
		
		if (!Hibernate.isInitialized(obj)) {
			// if collection, initialize objects in collection too. Hibernate don't do it.
			if (obj instanceof Collection) {
				initializeCollection(sessionFactory, obj, initializedObjects,
						depth);
				return;
			}
			
			sessionFactory.getCurrentSession().buildLockRequest(LockOptions.NONE).lock(obj);
			Hibernate.initialize(obj);
		}
	
		// now we can call equals safely. If object are already initializated, return
		if (initializedObjects.contains(obj))
			return;
		
		initializedObjects.add(obj);
		
		// initialize all persistent associaciations.
		ClassMetadata classMetadata = getClassMetadata(sessionFactory, obj);
		
		if (classMetadata == null) {
			return; // Not persistent object
		}
		
		Object[] pvs = classMetadata.getPropertyValues(obj, EntityMode.POJO);
		
		for (Object pv : pvs) {
			initialize(sessionFactory, pv, initializedObjects, depth - 1);
		}
	}

	/**
	 * Initalize Collection and recurse on elements 
	 * 
	 * @param sessionFactory the hibernate SessionFactory
	 * @param obj the obj to initilize
	 * @param initializedObjects list with already initialized objects
	 * @param depth max depth in recursion
	 */
	private static void initializeCollection(SessionFactory sessionFactory,
			Object obj, List<Object> initializedObjects, int depth) {

		Collection<?> collection = (Collection<?>) obj;
		initializeCollection(collection, sessionFactory.getCurrentSession());
		
		// Initialize elements
		for (Object o : collection) {
			initialize(sessionFactory, o, initializedObjects, depth - 1);
		}
	}
	
	/**
	 * Initialize Collection (detached or not)
	 * @param collection collection to initialize
	 * @param session Session to use for initialization
	 */
	public static void initializeCollection(Collection collection, Session session) {
		if (collection instanceof AbstractPersistentCollection) {
			AbstractPersistentCollection ps = (AbstractPersistentCollection) collection;
			log.debug("Initalizing PersistentCollection of role: " + ps.getRole());	
			if (!ps.wasInitialized()) {
				SessionImpl source = (SessionImpl) session;
				PersistenceContext context = source.getPersistenceContext();
				CollectionPersister cp = source.getFactory().getCollectionPersister(ps.getRole());
				
				if (context.getCollectionEntry(ps) == null) {  // detached
					context.addUninitializedDetachedCollection(cp, ps);
				}
				
				ps.setCurrentSession(context.getSession());
				Hibernate.initialize(collection);
			}
		}
	}

	/**
	 * Get ClassMetadata for persistent object
	 *  
	 * @param sessionFactory the hibernate SessionFactory
	 * @param obj Object to initilize
	 * @return ClassMetadata the class metadata
	 */
	private static ClassMetadata getClassMetadata(
			SessionFactory sessionFactory, Object obj) {
		return sessionFactory.getClassMetadata(Hibernate.getClass(obj));	
	}

	/**
	 * Gets the identifier property name of persistent object
	 * 
	 * @param sessionFactory the hibernate SessionFactory
	 * @param obj the persistent object
	 * @return the identifier property name
	 */
	public static String getIdentifierPropertyName(
			SessionFactory sessionFactory, Object obj) {
		
		ClassMetadata cm = getClassMetadata(sessionFactory, obj);

		return cm == null ? null : cm.getIdentifierPropertyName();
		
	}
	
	/**
	 * Get all name attributes from a object that are of the given class 
	 * @param obj Object to get fields
	 * @param type type of the class to find.
	 * @return 	name attributes with the class type specified
	 */
	public static Set<String> getFieldNamesByType(Object obj, 
			Class <?> type) {
		Set<String> fieldNames = new HashSet<String> (0);
		Field [] fields = obj.getClass().getDeclaredFields();
		for (Field field : fields) {
			if (type.equals(field.getType())) {
				fieldNames.add(field.getName());
			}
		}
		return fieldNames;
	}
	
	/**
	 * Get a hibernate Example object that excludes zeroes values and excludes
	 * all boolean -primitive and wrapper class- attributes of a given object.  
	 * @param instance given object for a build a Example object.
	 * @return a hibernate Example object.
	 */
	public static Example excludeBooleanFields (Object instance) {
		Example result = Example.create(instance).excludeZeroes();
		Set<String> fieldNames = getFieldNamesByType(instance, Boolean.class);
		fieldNames.addAll(getFieldNamesByType(instance, Boolean.TYPE));
		for (String fieldName : fieldNames) {
			result.excludeProperty(fieldName);
		}
		return result;
	}
	
	/**
	 * Return a existing alias for propertyPath on Criteria or null if none
	 * @param criteria Hibernate Criteria
	 * @param propertyPath the property path
	 * @return alias or null if none
	 */
	public static String findAliasForPropertyPath(Criteria criteria, String propertyPath) {
		CriteriaImpl c = (CriteriaImpl) criteria;
		Iterator iter = c.iterateSubcriteria();
		while (iter.hasNext()) {
			Subcriteria subCriteria = (Subcriteria) iter.next();
			if (propertyPath.equals(subCriteria.getPath()));
				return subCriteria.getAlias();
		}
		// not found
		return null; 
	}
	
	/**
	 * Create an alias for a property path
	 * @return the alias
	 */
	public static String  createAlias(Criteria criteria, String propertyPath) {
		String[] paths = PropertyUtils.split(propertyPath);
		String alias = "";
		
		for (String name : paths) {
			alias += StringUtils.isEmpty(alias) ? name : "." + name;
			criteria.createAlias(alias, name);
		}
		
		
		return PropertyUtils.getPropertyName(alias);
	}
	
	/**
	 * Test if a entity already exists.
	 * @param entity entity to test
	 * @param session hibernate session
	 * @return true if exists, false otherwise
	 */
	public static boolean exists(Object entity, Session session) {
 		String propertyId = getIdentifierPropertyName(session.getSessionFactory(), entity);
		if (propertyId == null)
			return false;
		
		Object id = BeanUtils.getProperty(entity, propertyId);
		if (id == null)
			return false;
		
		return session.createQuery(String.format(EXISTS_QUERY, entity.getClass().getSimpleName(), propertyId))
			.setParameter(0, id)
			.list()
			.size() > 0;
	}
}
