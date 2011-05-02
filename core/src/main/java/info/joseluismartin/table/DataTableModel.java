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
package info.joseluismartin.table;

import info.joseluismartin.dao.Page;

import java.util.Collection;

/**
 * TableModel data, used in TableEditor for request a page.
 * 
 * @author Ignacio Vargas 
 */
public class DataTableModel<T> {
	
	/** number of records */
	private int totalRecords;
	/** startIndex */
	private int startIndex;
	/** sort order */
	private String sort;
	/** the size of one page */
	private int results;
	/** sort order dir (asc, des) */
	private String dir;
	/** List with model records */
	private Collection<?> records;
	/** json string with filter */
	private String filter;
	
	/**
	 * @return the totalRecords
	 */
	public int getTotalRecords() {
		return totalRecords;
	}
	
	/**
	 * @param totalRecords the totalRecords to set
	 */
	public void setTotalRecords(int totalRecords) {
		this.totalRecords = totalRecords;
	}
	
	/**
	 * @return the startIndex
	 */
	public int getStartIndex() {
		return startIndex;
	}
	
	/**
	 * @param startIndex the startIndex to set
	 */
	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}
	
	/**
	 * @return the sort
	 */
	public String getSort() {
		return sort;
	}
	
	/**
	 * @param sort the sort to set
	 */
	public void setSort(String sort) {
		this.sort = sort;
	}
	
	/**
	 * @return the pageSize
	 */
	public int getResults() {
		return results;
	}
	
	/**
	 * @param results the results to set
	 */
	public void setResults(int results) {
		this.results = results;
	}
	
	/**
	 * @return the dir
	 */
	public String getDir() {
		return dir;
	}
	
	/**
	 * @param dir the dir to set
	 */
	public void setDir(String dir) {
		this.dir = dir;
	}
	
	/**
	 * @return the records
	 */
	public Collection<?> getRecords() {
		return records;
	}
	
	/**
	 * @param records the records to set
	 */
	public void setRecords(Collection<?> records) {
		this.records = records;
	}
	
	/**
	 * Create a page definition from request data
	 * @return a pageDefinition 
	 */
	public Page<T> getPage() {
		return new Page<T>();
	}

	/**
	 * @return the filter
	 */
	public String getFilter() {
		return filter;
	}

	/**
	 * @param filter the filter to set
	 */
	public void setFilter(String filter) {
		this.filter = filter;
	}
}


