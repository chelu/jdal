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
package info.joseluismartin.gui.bind;

import info.joseluismartin.gui.list.ListListModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 *
 */
public class ListAccessor extends AbstractControlAccessor implements ListDataListener {

	public ListAccessor(Object list) {
		super(list);
		getControl().getModel().addListDataListener(this);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void contentsChanged(ListDataEvent e) {
		fireControlChange();
	}

	/**
	 * {@inheritDoc}
	 */
	public void intervalAdded(ListDataEvent e) {
		fireControlChange();
	}

	/**
	 * {@inheritDoc}
	 */
	public void intervalRemoved(ListDataEvent e) {
		fireControlChange();
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection<?> getControlValue() {
		List <Object> l = new ArrayList<Object>();
		ListModel lm = getControl().getModel();
	
		for (int i = 0; i < lm.getSize(); i++)
			l.add(lm.getElementAt(i));
		
		return l;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setControlValue(Object value) {
		if (value instanceof Collection<?>) {
			ListListModel listModel = new ListListModel(new ArrayList<Object>((Collection<?>) value));
			listModel.addListDataListener(this);
			getControl().setModel(listModel);
		}
	}
	
	public JList getControl() {
		return (JList) super.getControl();
	}

}
