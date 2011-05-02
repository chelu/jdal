/*
 * Copyright 2005-2011 the original author or authors.
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

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

/**
 * Unit of Work pattern using object registration via registerXXX(DomainModel
 * dm) methods. <br>
 * Clean Models are not tracked <br>
 * 
 * @author Jose Luis Martin (chelu.es@gmail.com)
 */

@SuppressWarnings("unchecked")
public class Storer {

	static Logger log = Logger.getLogger(Storer.class);
	private static Map configuredMappers;
	private Map mappers;
	private List<DomainModel> newModels;
	private List<DomainModel> removedModels;
	private List<DomainModel> dirtyModels;
	static DataSource ds;
	Connection conn;

	// don't share Storer over threads
	static ThreadLocal<Storer> localStorer = new ThreadLocal();

	public Storer() {
		newModels = new ArrayList();
		removedModels = new ArrayList();
		dirtyModels = new ArrayList();
		if (configuredMappers != null)
			createMappers(configuredMappers);
	}

	/**
	 * Register a DomainModel as Dirty
	 * 
	 * @param dm
	 *            DomainModel to register
	 */
	public void registerDirty(DomainModel dm) {
		assert dm != null;
		
		if (!dirtyModels.contains(dm))
			dirtyModels.add(dm);
		log.debug("registry dirty dm: " + dm.getClass().getName() + dm.getKey());
	}

	/**
	 * Register a DomainModel as Clean
	 * 
	 * @param dm
	 *            DomainModel to register
	 */
	public void registerClean(DomainModel dm) {
	}

	/**
	 * Register a DomainModel as New
	 * 
	 * @param dm DomainModel to registry
	 */

	public void registerNew(DomainModel dm) {
		assert dm != null;
		
		if (!newModels.contains(dm))
			if (getMapper(dm).findByKey(dm.getKey()) == null) { 
				// must be really new
				newModels.add(dm);
				log.debug("registrando nuevo domain model: " + dm.getClass().getName() + dm.getKey());
			} else {
				dirtyModels.add(dm);
				log.debug("registrando nuevo domain model: " + dm.getClass().getName() + dm.getKey());
			}
	}

	public void registerDeleted(DomainModel dm) {
		assert dm != null;
		
		if (!removedModels.contains(dm))
			removedModels.add(dm);
		
		log.debug("register to delete: " + dm.getClass().getName() + dm.getKey());
	}

	public Mapper getMapper(DomainModel dm) {
		Mapper mapper = getMapper(dm.getClass());
		if (mapper != null)
			return mapper;
		Iterator iter = mappers.keySet().iterator();
		Class dmClazz = dm.getClass();
		while (iter.hasNext()) {
			Class clazz = (Class) iter.next();
			if (clazz.isAssignableFrom(dmClazz))
				return getMapper(clazz);

		}
		return null;
	}

	public Mapper getMapper(Class clazz) {
		return (Mapper) mappers.get(clazz);
	}
	
	@Transactional 
	public void doWork() {
		try {
			insert();
			update();
			remove();
		} catch (DataAccessException dae) {
			invalidate();
			throw dae;
		} finally {
			clearModels();
		}
	}

	private void clearModels() {
		newModels.clear();
		dirtyModels.clear();
		removedModels.clear();
		
	}

	private void invalidate() {
		invalidate(this.newModels);
		invalidate(this.dirtyModels);
		invalidate(this.removedModels);
		
	}

	/**
	 * Commit changes on domain models to Database
	 * <li>Insert new Models.
	 * <li>update dirty Models.
	 * <li>remove removed Models.
	 */
	public void commit() {
		log.debug("-------- iniciando commit");
		doWork();
		log.debug("-------- finalizando commit");
	}

	public void insert() {
		Iterator iter = newModels.iterator();
		while (iter.hasNext()) {
			DomainModel dm = (DomainModel) iter.next();
			getMapper(dm).insert(dm);
			dm.setSerial(1);	

			log.debug("insertado domain model: " + dm.getClass().getName() + dm.getKey());
		}
	}

	private void invalidate(List<DomainModel> models) {
		for (DomainModel model : models) 
			model.invalidate();
	}

	/**
	 * 
	 */
	public void clearNewModels() {
		newModels.clear();
	}

	public void update() {
		Iterator iter = dirtyModels.iterator();
		while (iter.hasNext()) {
			DomainModel dm = (DomainModel) iter.next();
			if (dm != null) {
				getMapper(dm).update(dm);
				dm.setSerial(dm.getSerial() + 1);
			}
			log.debug("actualizando dm: " + dm.getClass().getName() + dm.getKey());
		}
	}

	/**
	 * 
	 */
	public void clearDirtyModels() {
		dirtyModels.clear();
	}

	public void remove() {
		Iterator iter = removedModels.iterator();
		while (iter.hasNext()) {
			DomainModel dm = (DomainModel) iter.next();
			getMapper(dm).delete(dm);
			log.debug("eliminando dm: " + dm.getClass().getName() + dm.getKey());
		}
		clearRemovedModels();
	}

	/**
	 * 
	 */
	public void clearRemovedModels() {
		removedModels.clear();
	}

	/**
	 * Static Factory method for get Storer from a ThreadLocal variable
	 * 
	 * @return a Storer for this thread.
	 */
	public static Storer getInstance() {
		if (localStorer.get() == null) {
			Storer storer = new Storer();
			localStorer.set(storer);
		}

		return (Storer) localStorer.get();
	}

	public void addMapper(Class clazz, Object bean) {
		mappers.put(clazz, bean);
	}

	public void removeMapper(Class clazz) {
		mappers.remove(clazz);
	}

	/**
	 * Allow to set mappers with a map <class-name><Mapper>
	 * 
	 * @param mappers
	 */

	public void setMappers(Map mappers) {
		configuredMappers = mappers;
		createMappers(mappers);
	}

	private void createMappers(final Map mappers) {
		Map map = new HashMap();
		Set keys = mappers.keySet();
		Iterator iter = keys.iterator();
		while (iter.hasNext()) {
			Object key = iter.next();
			try {
				Class clazz = Class.forName(key.toString());
				map.put(clazz, mappers.get(key));
			} catch (ClassNotFoundException e) {
				log.error("Fail to find mapper class: " + key);
				log.error(e);
			}
		}
		this.mappers = map;
	}

	public Map getMappers() {
		return mappers;
	}

	public DomainModel lock(DomainModel dm) {
			return getMapper(dm).lock(dm);
			
	}

	public DataSource getDataSource() {
		return ds;
	}

	public void setDataSource(DataSource ds) {
		Storer.ds = ds;
	}
	
	// Single model insert, update and delete (without Unit Of Work)
	public void insertModel(DomainModel dm) {
		getMapper(dm).insert(dm);
	}
	
	public void updateModel(DomainModel dm) {
		getMapper(dm).update(dm);
	}
	
	public void deleteModel(DomainModel dm) {
		getMapper(dm).delete(dm);
	}
}
