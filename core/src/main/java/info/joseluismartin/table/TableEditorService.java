package info.joseluismartin.table;

import info.joseluismartin.dao.Filter;
import info.joseluismartin.dao.Page;

/**
 * Service for TableEditor  
 * 
 * @author Jose Luis Martin - (chelu.es@gmail.com)
 */
public interface TableEditorService {
	
	/**
	 * Save a model (insert or update)
	 * @param model Object to save
	 */
	void save(Object model);
	/**
	 * delete a model
	 * @param model Object to delete
	 */
	void delete(Object model);
	
	/**
	 * get a page of models
	 * @param pageDefinition the page definition
	 * @return page
	 */
	Page getPage(Page page);
	
	
	/**
	 * Return the number registers
	 * @return number registers
	 */
	int count();
	
	/**
	 * Enable predefined filter
	 * 
	 * @param filter the filter data
	 */
	void enableFilter(Filter filter);
}
