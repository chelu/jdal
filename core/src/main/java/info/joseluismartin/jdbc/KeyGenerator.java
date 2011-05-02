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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.support.JdbcUtils;

/**
 * A database table key generator using longs
 * Fetch keys by incrementBy to avoid excesive locking of table key
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class KeyGenerator  {
	/** logger */
	private static Logger log = Logger.getLogger(KeyGenerator.class);
	/** own our conecction to database */
	private static Connection conn; 
	/** table key name */
	private String keyName; 	   
	/** prefix keys with prefix */
	private String prefix;	 		
	/** our nextId */
	private long nextId;     		
	/** our maxId */
	private long maxId;      		
	/** reserve incrementBy keys to avoid excesive locks */
	private int incrementBy; 		
	/** DataSource */
	private DataSource ds;       
	/** top for retries in recoverable network exceptions */
	private int maxRetries;          
	
	/**
	 * Ctor.
	 * @param ds DataSource to use
 	 * @param keyName key name
	 * @param incrementBy get keys by incrementBy 
	 * to avoid excesive locks on key table
	 * @throws ApplicationException generic application exception
	 */
	public KeyGenerator(DataSource ds, String keyName, int incrementBy) 
		throws ApplicationException {
		this(ds, keyName, incrementBy, null);
	}
	
	/**
	 * Ctor.
	 * @param ds DataSource to use
	 * @param keyName key name
	 * @param incrementBy incrementBy get keys by incrementBy 
	 * to avoid excesive locks on key table
	 * @param prefix key prefix
	 * @throws ApplicationException generic applicatin exception
	 */
	public KeyGenerator(DataSource ds, String keyName, int incrementBy, 
			String prefix) throws ApplicationException  {
		this.incrementBy = incrementBy;
		this.keyName = keyName;
		this.ds = ds;
		try {
			if (conn == null) {
				getConnection();
			}
		} catch (SQLException sqle) {
			throw  new ApplicationException(
					"Unable to create key gererator for table "
					+ keyName, sqle);
		}
	}
	
	/**
	 * @return the nex log key
	 * @throws ApplicationException generic application exception
	 */
	public synchronized long nextLongKey() throws ApplicationException {
		if (nextId == maxId) {
			reserveIds();
		}
		return  nextId++;
	}
	/**
	 * 
	 * @return next key
	 * @throws ApplicationException generic application exception
	 */
	public synchronized Key nextKey() throws ApplicationException {
		return prefix == null ? new Key(nextLongKey()) 
			: new Key(nextStringKey());
	}
	
	/**
	 * @return the next String key
	 * @throws ApplicationException generic application exception
	 */
	public synchronized String nextStringKey() throws ApplicationException {
		return prefix + nextLongKey();
	}
	
	/**
	 * reserve incrementBy Ids
	 * @throws ApplicationException generic application exception
	 */
	private synchronized void reserveIds() throws ApplicationException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		long newNextId;
		
		do {
			try {			
				checkConnection(maxRetries);
				stmt = conn.prepareStatement(
						"SELECT nextid from key_ids where name = ? for update");
				stmt.setString(1, keyName);
				rs = stmt.executeQuery();
				rs.next();
				newNextId = rs.getLong(1);	
				long newMaxId = newNextId  + incrementBy;
				stmt = null;
				stmt = conn.prepareStatement(
						"UPDATE key_ids SET nextid = ? where name = ?");
				stmt.setLong(1, newMaxId);
				stmt.setString(2, keyName);
				stmt.executeUpdate();
				conn.commit();
				nextId = newNextId;
				maxId = newMaxId;
				break;
			} catch (SQLException sqle) {
				String state = sqle.getSQLState();
				// try again on network exceptions (mysql)
				if ("08S01".equals(state) || "40001".equals(state)) {	
					log.warn(
							"Recoverable mysql network exception" 
							+ ", try to reconnect... " +  maxRetries);
					maxRetries--;
					
				} else {
					throw new ApplicationException(
							"Unable to generate key Ids", sqle);
				}
			} finally {
				JdbcUtils.closeResultSet(rs);
				JdbcUtils.closeStatement(stmt);
			}
		} while (maxRetries != 0);
	}

	/**
	 * Check if we are on retry and get new connection from datasource
	 * @param maxRetries max retries on connection faliure 
	 * @throws SQLException the SqlException thro for the driver
	 */
	private void checkConnection(int maxRetries) throws SQLException {
		if (maxRetries != this.maxRetries) {
			getConnection();		
		}
	}

	
	/**
	 * get new Connection from datasource 
	 * @throws SQLException the SQLException
	 */
	private synchronized void getConnection() throws SQLException {
		if (conn != null && !conn.isClosed() ) {
			JdbcUtils.closeConnection(conn);
		}
		
		conn = ds.getConnection();
		conn.setAutoCommit(false);
	}
	
	/**
	 * @param string the prefix to set
	 */
	public void setPrefix(String string) {
		prefix = string;
	}

	/**
	 * @return the maxRetries
	 */
	public int getMaxRetries() {
		return maxRetries;
	}

	/**
	 * @param maxRetries the maxRetries to set
	 */
	public void setMaxRetries(int maxRetries) {
		this.maxRetries = maxRetries;
	}
 }
