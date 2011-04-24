package info.joseluismartin.dm;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import org.apache.commons.collections.LRUMap;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.SqlFunction;
import org.springframework.jdbc.object.SqlOperation;
import org.springframework.jdbc.object.SqlQuery;
import org.springframework.jdbc.object.SqlUpdate;


/**
 * Base class for database mappers
 * <ul>
 * <li>Override <b>initPrimaryKeyParameters<b> method to declare <code>SqlParameter[]<code> parameters of 
 * primary key and <b>initInsertParameters</b> to declare <code>SqlParameter[]</code> parameters of 
 * the insert query. 
 * The update, delete and findByFilter default queries are deduced from the insert parameters.
 * However you can define your own querys and overwrite <b>initUpdateParameters</b> and <b>initFilterParameters</b> 
 * methods to declare <code>SqlParameter[]</code> parameters.
 * 
 * <li>Overwrite doLoad() method to do the load of DomainModel.
 *  Before load a model (using load()) their are checked in a Identity Map and compared with a serial
 *  long value from database. All updates must increment de serial number of the model in database or 
 * the optimistic locking will fail and you can get out of date memory models.
 * </ul>
 * 
 * For more information of how this class work, you can refer to <b>Abstract Mapper Supertype</b> in
 * Enterprise Application Patterns, http://www.martinfowler.com
 * 
 * @author Jose Luis Martin (chelu.es@gmail.com)
 * 		  
 */
@SuppressWarnings("unchecked")
public abstract class AbstractMapper implements Mapper {
	
	protected Logger log = Logger.getLogger(AbstractMapper.class);
	
	public enum ORDER {	DESC, ASC; }
	public static enum COLUMNS { }
	
	// Test loadedMap in findByKey
	boolean cachedModels = true;
	
	// DataSource
	protected DataSource ds;	

	private int maxSize = 200;
    // Identity Map
    protected Map<Key, DomainModel> loadedMap;
    
    // Table name for Table oriented mappers
    protected String tableName;
    
    // Table MetaData
    
    // Key Generator 
    protected KeyGenerator keyGenerator;
    
    // Maps columns names to bean properties
    protected Map colsProperties;
    
    // Query strings	
	protected String insertSql;
	protected String updateSql;
	protected String deleteSql;
	protected String deleteAllSql;
	//	protected String lockSql;	
	protected String allSql;	
	protected String countSql;
	protected String countByFilterSql;
	protected String findByKeySql;
	protected String findByFilterSql;
	protected String getSerialSql;
	
	// Spring Object Queries			
	protected SqlUpdate insertQuery;
	protected SqlUpdate updateQuery;
	protected SqlUpdate deleteQuery;
	protected SqlFunction getSerialQuery;
	protected SqlUpdate deleteAllQuery;	
	protected SqlQuery lockQuery;
	protected SqlQuery findByKeyQuery;
	protected SqlQuery allQuery;
	protected SqlQuery findByFilterQuery;
	protected SqlFunction countQuery;
	protected SqlFunction countByFilterQuery;	
    
    // Sql Parameters of queries
    protected SqlParameter[] primaryKeyParameters;
    protected SqlParameter[] insertParameters;
    protected SqlParameter[] updateParameters; 
    protected SqlParameter[] filterParameters;
    
    
    public AbstractMapper() {}
    
    
    /* Getters and Setters properties --- */
    
    public DataSource getDataSource() {
		return ds;
	}
	public void setDataSource(DataSource ds) {
		this.ds = ds;
	}
	
	public String getTableName() {
		return tableName;
	}	
	public void setTableName(String name) {
		tableName = name;
	}
	
	public Map getColsProperties() {
		return colsProperties;
	}
	public void setColsProperties(Map colsProperties) {
		this.colsProperties = colsProperties;
	}	
	
		
	public String getFindByKeySql() {
		return findByKeySql;
	}
	public void setFindByKeySql(String findByKeySql) {
		this.findByKeySql = findByKeySql;
	}

