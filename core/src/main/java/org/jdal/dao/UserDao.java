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
package org.jdal.dao;

import org.jdal.model.User;

/**
 * Interface for User DAOs
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public interface UserDao extends Dao<User, Long> {
	
	/**
	 * Find a User by username
	 * @param username the user name
	 * @return the User that match username
	 */
	User findByUsername(String username);

}
