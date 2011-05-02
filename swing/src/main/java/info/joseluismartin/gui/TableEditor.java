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
package info.joseluismartin.gui;

import info.joseluismartin.gui.form.FormUtils;
import info.joseluismartin.service.PersistentService;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Simple table editor 
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class TableEditor<T> extends AbstractView<T> implements TableModelListener {
	
	public static String DEFAULT_ICON = "/images/table/table.png";
	public static String DEFAULT_ADD_ICON = "/images/table/22x22/edit-new.png";
	public static String DEFAULT_REMOVE_ICON = "/images/table/22x22/edit-delete.png";
	public static String DEFAULT_SAVE_ICON = "/images/table/22x22/save.png";
	
	private static final Log log = LogFactory.getLog(TableEditor.class);
	private JTable table;
	private ListTableModel tableModel;
	private Icon icon;
	private Icon addIcon;
	private Icon removeIcon;
	private Icon saveIcon;
	private List<T> dirty = new ArrayList<T>();
	private List<T> deleted = new ArrayList<T>();
	private Class<T> clazz;
	private String name;
	
	private PersistentService<T, Serializable> service;
	
	public TableEditor(Class<T> clazz) {
		this.clazz = clazz;
	}
	
	public void init() {
		loadIcons();
		refresh();
	}

	@Override
	protected JComponent buildPanel() {
		Box box = Box.createVerticalBox();
		// Container header = createHeader();
		Container tablePanel = createTablePanel(); 
		// box.add(header);
		box.add(tablePanel);
		
		return box;
	}

	/**
	 * @return
	 */
	protected Container createTablePanel() {
		table = new JTable(tableModel, tableModel.getTableColumnModel());
		table.setRowHeight(22);
		table.setAutoCreateRowSorter(true);
		tableModel.addTableModelListener(this);
		JScrollPane scroll = new JScrollPane(table);
		scroll.setAlignmentX(Container.LEFT_ALIGNMENT);
		Box box = Box.createVerticalBox();
		JButton addButton = new JButton(new AddAction());
		JButton deleteButton = new JButton(new DeleteAction());
		JButton saveButton = new JButton(new SaveAction());
		Box buttonBox = Box.createHorizontalBox();
		buttonBox.add(addButton);
		buttonBox.add(Box.createHorizontalStrut(5));
		buttonBox.add(deleteButton);
		buttonBox.add(Box.createHorizontalStrut(5));
		buttonBox.add(saveButton);
		buttonBox.setAlignmentX(Container.LEFT_ALIGNMENT);
		buttonBox.setMaximumSize(new Dimension(Short.MAX_VALUE, 25));
		box.add(buttonBox);
		box.add(Box.createVerticalStrut(5));
		box.add(scroll);
		return box;
	}

	/**
	 * 
	 */
	public void doRefresh() {
		tableModel.setList(service.getAll());
		dirty.clear();
		deleted.clear();
	}
	/**
	 * @return
	 */
	protected Container createHeader() {
		JLabel label = new JLabel(getName());
		label.setAlignmentX(Container.LEFT_ALIGNMENT);
		Box box = Box.createHorizontalBox();
		box.add(label);
		
		return box;
	}

	public void add() {
		T t = newType();
		tableModel.add(t);
		dirty.add(t);
	}
	
	@SuppressWarnings("unchecked")
	public void delete() {
		int[] rows = table.getSelectedRows();
		List<T> toDeleteList = new ArrayList<T>();
		for (int i : rows) {
			toDeleteList.add((T) tableModel.getList().get(i));
		}
		tableModel.getList().removeAll(toDeleteList);
		deleted.addAll(toDeleteList);
		tableModel.fireTableChanged();
	}
	
	/**
	 * @return
	 */
	private T newType() {
		try {
			return clazz.newInstance();
		} catch (Exception e) {
			log.error("Can't instantiate class: ", e);
		}
		return null;
	}

	public JTable getTable() {
		return table;
	}

	public void setTable(JTable table) {
		this.table = table;
	}

	public ListTableModel getTableModel() {
		return tableModel;
	}

	public void setTableModel(ListTableModel tableModel) {
		this.tableModel = tableModel;
	}

	public Icon getAddIcon() {
		return addIcon;
	}

	public void setAddIcon(Icon addIcon) {
		this.addIcon = addIcon;
	}


	public PersistentService<T, Serializable> getService() {
		return service;
	}

	public void setService(PersistentService<T, Serializable> service) {
		this.service = service;
	}
	
	
	
	private class SaveAction extends IconAction {
		
		private static final long serialVersionUID = 1L;

		public SaveAction() {
			setIcon(saveIcon);
		}

		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			service.save(dirty);
			service.delete(deleted);
			refresh();
		}
		
	}
	
	private class AddAction extends IconAction {

		private static final long serialVersionUID = 1L;

		public AddAction() {
			setIcon(addIcon);
		}

		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			add();
		}
		
	}
	
	private class DeleteAction extends IconAction {
		
		private static final long serialVersionUID = 1L;

		public DeleteAction() {
			setIcon(removeIcon);
		}

		public void actionPerformed(ActionEvent e) {
			delete();
		}
		
	}

	public Icon getSaveIcon() {
		return saveIcon;
	}

	public void setSaveIcon(Icon saveIcon) {
		this.saveIcon = saveIcon;
	}

	@SuppressWarnings("unchecked")
	public void tableChanged(TableModelEvent e) {
		if (e.getType() == TableModelEvent.UPDATE) {
			int row = e.getFirstRow();
			if (row >= 0) {
				dirty.add((T) tableModel.getList().get(row));
			}
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return getName();
	}

	/**
	 * @return the icon
	 */
	public Icon getIcon() {
		return icon;
	}

	/**
	 * @param icon the icon to set
	 */
	public void setIcon(Icon icon) {
		this.icon = icon;
	}
	
	protected void loadIcons() {
		icon = FormUtils.getIcon(icon, DEFAULT_ICON);
		addIcon = FormUtils.getIcon(addIcon, DEFAULT_ADD_ICON);
		saveIcon = FormUtils.getIcon(saveIcon, DEFAULT_SAVE_ICON);
		removeIcon = FormUtils.getIcon(removeIcon, DEFAULT_REMOVE_ICON);
	}

	/**
	 * @return the removeIcon
	 */
	public Icon getRemoveIcon() {
		return removeIcon;
	}

	/**
	 * @param removeIcon the removeIcon to set
	 */
	public void setRemoveIcon(Icon removeIcon) {
		this.removeIcon = removeIcon;
	}
}
