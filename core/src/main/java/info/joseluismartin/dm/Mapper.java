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
