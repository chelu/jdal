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
public class DbConnection {
	
	private Database database;
	private String host;
	private int port;
	private String dbName;
	
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
	public int getPort() {
		return port;
	}
	
	/**
	 * @param port the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}
	
	/**
	 * @return the name
	 */
	public String getDbName() {
		return dbName;
	}
	
	/**
	 * @param name the name to set
	 */
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
}
