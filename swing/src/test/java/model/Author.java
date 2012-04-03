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
package model;

import org.hibernate.validator.constraints.NotBlank;


public class Author {
	
	@NotBlank
	private String name;
	private String surname;
	private boolean bindFaliure = false;

	public Author(String name, String surname) {
		this.name = name;
		this.surname = surname;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		testFaliure();
		this.name = name;
	}

	/**
	 * @return the surname
	 */
	public String getSurname() {
		return surname;
	}

	/**
	 * @param surname the surname to set
	 */
	public void setSurname(String surname) {
		testFaliure();
		this.surname = surname;
	}

	/**
	 * 
	 */
	private void testFaliure() {
		if (bindFaliure)
			throw new IllegalArgumentException("I don't like it");
		
	}

	/**
	 * @return the bindFaliure
	 */
	public boolean isBindFaliure() {
		return bindFaliure;
	}

	/**
	 * @param bindFaliure the bindFaliure to set
	 */
	public void setBindFaliure(boolean bindFaliure) {
		this.bindFaliure = bindFaliure;
	}
}