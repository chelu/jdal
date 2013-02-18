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
package org.jdal.ui.action;


import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTable;

import org.jdal.ui.ListTableModel;
import org.jdal.ui.TableRowAction;

/**
 * Listen clicks on table with TableModelListener and 
 * execute RowActions on actions columns.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class TableListener extends MouseAdapter {

	private JTable table;
	
	public TableListener(JTable table) {
		this.table = table;
		table.addMouseListener(this);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		Point point = e.getPoint();
		int row = table.rowAtPoint(point);
		int col = table.columnAtPoint(point);
		ListTableModel tableModel = (ListTableModel) table.getModel();
		
		// check Actions
		if (col != -1 && row != -1 && tableModel.isActionColumn(col)) {
			TableRowAction action = (TableRowAction) tableModel.getValueAt(row, col);
			action.setRow(tableModel.getList().get(row));
			action.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "clicked"));
			
		}
	}
}