	public String getUpdateSql() {
		return updateSql;
	}
	public void setUpdateSql(String updateSql) {
		this.updateSql = updateSql;
	}
	
	public void setInsertSql(String insertSql) {
		this.insertSql = insertSql;
	}    
	public String getInsertSql() {
		return insertSql;
	}
	
	public String getDeleteSql() {
		return this.deleteSql;
	}	
	public void setDeleteSql(String deleteSql) {
		this.deleteSql = deleteSql;
	}
	
	public String getDeleteAllSql() {
		return this.deleteAllSql;
	}	
	
	public void setDeleteAllSql(String deleteAllSql) {
		this.deleteAllSql = deleteAllSql;
	}
    
	public String getFindByFilterSql() {
		return findByFilterSql;
	}
	
	public void setFindByFilterSql(String findByFilterSql) {
		this.findByFilterSql = findByFilterSql;
	}
		
	public String getAllSql() {
		return allSql;
	}
	public void setAllSql(String allSql) {
		this.allSql = allSql;
	}

	public String getCountByFilterSql() {
		return countByFilterSql;
	}
	
	public void setCountByFilterSql(String countByFilterSql) {
		this.countByFilterSql = countByFilterSql;
	}

	public String getCountSql() {
		return countSql;
	}
	
	public void setCountSql(String countSql) {
		this.countSql = countSql;
	}	
	
	public Map getLoadedMap() {
		return loadedMap;
	}
	
	public void setLoadedMap(Map map) {
		loadedMap = Collections.synchronizedMap(map);
	}
	
	public void setCachedModels(boolean value) {
		cachedModels = value;
	}
	
	public boolean isCachedModels() {
		return cachedModels;
	}
	
	/* --- end Getters and Setters properties */
	
	
	public void init() throws ApplicationException {
		
		loadedMap = new LRUMap(maxSize);
    	// initParameters
    	initPrimaryKeyParameters();
    	initInsertParameters();
    	initUpdateParameters();
    	initFilterParameters();
    	    	    	
    	// Create Basic Queries
    	checkDefaultQueries();
    	    	    	    	 
    	insertQuery = new SqlUpdate(ds, insertSql);
    	updateQuery = new SqlUpdate(ds, updateSql);
    	deleteQuery = new SqlUpdate(ds, deleteSql);
    	deleteAllQuery = new SqlUpdate(ds, deleteAllSql);
    	countQuery = new SqlFunction(ds, countSql);
    	countByFilterQuery = new SqlFunction(ds, countByFilterSql);
    	getSerialQuery = new SqlFunction(ds, getSerialSql);
    	    	
    	    	        
    	SqlOperation[] onlyKeyQueries = {deleteQuery, findByKeyQuery, lockQuery, getSerialQuery};
    	// Add Key parameter to Queries with only primary Key as parameter 
    	for (int i = 0; i < onlyKeyQueries.length; i++ ) {
    		SqlOperation sqlOp = onlyKeyQueries[i];
    		for (int j = 0; j < primaryKeyParameters.length; j++)
    			sqlOp.declareParameter(primaryKeyParameters[j]);
    		sqlOp.compile();
    	}
    	
    	SqlParameter[] params = getInsertParameters();
    	for (int i = 0; i < params.length; i++)
    		insertQuery.declareParameter(params[i]);    	    
    	    	
    	params = getUpdateParameters();
    	for (int i = 0; i < params.length; i++) 
    		updateQuery.declareParameter(params[i]);    	    
    	    	    
    	params = getFilterParameters();
    	for (int i = 0; i < params.length; i++) {
    		countByFilterQuery.declareParameter(params[i]);
    		findByFilterQuery.declareParameter(params[i]);
    	}
    	    	
    	insertQuery.compile();
    	updateQuery.compile();
    	allQuery.compile();
    	countQuery.compile();
    	countByFilterQuery.compile();
    	findByFilterQuery.compile();
    	    	
    	if (keyGenerator == null) 
    		keyGenerator = new KeyGenerator(ds, tableName, 100);
    }
  
