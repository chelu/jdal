/*
 * Copyright 2008-2015 Jose Luis Martin.
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.AbstractListModel;

/**
 * A ListModel that uses a List as Container
 * 
 * @author Jose Luis Martin.
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
	
	@Override
	public T getElementAt(int index) {
		return this.list.get(index);
	}

	@Override
	public int getSize() {
		return this.list.size();
	}

	/**
	 * @return the list
	 */
	public List<T> getList() {
		return this.list;
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
		this.list.removeAll(c);
		fireContentsChanged(this, -1, -1);
	}

	/**
	 * @param c collection with objets to add
	 */
	public void addAll(Collection<T> c) {
		list.addAll(c);
		fireContentsChanged(this, -1, -1);
	}

	/**
	 * Clear the list model.
	 */
	public void clear() {
		list.clear();
		fireContentsChanged(this, -1, -1);
		
	}
	
	/**
	 * Add item to list model.
	 * @param item item to add.
	 */
	public void add(T item) {
		list.add(item);
		fireContentsChanged(this, getSize() - 1, getSize());
	}
	
	/**
	 * Sort list elements using supplied Comparator
	 * @param comparator comparator to use.
	 */
	public void sort(Comparator<T> comparator) {
		Collections.sort(this.list, comparator);
		fireContentsChanged(this, -1, -1);
	}
}
