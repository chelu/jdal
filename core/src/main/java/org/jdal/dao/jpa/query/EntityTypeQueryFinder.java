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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.metamodel.EntityType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Find named queries declared annotated on EntityType.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class EntityTypeQueryFinder extends QueryFinderSupport {
	
	private static final Log log = LogFactory.getLog(EntityTypeQueryFinder.class);
	private EntityType<?> entityType;
	
	/**
	 * @param entityType
	 */
	public EntityTypeQueryFinder(EntityType<?> entityType) {
		this.entityType = entityType;
		
	}

	/**
	 * {@inheritDoc}
	 */
	public Map<String, String> find() {
		Map<String, String> queryMap = new HashMap<String, String>();
		List<NamedQuery> namedQueries = new ArrayList<NamedQuery>();
		NamedQueries nqs = entityType.getJavaType().getAnnotation(NamedQueries.class);
		if (nqs != null) {
			if (log.isDebugEnabled())
				log.debug("Found NamedQueries in type: " + entityType.getName());
			
			namedQueries.addAll(Arrays.asList(nqs.value()));
		}
		else {
			NamedQuery nq = entityType.getJavaType().getAnnotation(NamedQuery.class);
			if (nq != null) {
				if (log.isDebugEnabled())
					log.debug("Found NamedQueries in type: " + entityType.getName());
			
				namedQueries.add(nq);
			}				
		}
		
		for (NamedQuery nq : namedQueries)
			queryMap.put(nq.name(), nq.query());
		
		return queryMap;
	}


}
