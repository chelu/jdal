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
package org.jdal.ui.action;


import java.util.List;

import javax.swing.JComboBox;

import org.jdal.dao.Page;
import org.jdal.dao.filter.PatternFilter;
import org.jdal.service.PersistentService;

/**
 * AutoComletionListener based on PersistentService and PatternFilter
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class FilterAutoCompletionListener extends AutoCompletionListener  {

	public static final String DEFAULT_SORT_PROPERTY = "name";
	private PersistentService persistentService;
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
	 * @param maxValue maximun number of results. 
	 */
	public FilterAutoCompletionListener(JComboBox combo, int maxValue) {
		this(combo, Short.MAX_VALUE, DEFAULT_SORT_PROPERTY);
	}
	
	/**
	 * Create and add the autocompletion listener to JComboBox
	 * @param combo the combo to add autocompletion-
	 * @param maxResults maximun number of results.
	 * @param sortProperty property for ordering, by default "name". 
	 */
	public FilterAutoCompletionListener(JComboBox combo, int maxResults,
			String sortProperty) {
		super(combo);
		this.maxResults = maxResults;
		this.sortProperty = sortProperty;
	}

	@Override
	protected List getList(String editing) {
		Page<?> page = new Page<Object>(maxResults);
		PatternFilter filter = new PatternFilter();
		filter.setPattern(editing.trim() + "%");
		page.setFilter(filter);
		
		return persistentService.getPage(page).getData();
		
	}
	
	/**
	 * @return the persistentService
	 */
	public PersistentService getPersistentService() {
		return persistentService;
	}
	
	/**
	 * @param persistentService the persistentService to set
	 */

	public void setPersistentService(PersistentService persistentService) {
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
