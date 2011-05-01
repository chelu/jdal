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
package info.joseluismartin.dao;


import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *  A Page of Objects as result of query on PaginableDataProvider
 *  Holds results for a page and info of Page definition page size and order.
 *  
 *  @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
@SuppressWarnings("unchecked")
public class Page<T> implements Cloneable {

	private static final Log log = LogFactory.getLog(Page.class);
	public static enum Order { ASC, DESC };
	/** List of results */
	private List<T> data = new ArrayList<T>();
	/** Result count, Not the page size, the count of all result if not paginated */
	private int count;
	/** the page size */
	private int pageSize;
	/** sort by this property name */
	private String sortName;
	/** sort order */
	private Order order;
	/** a Object used as filter */
	private Object filter;
	/** start index of the page */
	private int startIndex;

	public Page(int pageSize, int startIndex, String sortName, Order order) {
	
		this.pageSize = pageSize;
		this.startIndex = startIndex;
		this.sortName = sortName;
		this.order = order;
	}
	
	public Page(int pageSize, int startIndex, String sortName) {
		this(pageSize, startIndex, sortName, Order.ASC);
		
	}
	
	public Page(int pageSize, int startIndex) {
		this(pageSize, startIndex, null);
	}
	
	public Page(int pageSize) {
		this (pageSize, 0);
	}
	
	public Page() {
		this (10);
	}
	
	/**
	 * @return list of data objects
	 */
	public List<T> getData() {
		return data;
	}
	
	/**
	 * @return the count
	 */
	public int getCount() {
		return count;
	}

	/**
	 * @param count the count to set
	 */
	public void setCount(int count) {
		this.count = count;
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

	/**
	 * @return the sortName
	 */
	public String getSortName() {
		return sortName;
	}

	/**
	 * @param sortName the sortName to set
	 */
	public void setSortName(String sortName) {
		this.sortName = sortName;
	}

	/**
	 * @return the order
	 */
	public Order getOrder() {
		return order;
	}

	/**
	 * @param order the order to set
	 */
	public void setOrder(Order order) {
		this.order = order;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(List<T> data) {
		this.data = data;
	}
	
	public void setOrderAsc() {
		setOrder(Order.ASC);
	}
	
	public void setOderDesc() {
		setOrder(Order.DESC);
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
	
	public Page<T> clone() {
		try {
			return (Page<T>) super.clone();
		} catch (CloneNotSupportedException e) {
			log.error(e);
			return null;
		}
	}
}
