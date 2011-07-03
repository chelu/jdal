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
package info.joseluismartin.dao.hibernate;

import info.joseluismartin.dao.Dao;
import info.joseluismartin.dao.DaoFactory;

import java.io.Serializable;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * DaoFactory for Hibernate Daos.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 * @since 1.1
 */
public class HibernateDaoFactory implements DaoFactory {
	
	@Autowired
	private SessionFactory sessionFactory;

	/**
	 * {@inheritDoc}
	 */
	public <T> Dao<T, Serializable> createDao(Class<T> entityClass) {
		HibernateDao<T, Serializable> dao = new HibernateDao<T, Serializable>(entityClass);
		dao.setSessionFactory(sessionFactory);
		
		return dao;
	}

}
