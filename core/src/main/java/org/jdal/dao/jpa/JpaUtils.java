/*
 * Copyright 2009-2011 the original author or authors.
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
package org.jdal.dao.jpa;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PersistenceUnitUtil;
import javax.persistence.criteria.CompoundSelection;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.Attribute.PersistentAttributeType;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.EntityType;

import org.apache.commons.lang.StringUtils;
import org.jdal.beans.PropertyUtils;
import org.springframework.beans.PropertyAccessor;
import org.springframework.beans.PropertyAccessorFactory;


/**
 * Utility class for dealing with JPA API
 * 
 * @author Jose Luis Martin
 * @since 1.1
 */
public abstract class JpaUtils {
	
	private static String ALIAS_PATTERN_STRING = "(?<=from)\\s+(?:\\S+)\\s+(?:as\\s+)*(\\w*)";
	private static Pattern ALIAS_PATTERN = Pattern.compile(ALIAS_PATTERN_STRING, Pattern.CASE_INSENSITIVE);
	private static String FROM_PATTERN_STRING = "(from.*+)";
	private static Pattern FROM_PATTERN = Pattern.compile(FROM_PATTERN_STRING, Pattern.CASE_INSENSITIVE);
	private static volatile int aliasCount = 0;
	
	/**
	 * Result count from a CriteriaQuery
	 * @param em Entity Manager
	 * @param criteria Criteria Query to count results
	 * @return row count
	 */
	public static <T> Long count(EntityManager em, CriteriaQuery<T> criteria) {
	   
	    return em.createQuery(countCriteria(em, criteria)).getSingleResult();
	}
	
	/**
	 * Create a row count CriteriaQuery from a CriteriaQuery
	 * @param em entity manager
	 * @param criteria source criteria
	 * @return row count CriteriaQuery
	 */
	public static <T> CriteriaQuery<Long> countCriteria(EntityManager em, CriteriaQuery<T> criteria) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
		copyCriteriaNoSelection(criteria, countCriteria);
		
		Expression<Long> countExpression;
		
		if (criteria.isDistinct()) {
			countExpression = builder.countDistinct(findRoot(countCriteria, criteria.getResultType()));
		}
		else {	
			countExpression = builder.count(findRoot(countCriteria, criteria.getResultType()));
		}
		
