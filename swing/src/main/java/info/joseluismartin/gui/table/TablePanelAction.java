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
package info.joseluismartin.gui.table;

import info.joseluismartin.gui.action.BeanAction;

/**
 * Base class for TablePanel Actions
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
@SuppressWarnings("rawtypes")
public abstract class TablePanelAction extends BeanAction {

	private static final long serialVersionUID = 1L;

	private TablePanel tablePanel;
	
	public void init() {
		
	}
	
	/**
	 * @return the tablePanel
	 */
	public TablePanel getTablePanel() {
		return tablePanel;
	}
	/**
	 * @param tablePanel the tablePanel to set
	 */
	public void setTablePanel(TablePanel tablePanel) {
		this.tablePanel = tablePanel;
	}
	
	
}
