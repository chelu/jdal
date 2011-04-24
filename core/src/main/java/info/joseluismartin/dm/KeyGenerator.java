package info.joseluismartin.dm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.support.JdbcUtils;

/**
 * A database table key generator using longs
 * Fetch keys by incrementBy to avoid excesive locking of table key
 * 
 * @author Jose Luis Martin - (chelu.es@gmail.com)
 */
public class KeyGenerator  {
	private static Log log = LogFactory.getLog(KeyGenerator.class);
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
	
	public KeyGenerator(DataSource ds, String keyName, int incrementBy) throws ApplicationException {
		this(ds, keyName, incrementBy, null);
	}
	
	public KeyGenerator(DataSource ds, String keyName, int incrementBy, 
			String prefix) throws ApplicationException  {
		this.incrementBy = incrementBy;
		this.keyName = keyName;
		this.ds = ds;
		try {
			if (conn == null) {
				getConnection();
			}
		}
		catch (SQLException sqle) {
			throw  new ApplicationException("Unable to create key gererator for table " + keyName, sqle);
		}
	}
	
	/**
	 * @return the nex log key
	 * @throws ApplicationException
	 */
	public synchronized long nextLongKey() throws ApplicationException {
		if (nextId == maxId)
			reserveIds();
		return  nextId++;
	}
	/**
	 * 
	 * @return next key
	 * @throws ApplicationException
	 */
	public synchronized Key nextKey() throws ApplicationException {
		return prefix == null ? new Key(nextLongKey()) : new Key(nextStringKey());
	}
	
	/**
	 * @return the next String key
	 * @throws ApplicationException
	 */
	public synchronized String nextStringKey() throws ApplicationException {
		return prefix + nextLongKey();
	}
	
	/**
	 * reserve incrementBy Ids
	 * @throws ApplicationException
	 */
	private synchronized void reserveIds() throws ApplicationException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		long newNextId;
		
		do {
			try {			
				checkConnection(maxRetries);
				stmt = conn.prepareStatement("SELECT nextid from key_ids where name = ? for update");
				stmt.setString(1, keyName);
				rs = stmt.executeQuery();
				rs.next();
				newNextId = rs.getLong(1);	
				long newMaxId = newNextId  + incrementBy;
				stmt = null;
				stmt = conn.prepareStatement("UPDATE key_ids SET nextid = ? where name = ?");
				stmt.setLong(1, newMaxId);
				stmt.setString(2, keyName);
				stmt.executeUpdate();
				conn.commit();
				nextId = newNextId;
				maxId = newMaxId;
				break;
			} catch (SQLException sqle) {
				String state = sqle.getSQLState();
				// try again on network exceptions (mysql espefic?)
				if ("08S01".equals(state) || "40001".equals(state)) {	
					log.warn("Recoverable mysql network exception, try to reconnect... " +  maxRetries);
					maxRetries--;
					
				}
				else 
					throw new ApplicationException("Unable to generate key Ids", sqle);
			} finally {
				JdbcUtils.closeResultSet(rs);
				JdbcUtils.closeStatement(stmt);
			}
		} while (maxRetries != 0);
	}

	/**
	 * Check if we are on retry and get new connection from datasource
	 * @param maxRetries
	 * @throws SQLException
	 */
	private void checkConnection(int maxRetries) throws SQLException {
		if (maxRetries != this.maxRetries)
			getConnection();		
	}

	
	/**
	 * get new Connection from datasource 
	 * @throws SQLException
	 */
	private synchronized void getConnection() throws SQLException {
		if (conn != null && !conn.isClosed() ) 
			JdbcUtils.closeConnection(conn);
		
		conn = ds.getConnection();
		conn.setAutoCommit(false);
	}

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
