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
package dao.jpa;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.jdal.dao.Filter;
import org.jdal.dao.jpa.JpaCriteriaBuilder;

/**
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 *
 */
public class BookJoinCriteriaQuery implements JpaCriteriaBuilder<Book> {

	/**
	 * {@inheritDoc}
	 */
	public CriteriaQuery<Book> build(CriteriaQuery<Book> criteria, CriteriaBuilder cb, Filter filter) {
		Root<Book> root = criteria.from(Book.class);
		
		return criteria.where(root.join("category").isNotNull());
	}

}
