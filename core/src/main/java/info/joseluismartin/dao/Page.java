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
 *  A Page of Objects as result of query on PaginableDataSource
 *  Holds results for a page and info of Page definition page size and order.
 *  
 *  @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
@SuppressWarnings("unchecked")
public class Page<T> implements Paginator, Cloneable {

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
	/** page number  */
	private int page = 1;
	/** PageableDataSource that loads the page */
	private PageableDataSource<T> pageableDataSource;
	/** Paginator Listeners */
	private ArrayList<PaginatorListener> listeners = new ArrayList<PaginatorListener>();

	public Page(int pageSize, int page, String sortName, Order order) {
	
		this.pageSize = pageSize;
		this.page = page;
		this.sortName = sortName;
		this.order = order;
	}
	
	public Page(int pageSize, int page, String sortName) {
		this(pageSize, page, sortName, Order.ASC);
		
	}
	
	public Page(int pageSize, int page) {
		this(pageSize, page, null);
	}
	
	public Page(int pageSize) {
		this (pageSize, 1);
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

	
	public Page<T> clone() {
		try {
			return (Page<T>) super.clone();
		} catch (CloneNotSupportedException e) {
			log.error(e);
			return null;
		}
	}

	/**
	 * @return the pageableDataSource
	 */
	public PageableDataSource<T> getPageableDataSource() {
		return pageableDataSource;
	}

	/**
	 * {@inheritDoc}
	 * @see info.joseluismartin.gui.Paginator#hasNext()
	 */
	public boolean hasNext() {
		return page < getTotalPages();
	}

	/**
	 * {@inheritDoc}
	 * @see info.joseluismartin.gui.Paginator#hasPage(int)
	 */
	public boolean hasPage(int indexPage) {
		return indexPage <= getTotalPages() &&  indexPage > 0;
	}

	/**
	 * {@inheritDoc}
	 * @see info.joseluismartin.gui.Paginator#hasPreviews()
	 */
	public boolean hasPrevious() {
		return page > 1;
	}

	/**
	 *  {@inheritDoc}
	 * @see info.joseluismartin.gui.Paginator#setPage(int)
	 */
	public void setPage(int indexPage) {
		if (page > 0)  {
			page = indexPage;
			load();
			firePageChangedEvent();
		}
		else {
			log.warn("Try to set a page < 0");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public int getStartIndex() {
		return (page - 1)*pageSize;
	}

	/**
	 * {@inheritDoc}
	 * @see info.joseluismartin.gui.Paginator#getTotalPages()
	 */
	public int getTotalPages() {
		if (pageSize > 0)
			return (int) Math.ceil(count/pageSize) + (count % pageSize == 0 ? 0 : 1);
		
		return 1;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void addPaginatorListener(PaginatorListener listener) {
		if (!listeners.contains(listener))
			listeners.add(listener);
	}

	/**
	 * {@inheritDoc}
	 */
	public void removePaginatorListener(PaginatorListener listener) {
		listeners.remove(listener);
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
		if (pageSize > 0) {
			// need to recalculate current page
			page = (int) Math.ceil(getStartIndex()/pageSize) + 1;
			this.pageSize = pageSize;
			firePageChangedEvent();
		}
	}

	/**
	 * @return the page
	 */
	public int getPage() {
		return page;
	}

	/**
	 * {@inheritDoc}
	 * @see info.joseluismartin.gui.Paginator#firstPage()
	 */
	public void firstPage() {
		setPage(1);
		
	}

	/**
	 * {@inheritDoc}
	 * @see info.joseluismartin.gui.Paginator#lastPage()
	 */
	public void lastPage() {
		setPage(getTotalPages());
	}

	/**
	 * {@inheritDoc}
	 * @see info.joseluismartin.gui.Paginator#nextPage()
	 */
	public void nextPage() {
			setPage(page + 1);
		
	}

	/**
	 * {@inheritDoc}
	 * @see info.joseluismartin.gui.Paginator#previousPage()
	 */
	public void previousPage() {
			setPage(page - 1);
	}
	
	/**
	 * Notify Listener that current page changed
	 */
	private void firePageChangedEvent() {
		for (PaginatorListener listener : listeners) {
			listener.pageChanged(new PageChangedEvent(this, page, getStartIndex(), getTotalPages(), pageSize));
		}
	}

	
	public void load() {
		if (pageableDataSource != null)
			pageableDataSource.getPage(this);
	}

	/**
	 * @param pageableDataSource the pageableDataSource to set
	 */
	public void setPageableDataSource(PageableDataSource<T> pageableDataSource) {
		this.pageableDataSource = pageableDataSource;
	}
}
