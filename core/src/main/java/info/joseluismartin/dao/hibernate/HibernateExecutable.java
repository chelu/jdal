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
package info.joseluismartin.dao.hibernate;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.NonUniqueResultException;
import org.hibernate.Query;
import org.hibernate.transform.ResultTransformer;


/**
 * Wrapper for common methods in Query and Criteria interfaces
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
@SuppressWarnings("unchecked")
public class HibernateExecutable {
	
	
	public static final String LIST = "list";
	public static final String UNIQUE_RESULT = "uniqueResult";
	public static final String SET_MAX_RESULTS = "setMaxResults";
	public static final String SET_FIRST_RESULT = "setFirstResult";
	public static final String SET_RESULT_TRANSFORMER = "setResultTransformer";
	
	private static final Log log = LogFactory.getLog(HibernateExecutable.class);
	private Object executable;
	private Map<String, Method> methodMap = new HashMap<String,	Method>();
	
	public HibernateExecutable(Criteria criteria) {
		this.executable = criteria;
	}
	
	public HibernateExecutable(Query query) {
		this.executable = query;
	}
	
	/**
	 * Return the query results as a <tt>List</tt>. If the query contains
	 * multiple results pre row, the results are returned in an instance
	 * of <tt>Object[]</tt>.
	 *
	 * @return the result list
	 * @throws HibernateException
	 */
	
	public List list() throws HibernateException {
		return (List) invoke(LIST);
	}
	
	/**
	 * Convenience method to return a single instance that matches
	 * the query, or null if the query returns no results.
	 *
	 * @return the single result or <tt>null</tt>
	 * @throws NonUniqueResultException if there is more than one matching result
	 */
	public Object uniqueResult() throws HibernateException {
		return invoke(UNIQUE_RESULT);
	}
	
	/**
	 * @param uniqueResult
	 * @return
	 */
	private Object invoke(String name, Object...args ) {
		
		Method method = methodMap.get(name);
		if (method == null) {
			Class clazz = executable.getClass();
			Class[] types = new Class[args.length];
			
			for (int i = 0; i < args.length; i++) {
				types[i] = args[i].getClass();
			}
			
			try {
				method = ClassUtils.getPublicMethod(clazz, name, types);
			} catch (Exception e) {
				log.error(e);
			}
			
			methodMap.put(name, method);
		}
		
		try {
			return method.invoke(executable, args);
		} catch (Exception e) {
			log.error(e);
		}
		
		return null;
	}

	/**
	 * Set the maximum number of rows to retrieve. If not set,
	 * there is no limit to the number of rows retrieved.
	 * @param maxResults the maximum number of rows
	 */
	public HibernateExecutable setMaxResults(int maxResults) {
		invoke(SET_MAX_RESULTS, maxResults); 
		return this;
	}
	
	/**
	 * Set the first row to retrieve. If not set, rows will be
	 * retrieved beginnning from row <tt>0</tt>.
	 * @param firstResult a row number, numbered from <tt>0</tt>
	 */
	public HibernateExecutable setFirstResult(int firstResult) {
		invoke(SET_FIRST_RESULT, firstResult);
		return this;
	}
	
	/**
	 * Set a strategy for handling the query results. This can be used to change
	 * "shape" of the query result.
	 *
	 * @param transformer The transformer to apply
	 * @return this (for method chaining)	
	 */
	public HibernateExecutable HibersetResultTransformer(ResultTransformer transformer) {
		invoke(SET_RESULT_TRANSFORMER, transformer);
		return this;
	}

}
