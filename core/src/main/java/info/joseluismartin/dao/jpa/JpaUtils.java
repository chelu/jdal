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
package info.joseluismartin.dao.jpa;

import info.joseluismartin.beans.PropertyUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;

/**
 * Utility class for dealing with JPA API
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public abstract class JpaUtils {
	
	private static String ALIAS_PATTERN_STRING = "(?<=from)\\s+(?:\\S+)\\s+(?:as\\s+)*(\\w*)";
	private static Pattern ALIAS_PATTERN = Pattern.compile(ALIAS_PATTERN_STRING, Pattern.CASE_INSENSITIVE);
	private static String FROM_PATTERN_STRING = "(from.*+)";
	private static Pattern FROM_PATTERN = Pattern.compile(FROM_PATTERN_STRING, Pattern.CASE_INSENSITIVE);
	/**
	 * Find the Root with type class on Set
	 * @param <T> root type
	 * @param roots root set
	 * @param clazz root type
	 * @return Root<T> of null if none
	 */
	public static  <T> Root<T> findRoot(CriteriaQuery<?> query, Class<T> clazz) {
		Root<T> root = null;
		for (Root<?> r : query.getRoots()) {
			if (clazz.equals(r.getJavaType())) {
				root = (Root<T>) r.as(clazz);
				break;
			}
		}
		return root;
	}
	
	/**
	 * Find Joined Root of type clazz
	 * @param <T>
	 * @param query
	 * @param clazz
	 * @return
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
	 * @return
	 */
	public static Path<?> getPath(Path<?> path, String propertyPath) {
		if (StringUtils.isEmpty(propertyPath))
			return path;
		
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
}
