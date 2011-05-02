/*
 * Copyright 2008-2011 the original author or authors.
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
package info.joseluismartin.jdbc;

import java.util.Properties;

/**
 * dbcp BasicDataSource that expose connectionProperties as property
 * for use with Spring IoC 
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */

public class BasicDataSource extends org.apache.commons.dbcp.BasicDataSource {

	/**
	 * @return the connection properties to get
	 */
	public Properties getConnectionProperties() {
		return connectionProperties;
	}
	/**
	 * @param prop the connection properties to set
	 */
	public void setConnectionProperties(Properties prop) {
		connectionProperties = prop;
	}
}
