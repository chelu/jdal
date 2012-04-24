/*
 * Copyright 2009-2012 the original author or authors.
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

import info.joseluismartin.dao.Filter;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 *
 */
public abstract class JpaCriteriaBuilderSupport<T, F extends Filter> implements JpaCriteriaBuilder<T> {

	protected F filter;
	protected Root<T> root;
	protected CriteriaBuilder cb;
	protected Class<T> entityClass;

	
	public JpaCriteriaBuilderSupport(Class<T> entityClass) {
		this.entityClass = entityClass;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public CriteriaQuery<T> build(CriteriaQuery<T> criteria, CriteriaBuilder cb, Filter filter) {
		this.filter = (F) filter;
		root = criteria.from(entityClass);
		this.cb = cb;
		
		doBuild(criteria, cb, filter);
		
		return criteria;
	}
	
	/**
	 * Build criteria
	 * @param criteria
	 * @param cb
	 * @param filter
	 */
	protected abstract void doBuild(CriteriaQuery<T> criteria, CriteriaBuilder cb, Filter filter);
	
	/**
	 * Add a '=' Restriction on property
	 * @param propertyName property path
	 * @param value restriction value
	 */
	protected Predicate equal(String propertyName, Object value) { 
		return cb.equal(JpaUtils.getPath(root, propertyName), value);
	}
	
	/**
	 * Add a '<=' Restriction on property
	 * @param propertyName property path
	 * @param value restriction value
	 */
	protected <Y extends Comparable<? super Y>> Predicate lessThanOrEqualTo(String propertyName, Y value) {
		return cb.lessThanOrEqualTo(JpaUtils.<Y>getPath(root, propertyName), value);
	}
	
	/**
	 * Add a '>=' Restriction on property
	 * @param criteria Criteria to add restriction
	 * @param property property path
	 * @param value restriction value
	 */
	protected <Y extends Comparable<? super Y>> Predicate greatThanOrEqualTo(String propertyName,  Y value) {
		return cb.greaterThanOrEqualTo(JpaUtils.<Y>getPath(root, propertyName), value);
	}
	
	/**
	 * Add a like Restriction adding wrapping value on '%' and replacing '*'
	 * for '%'
	 * @param criteria Criteria to add restriction
	 * @param property property path
	 * @param value text for the ilike restriction
	 */
	protected Predicate like(String propertyName, String value) {
		String toMatch = ((String) value).trim();
		toMatch = toMatch.replace('*', '%');
		toMatch = "%" + toMatch + "%";
		return cb.like(JpaUtils.<String>getPath(root, propertyName), toMatch);
	}
}
