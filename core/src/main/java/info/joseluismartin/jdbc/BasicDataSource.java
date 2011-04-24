package info.joseluismartin.jdbc;

import java.util.Properties;

/**
 * dbcp BasicDataSource that expose connectionProperties as property
 * for use with Spring IoC 
 * 
 * @author Jose Luis Martin - (jolmarting@matchmind.es)
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
