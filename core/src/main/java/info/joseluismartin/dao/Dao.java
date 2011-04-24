/**
 * 
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
     * @return List of populated objects
     */
    List<T> getAll();

    /**
	 * Find object by id
     * @param id the identifier (primary key) of the object to get
     * @return the object or null if none
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
     * @param id the identifier (primary key) of the object to remove
     */
    void deleteById(PK id);
    
    /**
     * Delete a object 
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

    
    T initialize(T entity);
	T initialize(T entity, int depth);
}
