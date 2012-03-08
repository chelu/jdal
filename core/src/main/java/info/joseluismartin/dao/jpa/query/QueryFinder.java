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
package info.joseluismartin.dao.jpa.query;

import java.util.Map;

/**
 * Strategy to find named query string. 
 * JPA API don't support access to Query String from Query objects so we need 
 * this to try to find them.
 *  
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public interface QueryFinder {
	
	/**
	 * Find for NamedQueries and return a Map with name as key and query string as value.
	 * @return Map with query strings
	 */
	Map<String, String> find();
	
	/**
	 * Find a NameQuery String by name
	 */
	String find(String name);

}