    // Check insert, update, delete ... Strings and set to default if null
	private void checkDefaultQueries() {									
		if (insertSql == null) 
			insertSql = "INSERT INTO " + tableName + " (" + getFieldNames() + ")" + 
				" VALUES (" + getFieldPlaceHolders() + ")";
	
		if (updateSql == null)
			updateSql = "UPDATE " + tableName + " SET " + getUpdateFieldPlaceHolders() + 
				getSerialIncrement() + " WHERE " + getPrimaryKeyPlaceHolders();
	
		if (deleteSql == null)
			deleteSql = "DELETE FROM " + tableName + " WHERE " + getPrimaryKeyPlaceHolders();
		
		if (deleteAllSql == null)
			deleteAllSql = "DELETE FROM " + tableName;
		
		if (findByKeySql == null)
			findByKeySql = "SELECT " + getFieldNames()  + ", SERIAL" + " FROM " + tableName + 
				" WHERE " + getPrimaryKeyPlaceHolders();
		
		if (findByFilterSql == null)
			findByFilterSql = "SELECT " + getFieldNames()  + ", SERIAL" + " FROM " + tableName + 
				" WHERE " + getFilterFieldPlaceHolders();			
		
		if (allSql == null)
			allSql = "SELECT " + getFieldNames()  + ", SERIAL" + " FROM " + tableName;
		
		if (countSql == null)
			countSql = "SELECT COUNT(*) FROM " + tableName;
		
		if (countByFilterSql == null)
			countByFilterSql = 
				"SELECT COUNT(*) " + findByFilterSql.substring(findByFilterSql.indexOf("FROM"));
		
		if (getSerialSql == null)
			getSerialSql = "SELECT serial from " + tableName +
				" WHERE " + getPrimaryKeyPlaceHolders();
	}	
	
	private static String getSerialIncrement() {
		return ", serial = serial + 1 ";
	}

	public DomainModel lock(DomainModel dm) {
		dm.invalidate(); 
    	DomainModel locked = lock(dm.getKey());
    	return locked;
    }	
    
    public DomainModel lock(Key key) {
    	List list = lockQuery.execute(key.getFields());
    	return (DomainModel) list.get(0);
    }
    
	public DomainModel load(ResultSet rs) throws SQLException {
		Key key = getKey(rs);
    	DomainModel dm = null;
    	int serial = getSerial(rs);
    	
    	//  Test loaded map first    	    	
    	if (( (dm = (DomainModel) loadedMap.get(key)) != null)) {
    		log.debug("Found model: " + dm.getClass().getSimpleName() + " key: " + key + " in identity map");
    		if (dm.mustReload(serial)) {
    			// FIXME: should be a method or strategy
    			doLoad(dm, rs);  // refill model with new data from database
    			log.debug("Loaded model " + dm.getClass().getSimpleName() + " key: " + key + " due serial conflict");
    		}
    	}
    	else {// load data from resultset on a new Model
    		dm = doLoad(key, rs);
    		loadedMap.put(dm.getKey(), dm);
    		log.debug("Created new model: " + dm.getClass().getSimpleName() + " key: " + key.getFields());
    	}
    	// Model Loaded
    	dm.setSerial(serial);

    	return dm;
    }

	protected DomainModel getFromIdentityMap(Key key, ResultSet rs, int serial) throws SQLException {
		DomainModel dm = null;
	  	if (((dm = (DomainModel) loadedMap.get(key)) != null)) {
	  		if (dm.getSerial() != serial) {
	  			// FIXME: should be a method or strategy
	  			doLoad(dm, rs);  // refill model with new data from database
	  			dm.setSerial(serial);
	  		}
	  	}
		return dm;
	}
	
	  	
	protected int getSerial(ResultSet rs) throws SQLException {
		return rs.getInt("serial");
	}
	
