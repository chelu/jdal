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
import java.util.List;

import javax.swing.MutableComboBoxModel;

import org.apache.commons.lang.ObjectUtils;

/**
 * A {@link MutableComboBoxModel} that use a List as Container
 * 
 * @author Jose Luis Martin
 */
public class ListComboBoxModel<T> extends ListListModel<T> implements MutableComboBoxModel<T> {

	private static final long serialVersionUID = 1L;
	private Object selectedItem;
	private boolean allowNullSelection = true;
	
	public ListComboBoxModel() {
		this(new ArrayList<T>());
	}
	
	public ListComboBoxModel(List<T> list) {
		this(list, true);
	}

	public ListComboBoxModel(List<T> list, boolean allowNullSelection) {
		super(list);
		if (list != null && list.size() > 0)
			setSelectedItem(list.get(0));
		this.allowNullSelection = allowNullSelection;
		
	}
	
	@Override
	public void addElement(T element) {
		getList().add(element);
		int index = getList().indexOf(element);
		fireIntervalAdded(this, index, index);
		
	}

	@Override
	public void insertElementAt(T element, int index) {
		getList().add(index, element);
		fireIntervalAdded(this, index, index);
		
	}

	@Override
	public void removeElement(Object obj) {
		int index = getList().indexOf(obj);
		if (getList().remove(obj)) {
			fireIntervalRemoved(this, index, index);
		}
		if (ObjectUtils.equals(selectedItem, obj)) {
			selectedItem = getList().size() > 0 ? getList().get(0) : null;
		}
	}

	@Override
	public void removeElementAt(int index) {
		getList().remove(index);
		
	}
	
	@Override
	public Object getSelectedItem() {
		return selectedItem;
	}

	@Override
	public void setSelectedItem(Object item) {
		if (item == null) {
			selectNullItem();
			fireContentsChanged(this, -1, -1);
		}
		else if (!item.equals(selectedItem)) {
			int index = getList().indexOf(item);
			if (index != -1) 
				selectedItem = item;
			else
				selectNullItem();
			
				fireContentsChanged(this, index, index);
		}
	}

	private void selectNullItem() {
		if (allowNullSelection)
			selectedItem = null;
		else {
			if (getList().size() > 0)
				setSelectedItem(getList().get(0));
		}
	}
}
