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
package info.joseluismartin.db;

import java.util.ArrayList;
import java.util.List;

/**
 * Database model for use with DbConnectionForm
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class Database {
	
	public static List<Database> DATABASES;
	
	private static String[][] DBDATA = {
		{ "MySQL", "mysql", "com.mysql.jdbc.Driver", "3306" }, 
		{ "ORACLE", "oracle:thin", "oracle.jdbc.driver.OracleDriver", "1521" }, 
		{ "SQLite", "sqlite", "org.sqlite.JDBC", "" }
	};
	
	static {
		DATABASES  = new ArrayList<Database>();
		
		for (String[] d : DBDATA) {
			DATABASES.add(new Database(d[0], d[1], d[2], d[3]));
		}

	}

	private String name;
	private String jdbcName;
	private String driver;
	private String defaultPort;
	
	public Database() {
		this(null, null, null, null);
	}
	
	public Database(String name, String jdbcName, String driver, String defaultPort) {
		this.name = name;
		this.jdbcName = jdbcName;
		this.driver = driver;
		this.defaultPort = defaultPort;
		
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

	/**
	 * @return driver string
	 */
	public String getDriver() {
		return driver;
	}

	/**
	 * @param driver the driver to set
	 */
	public void setDriver(String driver) {
		this.driver = driver;
	}
	
	public String toString() {
		return name;
	}

	/**
	 * @return the defaultPort
	 */
	public String getDefaultPort() {
		return defaultPort;
	}

	/**
	 * @param defaultPort the defaultPort to set
	 */
	public void setDefaultPort(String defaultPort) {
		this.defaultPort = defaultPort;
	}
}
