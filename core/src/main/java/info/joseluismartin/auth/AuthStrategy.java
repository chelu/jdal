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
package info.joseluismartin.auth;

import java.security.NoSuchAlgorithmException;

/**
 * Strategy for validate user supplied passords with user stored passwords
 * 
 * @see AuthPlain 
 * @see AuthHashMD5
 * @see AuthManager
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public interface AuthStrategy {
	
	/**
	 * Validate password.
	 * 
	 * @param suppliedPassword supplied password
	 * @param userPassword user password
	 * @return true if password match.
	 */

	boolean validate(String suppliedPassword, String userPassword);
	/** 
	 * Crypt a password
	 * @param suppliedPassword
	 * @return crypted passord
	 * @throws NoSuchAlgorithmException
	 */
	String crypt(String suppliedPassword) throws NoSuchAlgorithmException;

}
