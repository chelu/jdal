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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * An AuthStrategy that use HashMD5
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 *
 */
public class AuthHashMD5 implements AuthStrategy {

	/** log */
	private static Log log = LogFactory.getLog(AuthHashMD5.class);
	/**
	 * Test if userPassword is a md5 hash of suppliedPassword
	 * 
	 * @param suppliedPassword password form user
	 * @param userPassword password from db
	 * @return true if passwords match
	 */
	public boolean validate(String suppliedPassword, String userPassword) {
		if (suppliedPassword == null || userPassword == null) {
			return false;
		}

		try {
			String encriptedPassword = crypt(suppliedPassword);

			return userPassword.equals(encriptedPassword);
		} catch (NoSuchAlgorithmException nsae) {
			log.error(nsae);
			return false;
		}
	}
	
	/**
	 * Encript password 
	 * @param suppliedPassword
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public  String crypt(String suppliedPassword)
			throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(suppliedPassword.getBytes());
		String encriptedPassword = String.valueOf(md.digest());
		return encriptedPassword;
	}
}
