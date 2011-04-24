package info.joseluismartin.table;

import info.joseluismartin.dao.Dao;
import info.joseluismartin.dao.Filter;
import info.joseluismartin.dao.Page;
import info.joseluismartin.dao.hibernate.HibernateDao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * TableEditorManager
 * small wrapper that redirect calls to dao for use with table editor
 * 
 * @author Jose Luis Martin - (chelu.es@gmail.com) 
 */
@SuppressWarnings("unchecked")
public class TableEditorManager implements TableEditorService {
	
	/** dao */
	private HibernateDao dao;
	
	/**
	 * list of static filters configurated in Spring 
	 */
	private List<Filter> enabledFilters = new ArrayList<Filter>();


	/**
	 * Delete model 
	 * @param id to delete
	 */
	public void delete(Object id) {
		dao.delete((Serializable) id);
	}

	/**
	 * save model (insert or update) 
	 * @param model model to save
	 */
	public void save(Object model) {
		dao.save(model);
	}

	/**
	 * Return a page from page definition
	 * @param pageDefinition page Definition
	 * @return a page of models
	 */
	public Page getPage(Page page) {
		enableFilters();
		return dao.getPage(page);
	}

	/**
	 * @return the dao
	 */
	public Dao getDao() {
		return dao;
	}

	/**
	 * @param dao the dao to set
	 */
	public void setDao(HibernateDao dao) {
		this.dao = dao;
	}
	
	/**
	 * Return the number registers
	 * @return number registers
	 */
	public int count() {
		enableFilters();
		return dao.count();
	}

	/**
	 * Enables a filter
	 * @param filter filter to enable 
	 */
	public void enableFilter(Filter filter) {
		dao.enableFilter(filter);
		
	}
	
	/**
	 * Enable preconfigured filters
	 */
	protected void enableFilters() {
		for (Filter filter : enabledFilters) {
			enableFilter(filter);
		}
	}

	/**
	 * @return the enabledFilters
	 */
	public List<Filter> getEnabledFilters() {
		return enabledFilters;
	}

	/**
	 * @param enabledFilters the enabledFilters to set
	 */
	public void setEnabledFilters(List<Filter> enabledFilters) {
		this.enabledFilters = enabledFilters;
	}

}
