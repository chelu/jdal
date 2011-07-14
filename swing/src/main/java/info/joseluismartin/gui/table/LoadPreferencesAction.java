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
package info.joseluismartin.gui.table;

import info.joseluismartin.gui.PageableTable;
import info.joseluismartin.gui.form.FormUtils;

import java.awt.event.ActionEvent;

import javax.swing.Icon;

/**
 * Action for load user preferences in PageableTable menu
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 * @since 1.1
 */
public class LoadPreferencesAction extends PageableTableAction {
	
	public static final String DEFUALT_LOAD_PREFERENCES_ICON =  "/images/16x16/fileimport.png";
	
	public LoadPreferencesAction() {
		
	}

	/**
	 * @param pageableTable
	 */
	public LoadPreferencesAction(PageableTable pageableTable) {
		this(pageableTable, null, null);
	}

	
	/**
	 * @param pageableTable
	 * @param name
	 * @param icon
	 */
	public LoadPreferencesAction(PageableTable pageableTable, String name, Icon icon) {
		super(pageableTable, name, icon);
		init();
	}


	/**
	 * @param pageableTable
	 * @param name
	 */
	public LoadPreferencesAction(PageableTable pageableTable, String name) {
		this(pageableTable, name, null);
	}


	public void init() {
		setIcon(FormUtils.getIcon(getIcon(), DEFUALT_LOAD_PREFERENCES_ICON));
	}
	
 	/**
	 * {@inheritDoc}
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		getTable().restoreState();
		
	}

}
