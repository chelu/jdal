/*
 * Copyright 2002-2010 the original author or authors.
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
package info.joseluismartin.gui;

import java.awt.event.ActionEvent;

import javax.swing.table.TableModel;

/**
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 *
 */
public abstract class TableRowAction extends IconAction implements Cloneable {

	private static final long serialVersionUID = 1L;
	/** the row object */
	private Object row;
	/** the table model */
	private PageableTable<?> table;
	
	/** 
	 * {@inheritDoc}
	 */
	public abstract void actionPerformed(ActionEvent e);

	public  Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

	public Object getRow() {
		return row;
	}

	public void setRow(Object row) {
		this.row = row;
	}

	public TableModel getTableModel() {
		return table.getTableModel();
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
}
