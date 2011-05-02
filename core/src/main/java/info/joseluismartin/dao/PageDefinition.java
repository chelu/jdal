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
package info.joseluismartin.dao;

import info.joseluismartin.Constants;

/**
 * Page Definition for use when requesting a Page
 * 
 * @author mjantunez 
 */
public class PageDefinition {
	
	/**
	 * sort
	 */
	private String sort = "ID";
	/**
	 * dir
	 */
	private String dir = Constants.ORDER_ASC;
	/**
	 * startIndex
	 */
	private int startIndex = 0;
	/**
	 * results
	 */
	private int results = 20;
	/**
	 * filter
	 */
	private Object  filter;
	
	/**
	 * @param sort the sort to set
	 * @param dir the dir to set
	 * @param startIndex the startIndex to set
	 * @param results the results to set
	 */
	public PageDefinition(String sort, String dir, int startIndex,
			int results) {
		this.sort = sort;
		this.dir = dir;
		this.startIndex = startIndex;
		this.results = results;
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
	 * @return the results
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
	 * @return the filter
	 */
	public Object getFilter() {
		return filter;
	}
	/**
	 * @param filter the filter to set
	 */
	public void setFilter(Object filter) {
		this.filter = filter;
	}
}
