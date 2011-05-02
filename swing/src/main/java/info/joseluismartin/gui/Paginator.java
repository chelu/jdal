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
package info.joseluismartin.gui;

/**
 * A interface for row data paginators.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 * 
 */
public interface Paginator {

	/**
	 * @return true if there is records on next page
	 */

	boolean hasNext();

	/**
	 * @return true if there is records on previews page
	 */
	boolean hasPrevious();

	/**
	 * @return true if there is records on index page
	 */
	boolean hasPage(int indexPage);

	/**
	 * set the page number to index page if possible
	 * 
	 * @param indexPage
	 *            the new page number
	 */
	void setPage(int indexPage);

	/**
	 * Gets the current page number
	 * 
	 * @return the current page number
	 */
	int getPage();

	/**
	 * @return the number of total records
	 */
	int getToltalRecords();

	/**
	 * @return the number of pages
	 */
	int getTotalPages();

	/**
	 * Go to next page
	 */
	void nextPage();

	/** 
	 * Go to previous page
	 */
	void previousPage();

	/**
	 * Go to last page
	 */
	void lastPage();

	/** 
	 * Go to first page
	 */
	void firstPage();
	
	/**
	 * get the startIndex, ie the number of the first record of this page
	 * @return the start index
	 */
	int getStartIndex();

	/**
	 * set the number of total records, ie the number of records in all pages
	 * @param count the number of total records
	 */
	void setTotalRecords(int count);

	/**
	 * get the page size, ie the number of records in one page
	 * @return the page size
	 */
	int getPageSize();
	
	/**
	 * Set the page size, ie, the number of records in one page
	 * @param pageSize the page size
	 */
	void setPageSize(int pageSize);
	
	/**
	 * Add a PaginatorListner
	 * @param listener the PaginatorListener to add
	 */
	void addPaginatorListener(PaginatorListener listener);

	/**
	 * Remove a PaginatorListener
	 * @param listener the PaginatorListener to remove
	 */
	void removePaginatorListener(PaginatorListener listener);
	
	

}
