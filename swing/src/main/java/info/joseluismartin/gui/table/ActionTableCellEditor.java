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

import info.joseluismartin.gui.TableRowAction;


import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

/**
 * TableCellEditor that execute Actions on clicks. If Action is a TableRowAction set
 * a TableModel and row on TableRowAction.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class ActionTableCellEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {

	private static final long serialVersionUID = 1L;
	private Action action;
	private JButton button;
	
	public ActionTableCellEditor() {
		button = new JButton();
		button.setBorderPainted(false);
	}
	
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {

		action = (Action) value;
		
		if (action instanceof TableRowAction) {
			((TableRowAction) action).setTableModel(table.getModel());
			((TableRowAction) action).setRow(row);
		}
	
		if (isSelected)
			button.setBackground(table.getSelectionBackground());
		
		return button;
		
	}

	public Object getCellEditorValue() {
		// TODO Auto-generated method stub
		return null;
	}

	public void actionPerformed(ActionEvent e) {
		action.actionPerformed(new ActionEvent(button, ActionEvent.ACTION_PERFORMED, "clicked"));
	}

}
