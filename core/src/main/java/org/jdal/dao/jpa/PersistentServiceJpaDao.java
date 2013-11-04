/*
 * Copyright 2009-2012 Jose Luis Martin.
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jdal.dao.Dao;
import org.jdal.service.PersistentService;

/**
 * Support class for JpaDaos that implements PersistentService.
 *
 * In next versions may be Dao interface will extend PersistentService directly, 
 * for now, this class can be used for the same purpose.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class PersistentServiceJpaDao<T,PK extends Serializable> extends JpaDao<T, PK> 
	implements Dao<T,PK>, PersistentService<T, PK> {

	
	public PersistentServiceJpaDao() {
	
	}
	
	/**
	 * @param entityClass the entity class
	 */
	public PersistentServiceJpaDao(Class<T> entityClass) {
		super(entityClass);
	}

	/**
	 * {@inheritDoc}
	 */
	public void delete(Collection<T> collection) {
		for (T t : collection) {
			delete(t);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection<T> save(Collection<T> collection) {
		List<T> saved = new ArrayList<T>();

		for (T t : collection) {
			saved.add(save(t));
		}

		return saved;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void deleteById(Collection<PK> ids) {
		for (PK id : ids)
			deleteById(id);

	}
	
}
