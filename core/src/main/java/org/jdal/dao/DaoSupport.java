/*
 * Copyright 2009-2014 Jose Luis Martin.
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
package org.jdal.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Support class for {@link Dao} implementation.
 * 
 * @author Jose Luis Martin
 * @since 2.0
 *
 */
public abstract class DaoSupport<T, PK extends Serializable> implements Dao<T, PK>  {
	
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
