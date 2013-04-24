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
package org.jdal.swing.table;


import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jdal.swing.TableEditor;
import org.jdal.swing.form.FormUtils;

public class TableEditorFrame extends JFrame implements ListSelectionListener {

	private static final long serialVersionUID = 1L;
	public static final String DEFAULT_TABLE_ICON = "/images/table/table.png";
	private TableEditor<?>[] editors;
	private JList list = new JList();
	private Icon tableIcon;
	private JPanel editorPanel = new JPanel(new BorderLayout());
	private JSplitPane split;
	
	public void init() {
		
		tableIcon = FormUtils.getIcon(tableIcon, DEFAULT_TABLE_ICON);
		for (TableEditor<?> editor : editors)
			editor.getPanel().setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
			
		list = new JList(editors);
		list.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setVisibleRowCount(-1);
		JScrollPane scroll = new JScrollPane(list);
		split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scroll, editorPanel);
		getContentPane().add(split);
		list.addListSelectionListener(this);
		list.setCellRenderer(new ListCellRenderer());
		list.setSelectedIndex(0);
		setSize(800, 600);
	}
	
	
	public void valueChanged(ListSelectionEvent e) {
		TableEditor<?> editor = (TableEditor<?>) list.getSelectedValue();
		// int location = split.getLastDividerLocation();
		editorPanel.removeAll();
		editorPanel.add(editor.getPanel());
		split.validate();
		// split.setDividerLocation(location);
	}

	/**
	 * @return the editors
	 */
	public TableEditor<?>[] getEditors() {
		return editors;
	}


	/**
	 * @param editors the editors to set
	 */
	public void setEditors(TableEditor<?>[] editors) {
		this.editors = editors;
	}


	/**
	 * @return the tableIcon
	 */
	public Icon getTableIcon() {
		return tableIcon;
	}

	/**
	 * @param tableIcon the tableIcon to set
	 */
	public void setTableIcon(Icon tableIcon) {
		this.tableIcon = tableIcon;
	}
	
	class ListCellRenderer extends DefaultListCellRenderer {
		
		private static final long serialVersionUID = 1L;

		@Override
		public void setText(String text) {
			super.setText(text);
			super.setIcon(tableIcon);
		}
	}
}
