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
package org.jdal.dao.jpa.query;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;

/**
 * Find named queries inspecting the PersistenceUnit metamodel
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class MetamodelQueryFinder extends QueryFinderSupport {
	
	/** metamodel to inspect */
	private Metamodel metamodel;

	public MetamodelQueryFinder() {
	}
	
	public MetamodelQueryFinder(Metamodel metamodel) {
		this.metamodel = metamodel;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Map<String, String> find() {
		Map<String, String> queryMap = new HashMap<String, String>();
		if (metamodel == null)
			return queryMap;
		
		for (EntityType<?> type : metamodel.getEntities()) 
			queryMap.putAll(new EntityTypeQueryFinder(type).find());
		
		return queryMap;
	}
}

