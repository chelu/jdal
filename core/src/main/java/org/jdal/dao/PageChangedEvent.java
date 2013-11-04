/*
 * Copyright 2002-2010 the original author or authors.
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
package org.jdal.dao;

import java.util.EventObject;

/**
 * Event to notify Paginator Listeners that the current page change.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class PageChangedEvent extends EventObject {

	private static final long serialVersionUID = 1L;
	/** the new page index */
	private int page;
	/** the new startIndex */
	private int startIndex;
	/** the new number of total pages */	
	private int totalPages;
	/** the new page size */
	private int pageSize;

	/**
	 * @param source
	 */
	public PageChangedEvent(Object source, int page, int startIndex, int totalPages, int pageSize) {
		super(source);
		this.page = page;
		this.startIndex = startIndex;
		this.totalPages = totalPages;
		this.pageSize = pageSize;
	}

	/**
	 * @return the page
	 */
	public int getPage() {
		return page;
	}

	/**
	 * @param page the page to set
	 */
	public void setPage(int page) {
		this.page = page;
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
	 * @return the totalPages
	 */
	public int getTotalPages() {
		return totalPages;
	}

	/**
	 * @param totalPages the totalPages to set
	 */
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	/**
	 * @return the pageSize
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * @param pageSize the pageSize to set
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
	

}
