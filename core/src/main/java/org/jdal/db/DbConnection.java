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
package org.jdal.db;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 *
 */
public class DbConnection implements Serializable {
	
	private static Log log = LogFactory.getLog(DbConnection.class);
	
	public static final String DRIVER = "jdbc.driverClassName";
	public static final String URL = "jdbc.url";
	public static final String USERNAME = "jdbc.username";
	public static final String PASSWORD = "jdbc.password";
	public static final String FILE_NAME = "jdbc.properties";
	
	private Database database;
	private String host;
	private String port;
	private String dbName;
	private String user;
	private String password;
	private String url;
	
	public boolean test() {
		boolean success = false;
		// Try to connect
		try {
	      Class.forName(database.getDriver());
	      String connectionUrl = buildUrl();
	      Connection conn = DriverManager.getConnection(connectionUrl, user, password); 
	      conn.close(); 
	      success = true;

	    } 
	    catch (Exception e) {
	    	log.error(e);
	    }
		
		return success;
	}
	
	/**
	 * @return the database
	 */
	
	public Database getDatabase() {
		return database;
	}
	/**
	 * @param database the database to set
	 */
	
	public void setDatabase(Database database) {
		this.database = database;
	}
	
	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}
	
	/**
	 * @param host the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}
	
	/**
	 * @return the port
	 */
	public String getPort() {
		return port;
	}
	
	/**
	 * @param port the port to set
	 */
	public void setPort(String port) {
		this.port = port;
	}
	
	/**
	 * @return the name
	 */
	public String getDbName() {
		return dbName;
	}
	
	/**
	 * @param dbName the database name to set
	 */
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}
	/**
	 * @param user the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return JDBC url connection string
	 */
	public String buildUrl() {
		if (StringUtils.isEmpty(port))
			port = database.getDefaultPort();
		
		// FIXME: obiusly :)
		if (database.getName().equals("ORACLE")) {
			this.url = "jdbc:" + getDatabase().getJdbcName() + "://@" + getHost() +
					":" + getPort() + ":" + getDbName(); 
		}
		else {
			StringBuilder sb = new StringBuilder("jdbc:");
			sb.append(getDatabase().getJdbcName());
			sb.append("://");
			
			if (!StringUtils.isEmpty(getHost())) 
				sb.append(getHost());
			
			if (!StringUtils.isEmpty(getPort())) {
				sb.append(":");
				sb.append(getPort());
			}
			
			if (!StringUtils.isEmpty(getDbName())) {
				sb.append("/");
				sb.append(getDbName());
			}
				
			this.url = sb.toString();
		}
			
			return url;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
		try {
			String[] parts = url.split("//");
			String[] hpd = parts[1].split(":");
			if (hpd[0].startsWith("@"))
				setHost(hpd[0].substring(1));
			else 
				setHost(hpd[0]);
			
			if (hpd.length == 3) {
				setPort(hpd[1]);
				setDbName(hpd[2]);
			}
			else {
				String[] pd = hpd[1].split("/");
				setPort(pd[0]);
				setDbName(pd[1]);
				
			}
		}
		catch (Exception e) {
			log.error(e);
		}
			
		
	}

	/**
	 * @param driver
	 */
	public void setDriver(String driver) {
		if (database == null)
			database = new Database();
		
		database.setDriver(driver);
		
	}
	
	public String getDriver() {
		return database != null ? database.getDriver() : null;
	}
	
	public void fromProperties(Properties prop) {
		setUser(prop.getProperty(USERNAME));
		setPassword(prop.getProperty(PASSWORD));
		setUrl(prop.getProperty(URL));
		setDriver(prop.getProperty(DRIVER));
		
		for (Database db :Database.DATABASES)
			if (db.getDriver().equals(getDriver()))
				setDatabase(db);
	}

	/**
	 * @return connection properties
	 */
	public Properties toProperties() {
		Properties prop = new Properties();
		prop.put(DRIVER, getDatabase().getDriver());
		prop.put(URL, getUrl());
		prop.put(USERNAME, getUser());
		prop.put(PASSWORD, getPassword());

		return prop;
	}
}
