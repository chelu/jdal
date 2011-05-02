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

/**
 * An Auth Strategy that use plain passwords.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class AuthPlain implements AuthStrategy {

	/**
	 * Compare  supplied password wiht stored user password
	 * @param suppliedPassword the supplied password
	 * @param userPassword the user password
	 * @return true if both passwords are equals and not null
	 */
	public boolean validate(String suppliedPassword, String userPassword) {
		if (suppliedPassword == null || userPassword == null) {
			return false;
		}
		
		return suppliedPassword.equals(userPassword);
	}

	/**
	 * 
	 */
	public String crypt(String suppliedPassword) {
		return suppliedPassword;
	}

}
