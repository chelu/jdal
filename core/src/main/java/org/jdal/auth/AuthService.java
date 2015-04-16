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

/**
 * An Auth Service to validate Users
 * 
 * @author Jose Luis Martin
 * @since 1.0
 */
public interface AuthService {
	
	/**
	 * Validate a username.
	 * @param username the user name
	 * @param password the supplied user password
	 * @return true if password match
	 */
	boolean validate(String username, String password);

}