	protected int getSerial(Key key) {
		return getSerialQuery.run(key.getFields());
	}


//	 Default implemetantion for insert, remove, update, findByKey, all, ...   
    public void insert(DomainModel dm) {
    	try {
    		loadedMap.put(dm.getKey(), dm);
    		log.debug("Inserting model: " + dm.getClass().getName() + " key: " + dm.getKey());
    		insertQuery.update(concatParameterValues(dm.getKeyAsArray(), dm.getValuesAsArray()));
    	
    	}
    	catch (DataAccessException dae) {
    		log.error(dae);  
    		log.error("model key: " + dm.getKey());
    		log.error("model values:" + dm);
    		throw dae;
		}
    }
    
	public void update(DomainModel dm) {
		try {		
			log.debug("Updating model: " + dm.getClass().getName() + " key: " + dm.getKey().toString());
			updateQuery.update(concatParameterValues(dm.getValuesAsArray(), dm.getKeyAsArray()));
			loadedMap.put(dm.getKey(), dm);		
		}
    	catch (DataAccessException dae) {
    		log.error(dae);   
    		log.error("model key: " + dm.getKey().toString());
    		log.error("model values:" + dm);
    		throw dae;
		}
    }	
    
    public void delete(DomainModel dm) {  
    	try {
    		log.debug("Deleting model: " + dm.getClass().getName() + " key: " + dm.getKey());
	    	deleteQuery.update(dm.getKeyAsArray());
	    	loadedMap.remove(dm.getKey());
	    }
		catch (DataAccessException dae) {
			log.error(dae);   
			log.error("model key: " + dm.getKey().toString());
    		log.error("model values:" + dm);
    		throw dae;
		}
    }
    
    public void deleteAll() {
    	try {
    		log.debug("Deleting all model from table: " + tableName);
			deleteAllQuery.update();
			loadedMap.clear();
	    }
		catch (DataAccessException dae) {
			log.error(dae);    		
		}
    }
	
	/**
     * Find a DomainModel by primary key checking first in a IdentityMap
     * @param key Primary Database Key.
     */
    public DomainModel findByKey(Key key) {
    	DomainModel model = null;
    	// try from identity map
    	if (cachedModels) {
    		model = (DomainModel) loadedMap.get(key);
    		if ( model != null  && !model.mustReload(getSerial(key)))
    			return model;
    	}
    	
    	// try load from database
    	List result = null;
    	try {
    		result = findByKeyQuery.execute(key.getFields());
    	}
		catch (DataAccessException dae) {
			log.error(dae);    		
		}
		
    	if (result != null && result.size() > 0) 
    		return  (DomainModel)  result.get(0);
    	// null if can't get model
    	log.warn("Cant't find model by key: Mapper:  " + getClass().getName() + " key: " + key);
    	return null;
    }
    
    // true if model and database row are equal
    private boolean checkSerial(DomainModel model) {
		return getSerial(model.getKey()) == model.getSerial() && 
		      ! model.isLoading();
	}


	/**
     * Find a DomainModel by long Id checking first in a IdentityMap
     * @param id primary key
     * @return a DomainModel with id as primary key or null if not found
     */
    public DomainModel findById(long id) {    	
    	log.debug(getClass().getName() + " findById long:" +id);
    	return findByKey(new Key(id));
    }
    
    /**
     * Find a DomainModel by String Id checking first in a IdentityMap
     * @param id primary key
     * @return a DomainModel with id as primary key or null if not found
     */
    public DomainModel findById(String id) {
    	log.debug(getClass().getName() + " findById string:" +id);
    	return findByKey(new Key(id));
    }
    
    public List all() {		
    	try {
			return allQuery.execute();
	    }
		catch (DataAccessException dae) {
			log.error(dae);    		
		}
		
		return new LinkedList();
	}	      
	
	public int count() {
		try {
			return countQuery.run();
		}
		catch (DataAccessException dae) {
			log.error(dae);    		
		}
		
		return 0;
	}

