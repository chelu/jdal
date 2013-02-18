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
package org.jdal.ui.bind;


import java.util.List;

import org.jdal.ui.EditorEvent;
import org.jdal.ui.EditorListener;
import org.jdal.ui.table.TablePanel;

/**
 * Control Accessor for TablePanel, usually used with CollectionPersistentService to edit Lists
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class TablePanelAccessor extends AbstractControlAccessor implements EditorListener {
	
	/**
	 * @param control
	 */
	public TablePanelAccessor(Object control) {
		super(control);
		getControl().addEditorListener(this);
	}

	/**
	 * {@inheritDoc}
	 */
	public Object getControlValue() {
		TablePanel<Object> table =  getControl();
		return table.getPersistentService().getAll();
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public void setControlValue(Object value) {
		List<Object> list = (List<Object>) value;
		getControl().getPersistentService().delete(getControl().getPersistentService().getAll());
		getControl().getPersistentService().save(list);
	}

	/**
	 * {@inheritDoc}
	 */
	public void modelChanged(EditorEvent e) {
		fireControlChange();
	}
	
	@SuppressWarnings("unchecked")
	public TablePanel<Object> getControl() {
		return (TablePanel<Object>) super.getControl();
	}

}
