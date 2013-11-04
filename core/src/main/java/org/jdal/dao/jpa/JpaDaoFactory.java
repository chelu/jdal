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

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jdal.dao.Dao;
import org.jdal.dao.DaoFactory;

/**
 * Dao Factory implementation that create JpaDaos on the fly. 
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 * @see org.jdal.dao.jpa.JpaDao
 * @see org.jdal.service.PersistentServiceFactory
 * @since 1.1
 */
public class JpaDaoFactory implements DaoFactory {
	@PersistenceContext
	private EntityManager em;
	
	/**
	 * {@inheritDoc}
	 */
	public <T> Dao<T, Serializable> createDao(Class<T> entityClass) {
		JpaDao<T, Serializable> dao = new JpaDao<T, Serializable>(entityClass);
		dao.setEntityManager(em);
		return dao;
	}

}
