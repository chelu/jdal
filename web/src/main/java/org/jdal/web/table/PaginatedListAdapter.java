/*
 * Copyright 2009-2011 the original author or authors.
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
package org.jdal.web.table;

import info.joseluismartin.dao.Page;

import java.util.List;

import org.displaytag.pagination.PaginatedList;
import org.displaytag.properties.SortOrderEnum;

/**
 * Page to displaytag PaginatedList adapter
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
@SuppressWarnings("rawtypes")
public class PaginatedListAdapter implements PaginatedList {
	
	private Page<?> model;
	
	
	public PaginatedListAdapter() {
		this(new Page(10));
	}
	
	public PaginatedListAdapter(Page<?> model) {
		this.model = model;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getFullListSize() {
		return model.getCount();
	}

	/**
	 * {@inheritDoc}
	 */
	public List getList() {
		return model.getData();
	}

	/**
	 * {@inheritDoc}
	 */
	public int getObjectsPerPage() {
		return model.getPageSize();
	}

	/**
	 * {@inheritDoc}
	 */
	public int getPageNumber() {
		return model.getPage();
	}

	/**
	 * {@inheritDoc}
	 */
	public String getSearchId() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getSortCriterion() {
		return model.getSortName();
	}

	/**
	 * {@inheritDoc}
	 */
	public SortOrderEnum getSortDirection() {
		return model.getOrder() == Page.Order.ASC ? SortOrderEnum.ASCENDING : SortOrderEnum.DESCENDING;
	}

	/**
	 * @return the model
	 */
	public Page<?> getModel() {
		return model;
	}

	/**
	 * @param model the model to set
	 */
	public void setModel(Page<?> model) {
		this.model = model;
	}
	
	public void setPage(int page) {
		model.setPage(page);
	}

	public void setSort(String sort) {
		model.setSortName(sort);
	}
	
	public void setDir(String dir) {
		model.setOrder("asc".equalsIgnoreCase(dir) ? Page.Order.ASC : Page.Order.DESC);
	}
	
}
