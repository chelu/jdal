/*
 * Copyright 2009-2012 the original author or authors.
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * List to PageableDataSource Adapter.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class ListPageableDataSource<T> implements PageableDataSource<T> {
	
	private List<T> list = new ArrayList<T>();
	
	public ListPageableDataSource() {

	}

	/**
	 * @param c Collection to intialize the List
	 */
	public ListPageableDataSource(Collection<T> c) {
		if (c != null)
			this.list.addAll(c);
	}

	/**
	 * {@inheritDoc}
	 */
	public Page<T> getPage(Page<T> page) {
		if (list.isEmpty()) {
			page.setCount(0);
			page.setData(list);
			return page;
		}
		
		int startIndex = page.getStartIndex() < list.size() ? page.getStartIndex() : list.size() - 1;
		int toIndex = (page.getStartIndex() + page.getPageSize());
		toIndex =  toIndex < list.size() ?  toIndex : list.size() - 1;
		page.setData(list.subList(startIndex, toIndex));
		page.setCount(list.size());
		
		return page;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Serializable> getKeys(Page<T> page) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @return the list
	 */
	public List<T> getList() {
		return list;
	}

	/**
	 * @param list the list to set
	 */
	public void setList(List<T> list) {
		this.list = list;
	}
	

}
