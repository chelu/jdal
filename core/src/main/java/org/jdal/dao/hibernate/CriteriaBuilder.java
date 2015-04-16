/*
 * Copyright 2008-2015 Jose Luis Martin.
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

/**
 * Criteria builder for filters
 * 
 * @author Jose Luis Martin
 * @since 1.0
 */
public interface CriteriaBuilder {

	/**
	 * Add Restrictions to Critera from filter
	 * @param criteria criteria to add restrictions
	 * @param filter Filter data
	 * @return criteria.
	 */
	Criteria build(Criteria criteria, Object filter);
}
