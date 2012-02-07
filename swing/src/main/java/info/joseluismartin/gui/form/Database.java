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
package info.joseluismartin.gui.form;

/**
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 *
 */
public class Database {
	private String name;
	private String jdbcName;
	
	public Database() {
		this(null, null);
	}
	
	public Database(String name, String jdbcName) {
		this.name = name;
		this.jdbcName = jdbcName;
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
		this.name = name;
	}
	/**
	 * @return the jdbcName
	 */
	public String getJdbcName() {
		return jdbcName;
	}
	/**
	 * @param jdbcName the jdbcName to set
	 */
	public void setJdbcName(String jdbcName) {
		this.jdbcName = jdbcName;
	}
}
