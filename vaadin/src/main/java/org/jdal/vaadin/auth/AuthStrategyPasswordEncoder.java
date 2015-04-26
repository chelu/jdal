/*
 * Copyright 2009-2014 the original author or authors.
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
package org.jdal.vaadin.auth;

import java.security.NoSuchAlgorithmException;

import org.jdal.auth.AuthStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Adapter to use a {@link AuthStrategy} as {@link PasswordEncoder}
 * 
 * @author Jose Luis Martin
 * @since 2.1
 */
public class AuthStrategyPasswordEncoder implements PasswordEncoder {
	
	@Autowired(required=false)
	private AuthStrategy authStrategy;

	@Override
	public String encode(CharSequence rawPassword) {
		try {
			return this.authStrategy.crypt(rawPassword.toString());
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		return this.authStrategy.validate(rawPassword.toString(), encodedPassword);
	}
	
	/**
	 * @return the authStrategy
	 */
	public AuthStrategy getAuthStrategy() {
		return authStrategy;
	}

	/**
	 * @param authStrategy the authStrategy to set
	 */
	public void setAuthStrategy(AuthStrategy authStrategy) {
		this.authStrategy = authStrategy;
	}

}
