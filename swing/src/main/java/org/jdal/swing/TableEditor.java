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
package org.jdal.swing;


import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdal.beans.MessageSourceWrapper;
import org.jdal.service.PersistentService;
import org.jdal.swing.action.BeanAction;
import org.jdal.swing.form.FormUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;

/**
 * Simple table editor 
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class TableEditor<T> extends AbstractView<T> implements TableModelListener {
	
	// Default Icons
	// FIXME: Move to Tango Desktop Icons
	public static String DEFAULT_ICON = "/images/table/table.png";
	public static String DEFAULT_ADD_ICON = "/images/table/22x22/edit-new.png";
	public static String DEFAULT_REMOVE_ICON = "/images/table/22x22/edit-delete.png";
	public static String DEFAULT_SAVE_ICON = "/images/table/22x22/save.png";
	public static String DEFAULT_REFRESH_ICON = "/images/table/22x22/reload.png";
	/** log */
	private static final Log log = LogFactory.getLog(TableEditor.class);
	/** JTable to show database rows */
	private JTable table;
	/** Use ListTableModel as table TableModel */
	private ListTableModel tableModel;
	private Icon icon;
	private Icon addIcon;
	private Icon removeIcon;
	private Icon saveIcon;
	private Icon refreshIcon;
	/** Hold dirty rows */
	private Set<T> dirty = new HashSet<T>();
	/** Entity type */
	private Class<T> clazz;
	private String name;
	
	/** Persistent Service to use */
	private PersistentService<T, Serializable> service;
	
	/** MessageSource */
	private MessageSourceWrapper messageSource = new MessageSourceWrapper();
	/** 
	 * Creates new TableEditor 
	 */
	public TableEditor() {
		
	}
	
	/**
	 * Creates new TableEditor
	 * @param clazz entity class
	 */
	public TableEditor(Class<T> clazz) {
		this.clazz = clazz;
	}
	
	/**
	 * Initiaze, usally called by container
	 */
	public void init() {
		loadIcons();
		
		if (tableModel.getModelClass() == null)
			tableModel.setModelClass(this.clazz);
		
		refresh();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected JComponent buildPanel() {
		Box box = Box.createVerticalBox();
		Container tablePanel = createTablePanel(); 
		box.add(tablePanel);
		box.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		return box;
	}

	/**
	 * Creates a new Box with table and actions buttons
	 * @return a new Box
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
		JButton refreshButton = new JButton(new RefreshAction());
		Box buttonBox = Box.createHorizontalBox();
		buttonBox.add(addButton);
		buttonBox.add(Box.createHorizontalStrut(5));
		buttonBox.add(deleteButton);
		buttonBox.add(Box.createHorizontalStrut(5));
		buttonBox.add(saveButton);
		buttonBox.add(Box.createHorizontalStrut(5));
		buttonBox.add(refreshButton);
		buttonBox.setAlignmentX(Container.LEFT_ALIGNMENT);
		buttonBox.setMaximumSize(new Dimension(Short.MAX_VALUE, 25));
		box.add(buttonBox);
		box.add(Box.createVerticalStrut(5));
		box.add(scroll);
		return box;
	}

	/**
	 * {@inheritDoc}
	 */
	public void doRefresh() {
		tableModel.setList(service.getAll());
	}
	
	/**
	 * @return header
	 */
	protected Container createHeader() {
		JLabel label = new JLabel(getName());
		label.setAlignmentX(Container.LEFT_ALIGNMENT);
		Box box = Box.createHorizontalBox();
		box.add(label);
		
		return box;
	}

	/**
	 * Add a new model to the table
	 */
	public void add() {
		T t = newType();
		tableModel.add(t);
		dirty.add(t);
	}
	
	/**
	 * Delete selected table rows using persistent service
	 */
	@SuppressWarnings("unchecked")
	public void delete() {
		int[] rows = table.getSelectedRows();
		for (int i : rows) {
			T model = (T) tableModel.getList().get(table.convertRowIndexToModel(i));
			try  {
				service.delete(model);
				tableModel.getList().remove(model);
			} catch (DataAccessException dae) {
				String errorMsg = messageSource.getMessage("TableEditor.objectInUse", 
						new Object[] {model.toString()}, Locale.getDefault());
				JOptionPane.showMessageDialog(getPanel(), errorMsg, "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
		tableModel.fireTableChanged();
	}
	
	/**
	 * Return a new instance of configured entity class
	 * @return new entity 
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
			try {
				service.save(dirty);
			} catch (DataAccessException dae) {
				String errorMsg = messageSource.getMessage("TableEditor.saveError", null, Locale.getDefault());
				JOptionPane.showMessageDialog(getPanel(), errorMsg, "Error", JOptionPane.ERROR_MESSAGE);
			}
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
	
	private class RefreshAction extends BeanAction {
		private static final long serialVersionUID = 1L;

		public RefreshAction() {
			setIcon(refreshIcon);
		}

		public void actionPerformed(ActionEvent e) {
			dirty.clear();
			refresh();
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
		refreshIcon = FormUtils.getIcon(refreshIcon, DEFAULT_REFRESH_ICON);
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

	/**
	 * @return the refreshIcon
	 */
	public Icon getRefreshIcon() {
		return refreshIcon;
	}

	/**
	 * @param refreshIcon the refreshIcon to set
	 */
	public void setRefreshIcon(Icon refreshIcon) {
		this.refreshIcon = refreshIcon;
	}

	/**
	 * @return the messageSource
	 */
	public MessageSource getMessageSource() {
		return messageSource.getMessageSource();
	}

	/**
	 * @param messageSource the messageSource to set
	 */
	@Autowired
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource.setMessageSource(messageSource);
	}
}
