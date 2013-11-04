/*
 * Copyright 2008-2010 the original author or authors.
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

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.util.StringUtils;

/**
 * Base class for CriteriaBuilders, add some utility methods.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public abstract class AbstractCriteriaBuilder implements CriteriaBuilder {

	public abstract Criteria build(Criteria criteria, Object filter);
	
	/**
	 * Add a '=' Restriction on property
	 * @param criteria Criteria to add restriction
	 * @param property property path
	 * @param value restriction value
	 */
	protected void eq(Criteria criteria, String property, Object value) {
		if (value != null)
			criteria.add(Restrictions.eq(property, value));
	}
	
	
	/**
	 * Add a '<=' Restriction on property
	 * @param criteria Criteria to add restriction
	 * @param property property path
	 * @param value restriction value
	 */
	protected void le(Criteria criteria, String property, Object value) {
		if (value != null)
			criteria.add(Restrictions.le(property, value));
	}
	
	/**
	 * Add a '>=' Restriction on property
	 * @param criteria Criteria to add restriction
	 * @param property property path
	 * @param value restriction value
	 */
	protected void ge(Criteria criteria, String property, Object value) {
		if (value != null)
			criteria.add(Restrictions.ge(property, value));
	}
	
	/**
	 * Add a ilike Restriction adding wrapping value on '%' and replacing '*'
	 * for '%'
	 * @param criteria Criteria to add restriction
	 * @param property property path
	 * @param value text for the ilike restriction
	 */
	protected void like(Criteria criteria, String property, String value) {
		if (StringUtils.hasText(value)) {
			String toMatch = ((String) value).trim();
			toMatch = toMatch.replace('*', '%');
			toMatch = "%" + toMatch + "%";
			criteria.add(Restrictions.ilike(property, toMatch));
		}
	}

}
