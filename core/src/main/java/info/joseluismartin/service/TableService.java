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
package info.joseluismartin.service;

import info.joseluismartin.model.TableState;
import info.joseluismartin.model.User;

/**
 * Read and Save TableStates 
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public interface TableService {
	
	/**
	 * Get TableState for user and table name
	 * @param name the table name
	 * @return the table state, null if none
	 */
	TableState getState(String name);
	
	/**
	 * Save the given TableState
	 * @param state the state 
	 */
	void saveState(TableState state);
}
