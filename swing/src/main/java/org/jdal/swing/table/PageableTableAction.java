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
package org.jdal.swing.table;


import java.awt.event.ActionEvent;

import javax.swing.Icon;

import org.jdal.swing.PageableTable;
import org.jdal.swing.action.BeanAction;

/**
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 *
 */
public abstract class PageableTableAction extends BeanAction {
	
	private PageableTable<?> table;
	
	public PageableTableAction() {
		
	}

	/**
	 * @param pageableTable
	 */
	public PageableTableAction(PageableTable<?> pageableTable) {
		this(pageableTable, null, null);
	}
	
	public PageableTableAction(PageableTable<?> pageableTable, String name) {
		this(pageableTable, name, null);
		
	}
	
	public PageableTableAction(PageableTable<?> pageableTable, String name, Icon icon) {
		this.table = pageableTable;
		setName(name);
		setIcon(icon);
	}

	/**
	 * @return the table
	 */
	public PageableTable<?> getTable() {
		return table;
	}

	/**
	 * @param table the table to set
	 */
	public void setTable(PageableTable<?> table) {
		this.table = table;
	}

	/**
	 * {@inheritDoc}
	 */
	abstract public void actionPerformed(ActionEvent e);	
	

}
