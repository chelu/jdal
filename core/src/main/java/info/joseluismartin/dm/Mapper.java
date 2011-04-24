/*
 * Created on 29-sep-2005
 *
 */
package info.joseluismartin.dm;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


/**
 * Interface for database mappers.
 * 
 * @author Jose Luis Martin - (chelu.es@gmail.com)
 */

public interface Mapper {
	
	/**
	 * Lock the row table of the key
	 * @param key
	 * @return
	 */
	public DomainModel lock(Key key);
	/**
	 * Lock the table row that this model represents
	 * @param dm
	 * @return
	 */
	public DomainModel lock(DomainModel dm);
	/**
	 * Load data from a jdbc resultset
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	public DomainModel load(ResultSet rs) throws SQLException;
	/** 
	 * Find a DomainModel by database key
	 * @param key
	 * @return
	 */
	public DomainModel findByKey(Key key);
	/**
	 * Insert model
	 * @param dm
	 */
	public void insert(DomainModel dm);
	/**
	 * Update model 
	 * @param dm
	 */
	public void update(DomainModel dm);
	/** 
	 * Delete model
	 * @param dm
	 */
	public void delete(DomainModel dm);
	/** 
	 * Clear the mapper Identity map
	 */
	public void clearLoadedMap();
	/**
	 * Get all models 
	 * @return
	 */
	public List<DomainModel> all();
	/**
	 * Get models count
	 * @return
	 */
	public int count();
	/**
	 * Remove a model from indentity map.
	 * @param dm
	 */
	public void remove(DomainModel dm);
}