	// Default implementation of getKey
    protected Key getKey(ResultSet rs) throws SQLException {        	
    	/* FIXME:  
    	 * Al hacer un rs.getObject(xxx) o rs.getLong(xxx) de la PRIMARYKEY (Types.INTEGER)
    	 * devuelve 4294967596 en vez de 0, 4294967597 en vez de 1 y asi sucesivamente. 
    	 */
    	Object[] objKey = new Object[primaryKeyParameters.length]; 
    	for (int i=0; i < primaryKeyParameters.length; i++) {
    		if (primaryKeyParameters[i].getSqlType() == Types.INTEGER) {
    			objKey[i] = new Long(rs.getInt(primaryKeyParameters[i].getName()));
    		}
    		else {
    			objKey[i] = rs.getObject(primaryKeyParameters[i].getName());
    		}    		
    	}
    	
    	return new Key(objKey);
    }       
    
    public void showLoadedMap() {
    	System.out.println(">>> " + this.getClass().getName() + "->loadedMap:\n" + loadedMap.toString()+ "\n\n");
    }
    
    public void showDefaultQuerysSql() {
    	System.out.println(getClass().getName() + "Querys:"
    			+ "\n\t" + insertSql 
    			+ "\n\t" + updateSql
    			+ "\n\t" + deleteSql    			
    			+ "\n\t" + allSql
    			+ "\n\t" + countSql
    			+ "\n\t" + countByFilterSql
    			+ "\n\t" + findByKeySql
    			+ "\n\t" + findByFilterSql
    			+ "\n\t" + getSerialSql
    			);
    }       
    
    /**
     * Clear IdentityMap
     */
    public void clearLoadedMap() {
    	loadedMap.clear();
    }	
		
	
	protected String getLimit(int start, int step) {
		return " LIMIT " + start + ", " + step + " ";
	} 

	public String getFieldNames() {
		return getNamesFromParameters(primaryKeyParameters) + ", " + 
			   getNamesFromParameters(insertParameters);
	}
	
	protected String getNamesFromParameters(SqlParameter[] params) {
		String s = "";
		for (int i = 0; i < params.length; i++) {
			s += params[i].getName();
			if (i < params.length - 1)
				s += ", ";
		}
		
		return s;
	}	

	private String getPrimaryKeyPlaceHolders() {
		String s = "";
		for (int i=0; i < primaryKeyParameters.length; i++) {
			if (i > 0) s += " AND ";
			s += primaryKeyParameters[i].getName() + " = ?";
		}
				
		return s;
	}
	
	private String getFieldPlaceHolders() {
		String s = "";
		SqlParameter[] params = getInsertParameters();
		for (int i = 0; i < params.length; i++) {
			s += (i == params.length - 1)? "?" : " ?,";
		}
		
		return s;
	}
	
	private String getUpdateFieldPlaceHolders() {
		String s =  "";
		for (int i = 0; i < updateParameters.length; i++) {
			s += updateParameters[i].getName();
			if (i == updateParameters.length - 1)
				s += "=?";
			else 
				s += "=?, ";
		}
		
		return s;		
	}	
	
	private String getFilterFieldPlaceHolders() {			
		String s = "";
		SqlParameter[] params = getFilterParameters();
		for (int i=0; i < params.length; i++) {
			if (i > 0) s += " AND ";
			s += params[i].getName() + " LIKE ?";		
		}
		
		return s;
	}


	public static String quote(String string) {
		Pattern pattern = Pattern.compile("\'");
		Matcher m = pattern.matcher(string);
		return  "'" + m.replaceAll("\\\'") + "'";
	}
	
	public synchronized Key nextKey() throws ApplicationException {
		return keyGenerator.nextKey();
	}

	/**
	 * Simple init a <code>MappingSqlQuery</code> with one parameter
	 * 
	 * @param sql
	 * @param parameter
	 * @return
	 */
	
	
	protected SqlParameter[] getPrimaryKeyParameters() {
		if (primaryKeyParameters == null)
			initPrimaryKeyParameters();
		
		return primaryKeyParameters;
		
	}

	protected SqlParameter[] getInsertParameters() {
		if (insertParameters == null)
			initInsertParameters();
		
		return concatParameters(primaryKeyParameters, insertParameters);
	}		

