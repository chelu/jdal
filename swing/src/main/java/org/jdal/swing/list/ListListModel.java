/*
 * Copyright 2008-2014 the original author or authors.
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
package org.jdal.swing.list;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.AbstractListModel;

/**
 * A ListModel that uses a List as Container
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class ListListModel<T> extends AbstractListModel<T> {

	private static final long serialVersionUID = 1L;
	private List<T> list;
	
	public ListListModel() {
		this(new ArrayList<T>());
	}
	
	public ListListModel(List<T> list) {
		if (list != null)
			this.list = list;
	}
	
	public T getElementAt(int index) {
		return list.get(index);
	}

	public int getSize() {
		return list.size();
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
		fireContentsChanged(this, -1, -1);
	}

	/**
	 * @param c collection with objects to remove
	 */
	public void removeAll(Collection<T> c) {
		list.removeAll(c);
		fireContentsChanged(this, -1, -1);
		
	}

	/**
	 * @param c collection with objets to add
	 */
	public void addAll(Collection<T> c) {
		list.addAll(c);
		fireContentsChanged(this, -1, -1);
	}

	public void clear() {
		list.clear();
		fireContentsChanged(this, -1, -1);
		
	}
	
	public void add(T item) {
		list.add(item);
		fireContentsChanged(this, getSize() - 1, getSize());
	}
}
