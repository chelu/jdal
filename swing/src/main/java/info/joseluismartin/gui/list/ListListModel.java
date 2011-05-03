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
package info.joseluismartin.gui.list;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.AbstractListModel;

/**
 * A ListModel that uses a List as Container
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
@SuppressWarnings("unchecked")
public class ListListModel extends AbstractListModel {

	private static final long serialVersionUID = 1L;
	private List list;
	
	public ListListModel() {
		this(new ArrayList());
	}
	
	public ListListModel(List<?> list) {
		this.list = list;
	}
	
	public Object getElementAt(int index) {
		return list.get(index);
	}

	public int getSize() {
		return list.size();
	}

	/**
	 * @return the list
	 */
	public List getList() {
		return list;
	}

	/**
	 * @param list the list to set
	 */
	public void setList(List list) {
		this.list = list;
		fireContentsChanged(this, -1, -1);
	}

	/**
	 * @param asList
	 */
	public void removeAll(Collection c) {
		list.removeAll(c);
		fireContentsChanged(this, -1, -1);
		
	}

	/**
	 * @param asList
	 */
	public void addAll(Collection c) {
		list.addAll(c);
		fireContentsChanged(this, -1, -1);
	}

	public void clear() {
		list.clear();
		fireContentsChanged(this, -1, -1);
	}
}
