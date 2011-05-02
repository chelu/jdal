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
import java.util.List;

import javax.swing.MutableComboBoxModel;

import org.apache.commons.lang.ObjectUtils;


/**
 * A ComboBoxModel that use a List as Container
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
@SuppressWarnings("unchecked")
public class ListComboBoxModel extends ListListModel implements MutableComboBoxModel {

	private static final long serialVersionUID = 1L;
	private Object selectedItem;
	
	public ListComboBoxModel() {
		this(new ArrayList());
	}
	
	public ListComboBoxModel(List list) {
		super(list);
		if (list != null && list.size() > 0)
			setSelectedItem(list.get(0));
	}

	
	public void addElement(Object obj) {
		getList().add(obj);
		int index = getList().indexOf(obj);
		fireIntervalAdded(this, index, index);
		
	}

	public void insertElementAt(Object obj, int index) {
		getList().add(index, obj);
		fireIntervalAdded(this, index, index);
		
	}

	public void removeElement(Object obj) {
		int index = getList().indexOf(obj);
		if (getList().remove(obj)) {
			fireIntervalRemoved(this, index, index);
		}
		if (ObjectUtils.equals(selectedItem, obj)) {
			selectedItem = getList().size() > 0 ? getList().get(0) : null;
		}
	}

	public void removeElementAt(int index) {
		getList().remove(index);
		
	}

	public Object getSelectedItem() {
		return selectedItem;
	}

	public void setSelectedItem(Object anItem) {
		this.selectedItem = anItem;
	}

}
