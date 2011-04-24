/**
 * 
 */
package info.joseluismartin.reporting;

import info.joseluismartin.dao.Page;
import info.joseluismartin.service.PersistentService;

import java.io.Serializable;

/**
 * This interface must be implemented by those classes that require working with reports
 * 
 * @author Jose A. Corbacho
 *
 */
public interface ReportDataProvider<T, PK extends Serializable> {

	/**
	 * Returns the data source used by this object
	 * @return the data source in use
	 */
	PersistentService<T, PK> getDataSource();
	
	public Object getFilter();
	
	public String getSortProperty();
	
	public Page.Order getSortOrder();
	
}