		return countCriteria.select(countExpression);
	}
	
	/**
	 * Gets The result alias, if none set a default one and return it
	 * @param selection 
	 * @return root alias or generated one
	 */
	public static synchronized <T> String getOrCreateAlias(Selection<T> selection) {
		// reset alias count
		if (aliasCount > 1000)
			aliasCount = 0;
			
		String alias = selection.getAlias();
		if (alias == null) {
			alias = "JDAL_generatedAlias" + aliasCount++;
			selection.alias(alias);
		}
		return alias;
		
	}

	/**
	 * Find Root of result type
	 * @param query criteria query
	 * @return the root of result type or null if none
	 */
	public static  <T> Root<T> findRoot(CriteriaQuery<T> query) {
		return findRoot(query, query.getResultType());
	}
	
	/**
	 * Find the Root with type class on CriteriaQuery Root Set
	 * @param <T> root type
	 * @param query criteria query
	 * @param clazz root type
	 * @return Root<T> of null if none
	 */
	@SuppressWarnings("unchecked")
	public static  <T> Root<T> findRoot(CriteriaQuery<?> query, Class<T> clazz) {

		for (Root<?> r : query.getRoots()) {
			if (clazz.equals(r.getJavaType())) {
				return (Root<T>) r.as(clazz);
			}
		}
		return (Root<T>) query.getRoots().iterator().next();
	}
	
	/**
	 * Find Joined Root of type clazz
	 * @param <T>
	 * @param query the criteria query
	 * @param rootClass the root class
	 * @param joinClass the join class
	 * @return the Join
	 */
	@SuppressWarnings("unchecked")
	public static <T, K> Join<T,K> findJoinedType(CriteriaQuery<T> query, Class<T> rootClass, Class<K> joinClass) {
		Root<T> root = findRoot(query, rootClass);
		Join<T,K> join = null;
		for (Join<T,?> j : root.getJoins()) {
			if (j.getJoinType().equals(joinClass)) {
				join = (Join<T, K>) j;
			}
		}
		return join;
	}
	
	/**
	 * Gets a Path from Path using property path
	 * @param path the base path
	 * @param propertyPath property path String like "customer.order.price"
	 * @return a new Path for property
	 */
	@SuppressWarnings("unchecked")
	public static <T> Path<T> getPath(Path<?> path, String propertyPath) {
		if (StringUtils.isEmpty(propertyPath))
			return (Path<T>) path;
		
		String name = StringUtils.substringBefore(propertyPath, PropertyUtils.PROPERTY_SEPARATOR);
		Path<?> p = path.get(name); 
		
		return getPath(p, StringUtils.substringAfter(propertyPath, PropertyUtils.PROPERTY_SEPARATOR));
		
	}

	/**
	 * Create a count query string from a query string
	 * @param queryString string to parse
	 * @return the count query string
	 */
	public static String createCountQueryString(String queryString) {
		return queryString.replaceFirst("^.*(?i)from", "select count (*) from ");
	}

	/**
	 * Gets the alias of root entity of JQL query 
	 * @param queryString JQL query 
	 * @return alias of root entity.
	 */
	public static String getAlias(String queryString) {
		Matcher m = ALIAS_PATTERN.matcher(queryString);
		return m.find() ? m.group(1) : null;
	}
	
	/**
	 * Add order by clause to queryString
	 * @param queryString JPL Query String
	 * @param propertyPath Order properti
	 * @param asc true if ascending
	 * @return JQL Query String with Order clause appened.
	 */
	public static String addOrder(String queryString, String propertyPath, boolean asc ) {
		
		if (StringUtils.containsIgnoreCase(queryString, "order by")) {
			return queryString;
		}
		
		StringBuilder sb = new StringBuilder(queryString);
		sb.append(" ORDER BY ");
		sb.append(getAlias(queryString));
		sb.append(".");
		sb.append(propertyPath);
		sb.append(" ");
		sb.append(asc ? "ASC" : "DESC");
		
		return sb.toString();
	}

	/**
	 * Gets Query String for selecting primary keys
	 * @param queryString the original query
	 * @param name primary key name
	 * @return query string 
	 */
	public static String getKeyQuery(String queryString, String name) {
		Matcher m = FROM_PATTERN.matcher(queryString);
		if (m.find()) {
			StringBuilder sb = new StringBuilder("SELECT ");
			sb.append(getAlias(queryString));
			sb.append(".");
			sb.append(name);
			sb.append(" ");
			sb.append(m.group());
			return sb.toString();
		}
		
		return null;
	}
	
	/**
	 * Copy Criteria without Selection
	 * @param from source Criteria
	 * @param to destination Criteria
	 */
	public static void  copyCriteriaNoSelection(CriteriaQuery<?> from, CriteriaQuery<?> to) {

		 if (!isEclipseLink(from)) {
			 // Copy Roots
			 for (Root<?> root : from.getRoots()) {
				 Root<?> dest = to.from(root.getJavaType());
				 dest.alias(getOrCreateAlias(root));
				 copyJoins(root, dest);
			 }
		}
		
		to.groupBy(from.getGroupList());
		to.distinct(from.isDistinct());
		
		if (from.getGroupRestriction() != null)
			to.having(from.getGroupRestriction());
		
		to.where((Predicate) from.getRestriction());
		to.orderBy(from.getOrderList());
	}
	
	private static boolean isEclipseLink(CriteriaQuery<?> from) {
		return from.getClass().getName().contains("org.eclipse.persistence");
	}

	public static <T> void copyCriteria(CriteriaQuery<T> from, CriteriaQuery<T> to) {
		copyCriteriaNoSelection(from, to);
		to.select(from.getSelection());
	}
	
	/**
	 * Copy Joins
	 * @param from source Join
	 * @param to destination Join
	 */
	public static void copyJoins(From<?, ?> from, From<?, ?> to) {
		for (Join<?, ?> j : from.getJoins()) {
			Join<?, ?> toJoin = to.join(j.getAttribute().getName(), j.getJoinType());
			toJoin.alias(getOrCreateAlias(j));
		
			copyJoins(j, toJoin);
		}
		
		for (Fetch<?, ?> f : from.getFetches()) {
			Fetch<?, ?> toFetch = to.fetch(f.getAttribute().getName());
			copyFetches(f, toFetch);
			
		}
	}

	/**
	 * Copy Fetches
	 * @param from source Fetch
	 * @param to dest Fetch
	 */
	public static void copyFetches(Fetch<?, ?> from, Fetch<?, ?> to) {
		for (Fetch<?, ?> f : from.getFetches()) {
			Fetch<?, ?> toFetch = to.fetch(f.getAttribute().getName());
			// recursively copy fetches
			copyFetches(f, toFetch);
		}
	}
	
	/**
	 * Test if the path exists
	 * @param path path to test on
	 * @param propertyPath path to test
	 * @return true if path exists
	 */
	public static boolean hasPath(Path<?> path, String propertyPath) {
		try {
			getPath(path, propertyPath);
			return true;
		}
		catch (Exception e) { // Hibernate throws NPE here.
			return false;
		}
	}
	
	/**
	 * Initialize a entity. 
	 * @param em entity manager to use
	 * @param entity entity to initialize
	 * @param depth max depth on recursion
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void initialize(EntityManager em, Object entity, int depth) {
		// return on nulls, depth = 0 or already initialized objects
		if (entity == null || depth == 0) { 
			return; 
		}
		
		PersistenceUnitUtil unitUtil = em.getEntityManagerFactory().getPersistenceUnitUtil();
		EntityType entityType = em.getMetamodel().entity(entity.getClass());
		Set<Attribute>  attributes = entityType.getDeclaredAttributes();
		
		Object id = unitUtil.getIdentifier(entity);
		
		if (id != null) {
			Object attached = em.find(entity.getClass(), unitUtil.getIdentifier(entity));

			for (Attribute a : attributes) {
				if (!unitUtil.isLoaded(entity, a.getName())) {
					if (a.isCollection()) {
						intializeCollection(em, entity, attached,  a, depth);
					}
					else if(a.isAssociation()) {
						intialize(em, entity, attached, a, depth);
					}
				}
			}
		}
	}
	

	/** 
	 * Initialize entity attribute
	 * @param em
	 * @param entity
	 * @param a
	 * @param depth
	 */
	@SuppressWarnings("rawtypes")
	private static void intialize(EntityManager em, Object entity, Object attached, Attribute a, int depth) {
		Object value = PropertyAccessorFactory.forDirectFieldAccess(attached).getPropertyValue(a.getName());
		if (!em.getEntityManagerFactory().getPersistenceUnitUtil().isLoaded(value)) {
			em.refresh(value);
		}
		
		PropertyAccessorFactory.forDirectFieldAccess(entity).setPropertyValue(a.getName(), value);
		
		initialize(em, value, depth - 1);
	}

	/**
	 * Initialize collection
	 * @param em
	 * @param entity
	 * @param a
	 * @param i
	 */
	@SuppressWarnings("rawtypes")
	private static void intializeCollection(EntityManager em, Object entity, Object attached, 
			Attribute a, int depth) {
		PropertyAccessor accessor = PropertyAccessorFactory.forDirectFieldAccess(attached);
		Collection c = (Collection) accessor.getPropertyValue(a.getName());
		
		for (Object o : c)
			initialize(em, o, depth -1);
		
		PropertyAccessorFactory.forDirectFieldAccess(entity).setPropertyValue(a.getName(), c);
	}
	
	/**
	 * Get all attributes where type or element type is assignable from class and has persistent type
	 * @param type entity type
	 * @param persistentType persistentType
	 * @param clazz class
	 * @return Set with matching attributes
	 */
	public static Set<Attribute<?, ?>> getAttributes(EntityType<?> type, PersistentAttributeType persistentType, 
			Class<?> clazz) {
		Set<Attribute<?, ?>> attributes = new HashSet<Attribute<?, ?>>();
		
		for (Attribute<?, ?> a : type.getAttributes()) {
			if (a.getPersistentAttributeType() == persistentType && isTypeOrElementType(a, clazz)) {
				attributes.add(a);
			}
		}
		
		return attributes;
	}
	
	/**
	 * Get all attributes of type by persistent type
	 * @param type 
	 * @param persistentType
	 * @return a set with all attributes of type with persistent type persistentType.
	 */
	public static Set<Attribute<?, ?>> getAttributes(EntityType<?> type, PersistentAttributeType persistentType) {
		return getAttributes(type, persistentType, Object.class);
	}
	
	/**
	 * Test if attribute is type or in collections has element type
	 * @param attribute attribute to test
	 * @param clazz Class to test
	 * @return true if clazz is asignable from type or element type
	 */
	public static boolean isTypeOrElementType(Attribute<?, ?> attribute, Class<?> clazz) {
		if (attribute.isCollection()) {
			return clazz.isAssignableFrom(((CollectionAttribute<?, ?>) attribute).getBindableJavaType());
		}
		
		return clazz.isAssignableFrom(attribute.getJavaType());
	}
	
	
	/**
	 * Gets the mappedBy value from an attribute
	 * @param attribute attribute
	 * @return mappedBy value or null if none.
	 */
	public static String getMappedBy(Attribute<?, ?> attribute) {
		String mappedBy = null;
		
		if (attribute.isAssociation()) {
			Annotation[] annotations = null;
			Member member = attribute.getJavaMember();
			if (member instanceof Field) {
				annotations = ((Field) member).getAnnotations();
			}
			else if (member instanceof Method) {
				annotations = ((Method) member).getAnnotations();
			}
			
			for (Annotation a : annotations) {
				if (a.annotationType().equals(OneToMany.class)) {
					mappedBy = ((OneToMany) a).mappedBy();
					break;
				}
				else if (a.annotationType().equals(ManyToMany.class)) {
					mappedBy = ((ManyToMany) a).mappedBy();
					break;
				}
				else if (a.annotationType().equals(OneToOne.class)) {
					mappedBy = ((OneToOne) a).mappedBy();
					break;
				}
			}
		}
		
		return "".equals(mappedBy) ? null : mappedBy;
	}
}
