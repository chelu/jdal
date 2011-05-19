/*
 * Copyright 2002-2010 the original author or authors.
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
package info.joseluismartin.service;

import info.joseluismartin.dao.PageableDataSource;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;


/**
 * Base interface for persistent services.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 *
 */
public interface PersistentService<T, PK extends Serializable> extends PageableDataSource<T> {
	
	T initialize(T entity, int depth);
	
	T initialize(T entity);
	
	T save (T entity);
	
	void delete(T entity);
	
	void deleteById(PK id);
	
	List<T> getAll();
	
	// Collections
	Collection<T> save(Collection<T> collection);
	
	void delete(Collection<T> collection);
	
	void deleteById(Collection<PK> ids);

	/**
	 * @param id
	 * @return
	 */
	T get(PK id);
	
}
