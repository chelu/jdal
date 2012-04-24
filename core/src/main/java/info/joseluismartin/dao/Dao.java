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
package info.joseluismartin.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Generic DAO with pageable datasource interface.
 *
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public interface Dao<T, PK extends Serializable> extends PageableDataSource<T> {
	
	/**
   	 * Get all models 
     * @return List of entities
     */
    List<T> getAll();

    /**
	 * Find object by id
     * @param id the identifier (primary key) of the object to get
     * @return entity or null if none
     */
    T get(PK id);

    /**
     * Checks for existence of an object of type T using the id arg.
     * @param id the id of the entity
     * @return - true if it exists, false if it doesn't
     */
    boolean exists(PK id);

    /**
	 * Save or update an object
     * @param object the object to save
     * @return the persisted object
     */
    T save(T object);

    /**
     * Delete an object by primary key
     * @param id the primary key
     */
    void deleteById(PK id);
    
    /**
     * Delete an entity
     * @param entity to delete
     */
    void delete(T entity);


    /**
     * Find a list of records by using a named query
     * @param queryName query name of the named query
     * @param queryParams a map of the query names and the values
     * @return a list of the records found
     */
    List<T> findByNamedQuery(String queryName, Map<String, Object> queryParams);

    
    /**
     * Initalize entity at default depth
     * @param entity to intialize
     * @return intialized entity
     */
    T initialize(T entity);
    
    /**
     * Initalize entity
     * @param entity to intialize
     * @param depth to descend.
     * @return intialized entity
     */
	T initialize(T entity, int depth);
	
	// Non generic get and getAll methods
	
	<E> E get(PK id, Class<E> clazz );
	
	<E> List<E> getAll(Class<E> clazz);
	
	Class<T> getEntityClass();
}
