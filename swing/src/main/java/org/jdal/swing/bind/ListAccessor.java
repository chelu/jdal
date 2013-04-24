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
package org.jdal.swing.bind;


import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import org.jdal.swing.list.ListListModel;
import org.jdal.ui.bind.AbstractControlAccessor;


/**
 * ControlAccessor for JList
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 * @since 1.1
 * @see org.jdal.ui.bind.ControlAccessor
 */
public class ListAccessor extends AbstractControlAccessor implements ListDataListener,
	PropertyChangeListener {

	public ListAccessor(Object list) {
		super(list);
		getControl().getModel().addListDataListener(this);
		getControl().addPropertyChangeListener(this);
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
		ListListModel listModel = null;
		
		if (value instanceof Collection<?>) {
			listModel = new ListListModel(new ArrayList<Object>((Collection<?>) value));
		}
		else if (value instanceof Object[]) {
			listModel = new ListListModel(Arrays.asList((Object[])value));
		}
		
		if (listModel != null) {
			listModel.addListDataListener(this);
			getControl().setModel(listModel);
		}
	}
	
	public JList getControl() {
		return (JList) super.getControl();
	}

	/**
	 * {@inheritDoc}
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		if ("model".equals(evt.getPropertyName()))
			fireControlChange();
	}

}
