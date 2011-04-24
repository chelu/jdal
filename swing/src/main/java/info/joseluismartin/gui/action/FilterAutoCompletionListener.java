package info.joseluismartin.gui.action;

import info.joseluismartin.dao.Page;
import info.joseluismartin.dao.filter.PatternFilter;
import info.joseluismartin.service.PersistentService;

import java.util.List;

import javax.swing.JComboBox;

/**
 * AutoComletionListener based on PersistentService and PatternFilter
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class FilterAutoCompletionListener extends AutoCompletionListener  {

	public static final String DEFAULT_SORT_PROPERTY = "name";
	private PersistentService<?,?>  persistentService;
	private int maxResults = Short.MAX_VALUE;
	private String sortProperty;
	
	public FilterAutoCompletionListener() {
		super();
	}

	/**
	 * Create and add the autocompletion listener to JComboBox
	 * @param combo the combo to add autocompletion
	 */
	public FilterAutoCompletionListener(JComboBox combo) {
		this(combo, Short.MAX_VALUE);
	}
	
	/**
	 * Create and add the autocompletion listener to JComboBox
	 * @param combo the combo to add autocompletion-
	 * @param maximun number of results. 
	 */
	public FilterAutoCompletionListener(JComboBox combo, int maxValue) {
		this(combo, Short.MAX_VALUE, DEFAULT_SORT_PROPERTY);
	}
	
	/**
	 * Create and add the autocompletion listener to JComboBox
	 * @param combo the combo to add autocompletion-
	 * @param maximun number of results.
	 * @param sortProperty property for ordering, by default "name". 
	 */
	public FilterAutoCompletionListener(JComboBox combo, int maxResults,
			String sortProperty) {
		super(combo);
		this.maxResults = maxResults;
		this.sortProperty = sortProperty;
	}

	

	@Override
	protected List<?> getList(String editing) {
		Page page = new Page(maxResults);
		PatternFilter filter = new PatternFilter();
		filter.setPattern(editing.trim() + "%");
		page.setFilter(filter);
		
		return persistentService.getPage(page).getData();
		
	}
	
	/**
	 * @return the persistentService
	 */
	public PersistentService<?, ?> getPersistentService() {
		return persistentService;
	}
	
	/**
	 * @param persistentService the persistentService to set
	 */
	public void setPersistentService(PersistentService<?, ?> persistentService) {
		this.persistentService = persistentService;
	}

	/**
	 * @return the maxResults
	 */
	public int getMaxResults() {
		return maxResults;
	}

	/**
	 * @param maxResults the maxResults to set
	 */
	public void setMaxResults(int maxResults) {
		this.maxResults = maxResults;
	}

	/**
	 * @return the sortProperty
	 */
	public String getSortProperty() {
		return sortProperty;
	}

	/**
	 * @param sortProperty the sortProperty to set
	 */
	public void setSortProperty(String sortProperty) {
		this.sortProperty = sortProperty;
	}
}
