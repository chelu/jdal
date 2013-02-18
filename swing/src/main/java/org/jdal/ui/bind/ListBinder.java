/*
 * Copyright 2009-2011 Jose Lus Martin.
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
package org.jdal.ui.bind;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JList;
import javax.swing.ListModel;

import org.jdal.ui.list.ListListModel;

/**
 * Binder for JList
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class ListBinder extends AbstractBinder {

	/**
	 * {@inheritDoc}
	 */
	public void doRefresh() {
		Collection<?> value = (Collection<?>) getValue();
		if (value != null)
			((JList) component).setModel(new ListListModel(new ArrayList<Object>(value))); 
		
	}

	/**
	 * {@inheritDoc}
	 */
	public void doUpdate() {
		List <Object> l = new ArrayList<Object>();
		ListModel lm = ((JList) component).getModel();
		
		for (int i = 0; i < lm.getSize(); i++)
			l.add(lm.getElementAt(i));
		
		setValue(l);
	}
}