	protected SqlParameter[] getUpdateParameters() {
		if (updateParameters == null) 
			initUpdateParameters();
		if (updateParameters == insertParameters)
			return concatParameters(updateParameters, primaryKeyParameters);		
					
		return updateParameters;
	}
	
	protected SqlParameter[] getFilterParameters() {
		if (filterParameters == null)
			initFilterParameters();			
				
		return filterParameters;
	}
		
	
	// Why i can't templatize Arrays :?
	protected SqlParameter[] concatParameters(SqlParameter[] a, SqlParameter[] b) {
		SqlParameter[] c = new SqlParameter[a.length+b.length];
		   System.arraycopy(a, 0, c, 0, a.length);
		   System.arraycopy(b, 0, c, a.length, b.length);
		 
		   return  c;
		 
	}

	protected Object[] concatParameterValues(Object[] a, Object[] b) {
		Object[] c = new Object[a.length + b.length];
		   System.arraycopy(a, 0, c, 0, a.length);
		   System.arraycopy(b, 0, c, a.length, b.length);
		 
		   return  c;
	}	
	
	protected String getColumnFromProperty(String propertyName) {
		return (String) colsProperties.get(propertyName);		
	}
	
	protected String getPropertyFromColumn(String colName) {
		//TODO:
		Iterator ite = colsProperties.entrySet().iterator();
		while(ite.hasNext()) {
			Map.Entry entry = (Map.Entry) ite.next();
			if (entry.getValue().equals(colName))
				return (String) entry.getKey();
		}		
		
		return null;			
	}
	

	
		
    // Abstract initializacion :?, will move to configuration later.
    public abstract void initPrimaryKeyParameters();
    public abstract void initInsertParameters();
    
    /**
     * By default update parameters = insert parameters
     */  
	public void initUpdateParameters() {
    	updateParameters = insertParameters;
    }
	/**
	 *  By default filter parameters = insert parameters
	 */
	public void initFilterParameters() {
//		filterParameters = insertParameters;			
		SqlParameter[] params = getInsertParameters();
		filterParameters = new SqlParameter[params.length];
		for (int i = 0; i < filterParameters.length; i++) {
			if (params[i].getSqlType() == Types.TIMESTAMP) 
				filterParameters[i] = new SqlParameter(params[i].getName(), Types.TIMESTAMP);						
			else 
				filterParameters[i] = new SqlParameter(params[i].getName(), Types.VARCHAR);
		}		
	}
    
	/** 
	 * Delegates in doLoad(DomainModel dm, ResultSet rs). 
	 * @param key
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
    public DomainModel doLoad(Key key, ResultSet rs) throws SQLException {
    	DomainModel model = newModel(key);
    	loadedMap.put(key, model);
    	doLoad(model, rs);
    	
    	return model;
    }
    
    /**
	 * extract data from row in jdbc ResultSet.
	 * @param rs
	 * @return a new DomainModel
	 * @throws SQLException
	 */ 
    public abstract void doLoad(DomainModel model, ResultSet rs) throws SQLException;
    
    /**
     * Factory Method to create a DomainModel for this mapper 
     * @return
     */
    public abstract DomainModel newModel(Key key);
    
    public DomainModel newModel() throws ApplicationException  {
    		return newModel(keyGenerator.nextKey());
    }
    
	public void remove(DomainModel dm) {
    	loadedMap.remove(dm.getKey());
    }
    
    public void put(DomainModel dm) {
    	loadedMap.put(dm.getKey(), dm);
    }

	public static SqlUpdate getSqlUpdate(DataSource ds, String updateSqlString) {
		String updateSql = updateSqlString;
		if (updateSql.startsWith("UPDATE ") && updateSql.indexOf(getSerialIncrement()) == -1)
			updateSql = updateSql.replace(" WHERE ", getSerialIncrement() + " WHERE ");
		
		return new SqlUpdate(ds, updateSql);
	}


	public int getMaxSize() {
		return maxSize;
	}

	public void setMaxSize(int maxSize) {
		this.maxSize = maxSize;
	}          	
}
