/*
 * Copyright 2008-2010 the original author or authors.
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
package org.jdal.auth;

import org.jdal.dao.UserDao;
import org.jdal.model.User;

/**
 * An Auth Manager that uses AuthStrategy to authorize users
 * 
 * @author Jose Luis Martin
 * @since 1.0
 */
public class AuthManager implements AuthService {
	
	/** AuthStrategy used to compare passwords */
	private AuthStrategy authStrategy;
	/** UserDAO used to obtain user information */
	private UserDao userDao;
	
	/**
	 * Autenticate a User that was identified by password
	 * @param user the user to autenticate
	 * @param password plain user supplied password
	 * @return true if password is valid 
	 */
	public boolean authUser(User user, String password) {
		return user == null ? false 
				: authStrategy.validate(password, user.getPassword());
	}

	// AuthService Start
	/**
	 * Validate a user
	 * @param username  the username
	 * @param password the supplied user password
	 * @return true if password matchs.
	 */
	public final boolean validate(String username, String password) {
		return authUser(userDao.findByUsername(username), password);
	}
	// AutService End
	
	/**
	 * Getter for AuthStrategy
	 * @return the authStrategy
	 */
	public AuthStrategy getAuthStrategy() {
		return authStrategy;
	}

	/**
	 * Setter for AuthStrategy
	 * @param authStrategy the authStrategy to set
	 */
	public void setAuthStrategy(AuthStrategy authStrategy) {
		this.authStrategy = authStrategy;
	}

	/**
	 * @return the userDao
	 */
	public UserDao getUserDao() {
		return userDao;
	}

	/**
	 * @param userDao the userDao to set
	 */
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
}