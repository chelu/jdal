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
package org.jdal.swing;


import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.Action;
import javax.swing.event.EventListenerList;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdal.beans.MessageSourceWrapper;
import org.jdal.beans.PropertyUtils;
import org.jdal.swing.table.AnnotationFormatTableCellRenderer;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.util.ClassUtils;

/**
 * TableModel that use a List of Objects to hold data.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 * @see org.jdal.swing.PageableTable
 * @since 1.0
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class ListTableModel implements TableModel {

	public static final String MAX_WIDTH = "maxWidth";
	public static final String CELL_RENDERER = "cellRenderer";
	private static final String CELL_EDITOR = "cellEditor";

	/**  log */
	private final static Log log = LogFactory.getLog(ListTableModel.class);

	/** List holder for models */
	private List list;
	/** TableModel listeners */
	private ArrayList<TableModelListener> listeners = new ArrayList<TableModelListener>();
	/** columnConunt */
	private int columnCount = 0;
	/** Property descriptor array of model */
	private List<PropertyDescriptor> pds = new ArrayList<PropertyDescriptor>();
	/** columnNames */
	private List<String> columnNames = new ArrayList<String>();
	/** display names */
	private List<String> displayNames = new ArrayList<String>();
	/** if true, use instrospection to get property and display names */
	private boolean usingIntrospection = false;
	/** true if use checkbox to select or unselect rows */
	private boolean usingChecks = true;
	/** true if use actions at final of rows */
	private boolean usingActions = false;
	/** action list for table rows */
	private List<Action> actions = new ArrayList<Action>();
	/** List of checks values */
	private List<Boolean> checks = new ArrayList<Boolean>();
	/** Editable Map holds editable state by property name*/
	private Map<String, Boolean> editableMap = new HashMap<String, Boolean>();
	/** hold check state by model key */
	private Set<Serializable> selectedRowSet = new HashSet<Serializable>();
	/** model id property for checkMap */
	private String id = "id";
	/** Model class */
	private Class modelClass;
	/** ColumnDefintion List */
	private List<ColumnDefinition> columns = new ArrayList<ColumnDefinition>();
	/** Default TableCellRenderer */
	private TableCellRenderer defaultTableCellRenderer = new AnnotationFormatTableCellRenderer();
	/** MessageSource */
	private MessageSourceWrapper messageSource = new MessageSourceWrapper();

	/**
	 * Creates a new ListTableModel with model set to List l
	 * @param l the list to set as model
	 */
	public ListTableModel(List l) {
		setList(l);
	}

	/**
	 * Creates a new ListTableModel with a empty list model
	 */
	public ListTableModel() {
		setList(new ArrayList<Object>());
	}

	/** 
	 * Get the column name of an index
	 * @return String with column name
	 */
	public String getColumnName(int columnIndex) {
		String name = "";
		if (isPropertyColumn(columnIndex)) {
			name  = messageSource.getMessage(displayNames.get(columnToPropertyIndex(columnIndex)));
		}
		return name;
	}

	/**
	 * {@inheritDoc}
	 */
	public Class<?> getColumnClass(int columnIndex) {
		Class clazz = Object.class;

		if (isCheckColum(columnIndex)) {
			clazz = Boolean.class;
		} else if (isPropertyColumn(columnIndex)) {
			if (pds.size() > 0) {
				clazz = pds.get(columnToPropertyIndex(columnIndex)).getPropertyType();
			}
		} else if (isActionColumn(columnIndex)) {
			clazz = actions.get(columntoToActionIndex(columnIndex)).getClass();
		}
		
		// JTable hangs if we return a primitive type here
		return ClassUtils.resolvePrimitiveIfNecessary(clazz);
	}

	private int columntoToActionIndex(int columnIndex) {
		return columnIndex - pds.size() - (usingChecks ? 1 : 0);
	}

	/**
	 * {@inheritDoc}
	 */
	public int getRowCount() {
		return list.size();
	}

	/**
	 * {@inheritDoc}
	 */
	public int getColumnCount() {
		return columnCount;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return isCheckColum(columnIndex)
				|| isActionColumn(columnIndex)
				|| (isPropertyColumn(columnIndex) && Boolean.TRUE.equals(editableMap.get(getPropertyName(columnIndex))));

		//		editable[columnToPropertyIndex(columnIndex)]); 
	}

	/**
	 * {@inheritDoc}
	 */
	public Object getValueAt(int rowIndex, int columnIndex) {
		// return check
		if (usingChecks) {
			if (columnIndex == 0) {
				fillChecksIfNecesary(rowIndex);
				return checks.get(rowIndex);
			} else {
				columnIndex--;
			}
		}
		// return property name
		if (columnIndex < pds.size()) {
			return getCellValue(rowIndex, columnIndex);
		}
		// return Action
		if (usingActions) {
			return actions.get(columnIndex - pds.size());
		}

		return null;
	}

	/**
	 * @param rowIndex
	 */
	private void fillChecksIfNecesary(int rowIndex) {
		if (checks.size() >= rowIndex) {
			for (int i = checks.size() -1 ; i < rowIndex; i++) {
				checks.add(Boolean.FALSE);
			}
		}
		
	}

	private Object getCellValue(int rowIndex, int columnIndex) {
		BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(list.get(rowIndex));
		return bw.getPropertyValue(columnNames.get(columnIndex));
	}

	/**
	 * {@inheritDoc}
	 */
	public void setValueAt(Object value, int rowIndex, int columnIndex) {
		if (isCheckColum(columnIndex)) {
			checks.set(rowIndex, (Boolean) value);
			// sync selectedRowSet
			Object row = list.get(rowIndex);

			if (Boolean.TRUE.equals(value))
				selectedRowSet.add((Serializable) getPrimaryKey(row));
			else
				selectedRowSet.remove(getPrimaryKey(row));

		}
		else if (isPropertyColumn(columnIndex)) {
			int index = columnToPropertyIndex(columnIndex);
			BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(list.get(rowIndex));
			bw.setPropertyValue(columnNames.get(index), value);
			fireTableCellUpdated(rowIndex, columnIndex);
		}
	}

	/**
	 * Notifies all listeners that the value of the cell at 
	 * <code>[row, column]</code> has been updated.
	 *
	 * @param row  row of cell which has been updated
	 * @param column  column of cell which has been updated
	 * @see TableModelEvent
	 * @see EventListenerList
	 */
	public void fireTableCellUpdated(int row, int column) {
		fireTableChanged(new TableModelEvent(this, row, row, column));
	}

	/**
	 * {@inheritDoc}
	 */
	public void addTableModelListener(TableModelListener listener) {
		if (!listeners.contains(listener))
			listeners.add(listener);
	}

	/**
	 * {@inheritDoc}
	 */
	public void removeTableModelListener(TableModelListener l) {
		listeners.remove(l);
	}

	/** 
	 * Initialize table. Load propertyDescriptors based on columNames or 
	 * model introspection. 
	 */
	// FIXME: PropertyDescriptors  are now unused, review to remove.
	public void init() {
		if (modelClass == null) {
			log.warn("Cannot initilize without  modelClass, set a list of models o specify a model class");
			return;
		}

		columnCount = 0;

		if (usingIntrospection) {
			pds = Arrays.asList(BeanUtils.getPropertyDescriptors(modelClass));
			Collections.reverse(pds);
			columnNames = new ArrayList<String>(pds.size());
			displayNames = new ArrayList<String>(pds.size());

			for (PropertyDescriptor propertyDescriptor : pds) {
				columnNames.add(propertyDescriptor.getName());
				displayNames.add(propertyDescriptor.getDisplayName());
			}
		}

		else {
			pds = new ArrayList<PropertyDescriptor>(columnNames.size());
			for (String name : columnNames) {
				PropertyDescriptor pd = PropertyUtils.getPropertyDescriptor(modelClass, name);
				if (pd == null)
					throw new RuntimeException("Invalid property [" + name +"]" + 
							" for model class [" + modelClass.getName() + "]");
				pds.add(pd);
			}
		}

		columnCount += pds.size();

		if (usingChecks) {
			columnCount++;
			buildCheckArray();
		}

		if (usingActions) {
			columnCount += actions.size();
		}
	}

	private void buildCheckArray() {
		checks = new ArrayList<Boolean>(list.size());
		// fill checks list
		for (Object row : list) {
			// for now move from / to selectedObjectSet and old checks to
			// avoid refactor code
			checks.add(selectedRowSet.contains(getPrimaryKey(row)));
		}
	}

	/**
	 * Fire a TableModelChanged event
	 * @param e event to fire
	 */
	public void fireTableChanged(TableModelEvent e) {
		Iterator<TableModelListener> iter = listeners.iterator();
		while (iter.hasNext())
			((TableModelListener) iter.next()).tableChanged(e);
	}

	/**
	 * Create a TableColumnModel for JTable. 
	 * Try to use sizes and cell renderers from property descriptors.
	 * @return a new TableColumnModel based on PropertyDescriptors
	 */
	public TableColumnModel getTableColumnModel() {
		TableColumnModel tcm = new DefaultTableColumnModel();
		int baseIndex = 0;
		if (usingChecks) {
			TableColumn tableColumn = new TableColumn(0);
			tableColumn.setMaxWidth(50);
			tcm.addColumn(tableColumn);
			baseIndex++;
		}
		for (int i = 0; i < columnNames.size(); i++) {
			String name = this.columnNames.get(i);
			TableColumn tableColumn = new TableColumn(baseIndex + i);
			tableColumn.setHeaderValue(displayNames.get(i));

			if (pds != null && pds.size() > 0) {
				PropertyDescriptor descriptor = pds.get(i);
				// property values for TableColumns
				if (descriptor != null) {
					Integer maxWidth = getColumnWidth(name);
					if (maxWidth != null) {
						tableColumn.setMaxWidth(maxWidth.intValue());
						tableColumn.setPreferredWidth(maxWidth);
					}
					tableColumn.setCellRenderer(getColumnRenderer(name));
					tableColumn.setCellEditor(getColumnEditor(name));
				}
			}

			tcm.addColumn(tableColumn);
		}

		if (usingActions) {
			baseIndex += columnNames.size();
			for (int i = 0; i < actions.size(); i++) {
				TableColumn tableColumn = new TableColumn(baseIndex + i);
				tableColumn.setCellRenderer(new ActionCellRenderer());
				tableColumn.setMaxWidth(50);
				//	tableColumn.setCellEditor(new ActionCellEditor())
				tcm.addColumn(tableColumn);
			}
		}

		return tcm;
	}

	/**
	 * Try to get a TableCellRenderer from PropertyDescriptors or ColumnDefinitions
	 * @param index Column index
	 * @return a TableCellRenderer, null if none configured
	 */
	private TableCellRenderer getColumnRenderer(int index) {
		TableCellRenderer renderer = (TableCellRenderer) pds.get(index).getValue(CELL_RENDERER);
		if (renderer == null && columns.size() > 0)
			renderer = columns.get(index).getRenderer();

		if (renderer == null)
			renderer = defaultTableCellRenderer;

		return renderer;
	}

	/**
	 * Try to get a TableCellEditor from PropertyDescriptors or ColumnDefinitions
	 * @param index Column index
	 * @return a TableCellEditor, null if none configured
	 */
	private TableCellEditor getColumnEditor(int index) {
		TableCellEditor editor = (TableCellEditor) pds.get(index).getValue(CELL_EDITOR);
		if (editor == null && columns.size() > 0)
			editor = columns.get(index).getEditor();

		return editor;
	}
	
	private TableCellRenderer getColumnRenderer(String name) {
		TableCellRenderer renderer = null;
		for (ColumnDefinition cd : this.columns) { 
			if (name.equals(cd.getName())) {
				renderer = cd.getRenderer();
				break;
			}
		}
		
		return renderer != null ? renderer : this.defaultTableCellRenderer;
	}
	
	private TableCellEditor getColumnEditor(String name) {
		TableCellEditor editor = null;
		
		for (ColumnDefinition cd : this.columns) {
			if (name.equals(cd.getName())) {
				editor = cd.getEditor();
				break;
			}
		}
		
		return editor;
	}

	/**
	 * Try to get a column width from PropertyDescriptors or ColumnDefinitions
	 * @param index Column index
	 * @return a column width, null if none configured
	 */
	private Integer getColumnWidth(int index) {
		Integer maxWidth = (Integer) pds.get(index).getValue(MAX_WIDTH);
		if (maxWidth == null && columns.size() > 0)
			maxWidth = columns.get(index).getWidth();

		return maxWidth;
	}
	
	private Integer getColumnWidth(String name) {
		for (ColumnDefinition cd : this.columns) {
			if (name.equals(cd.getName()))
				return cd.getWidth();
		}
		
		return null;
	}

	/**
	 * Add a Object to underlaying list
	 * @param o the object to add
	 * @return true if added
	 */
	public boolean add(Object o) {
		boolean result = list.add(o);

		if (usingChecks)
			checks.add(Boolean.FALSE);

		if (list.size() == 1) {
			// adding on empty list, need to init
			init();
		}
		
		fireTableChanged(new TableModelEvent(this, list.size() - 1, list.size() - 1, TableModelEvent.ALL_COLUMNS,
				TableModelEvent.INSERT));

		return result;
	}

	/** 
	 * Remove a object from underlaying list model
	 * @param index column to remove
	 * @return the removed object
	 */
	public Object remove(int index) {
		Object result = list.remove(index);
		fireTableChanged(new TableModelEvent(this, index, index, TableModelEvent.ALL_COLUMNS, TableModelEvent.DELETE));
		return result;
	}

	/**
	 * Test if index is a property column
	 * @param column index to check
	 * @return true if index is a property column
	 */
	public boolean isPropertyColumn(int column) {
		if (usingChecks) {
			return column > 0 && column <= columnNames.size();
		} else {
			return column < columnNames.size();
		}
	}

	/**
	 * Test if index is a check column
	 * @param column column index to check
	 * @return true if index is a check column
	 */
	public boolean isCheckColum(int column) {
		return usingChecks && column == 0;
	}

	public boolean isActionColumn(int column) {
		return !(isPropertyColumn(column) || isCheckColum(column));
	}

	/**
	 * Get a property name from column index, used in PageableTable
	 * @param index to convert
	 * @return converted index
	 */
	public String getPropertyName(int index) {
		if (isPropertyColumn(index)) {
			return columnNames.get(columnToPropertyIndex(index));
		}
		return null;
	}

	/**
	 * Return de columnIndex of property
	 * @param propertyName property to find
	 * @return the column index or -1 if not found
	 */
	@SuppressWarnings("unused")
	private int getColumnByPropertyName(String propertyName) {
		int i = 0;
		for (String columnName : columnNames) {
			if (columnName.equals(propertyName)) {
				return isUsingChecks() ? i + 1 : i;
			}
			i++;
		}
		return -1;
	}

	/**
	 * Convert column model index to property index
	 * @param column the column to convert
	 * @return the property index
	 */
	public int columnToPropertyIndex(int column) {
		return usingChecks ? column - 1 : column;
	}

	/**
	 * Fire a model table changed
	 */
	public void fireTableChanged() {
		fireTableChanged(new TableModelEvent(this, TableModelEvent.HEADER_ROW));
	}

	public void setColumnEditable(int columnIndex, boolean value) {
		if (isPropertyColumn(columnIndex))
			editableMap.put(getPropertyName(columnIndex), value);
	}

	public void setEditableMap(Map<String, Boolean> editableMap) {
		this.editableMap = editableMap;
	}

	// Getters and Setters

	public void setList(List list) {
		this.list = list;
		// initialize if not already initialized or class model changes
		boolean initilized = pds.size() > 0;
		boolean modelClassChanged = list.size() > 0 && !list.get(0).getClass().equals(modelClass);

		if (!initilized || modelClassChanged) {
			if (modelClassChanged)
				modelClass = list.get(0).getClass();
			init();
		}

		if (usingChecks)
			buildCheckArray();

		fireTableChanged();

	}

	public List getList() {
		return list;
	}

	public Iterator<?> iterator() {
		return list.iterator();
	}

	public boolean isUsingIntrospection() {
		return usingIntrospection;
	}

	public void setUsingIntrospection(boolean usingIntrospection) {
		this.usingIntrospection = usingIntrospection;
	}

	public List<String> getColumnNames() {
		return columnNames;
	}

	public int getPropertyCount() {
		return columnNames.size();
	}

	public void setColumnNames(List<String> columnNames) {
		this.columnNames = columnNames;
	}
	
	public void setColumnNames(String[] columnNames) {
		setColumnNames(Arrays.asList(columnNames));
	}

	public List<String> getDisplayNames() {
		return displayNames;
	}

	public void setDisplayNames(List<String> displayNames) {
		this.displayNames = displayNames;
	}
	
	public void setDisplayNames(String[] displayNames) {
		setDisplayNames(Arrays.asList(displayNames));
	}

	public boolean isUsingChecks() {
		return usingChecks;
	}

	public void setUsingChecks(boolean useChecks) {
		this.usingChecks = useChecks;
	}

	public List<Action> getActions() {
		return actions;
	}

	public void setActions(List<Action> actions) {
		this.actions = actions;
	}

	public boolean isUsingActions() {
		return usingActions;
	}

	public void setUsingActions(boolean useActions) {
		this.usingActions = useActions;
	}

	public Map<String, Boolean> getEditableMap() {
		return editableMap;
	}

	/**
	 * Get a primary key of entity in the list
	 * @param row row of model
	 * @return the primary key of model, if any
	 */
	private Object getPrimaryKey(Object row) {
		if (BeanUtils.getPropertyDescriptor(modelClass, id) == null)
			return row;
		
		BeanWrapper wrapper = PropertyAccessorFactory.forBeanPropertyAccess(row);
		return wrapper.getPropertyValue(id);
	}

	/**
	 * Check a list of keys
	 * @param keys
	 */
	public void check(List<Serializable> keys) {
		if (usingChecks) {
			selectedRowSet.addAll(keys);
			fillChecks(true);
		}
	}

	/**
	 * Get a List with all selected model keys
	 * @return List with  checked model keys
	 */
	public List<Serializable> getChecked() {
		return new ArrayList<Serializable>(selectedRowSet);
	}
	
	public List getVisibleChecked() {
		List visibleChecked = new ArrayList();
		
		for (int i = 0; i < checks.size(); i++) {
			if (checks.get(i)) 
				visibleChecked.add(list.get(i));
		}
		
		return visibleChecked;
	}

	/**
	 * Uncheck All checks
	 */
	public void uncheckAll() {
		if (usingChecks) {
			selectedRowSet.clear();
			fillChecks(false);
		}
	}

	/** 
	 * Sets all checks to value 
	 * @param value
	 */
	private void fillChecks(boolean value) {
		for (int i = 0; i < checks.size(); i++)
			checks.set(i, value);

		fireTableChanged(new TableModelEvent(this, 0, list.size() - 1));
	}

	/**
	 * Add an action to action List
	 * @param action
	 */
	public void addAction(Action action) {
		if (!actions.contains(action))
			actions.add(action);
	}

	/**
	 * @return the modelClass
	 */
	public Class getModelClass() {
		return modelClass;
	}

	/**
	 * @param modelClass the modelClass to set
	 */
	public void setModelClass(Class modelClass) {
		this.modelClass = modelClass;
	}

	/**
	 * @return the columns
	 */
	public List<ColumnDefinition> getColumns() {
		return columns;
	}

	/**
	 * @param columns the columns to set
	 */
	public void setColumns(List<ColumnDefinition> columns) {
		this.columns = columns;
		parseColumnDefinitions();
	}

	/**
	 * Parse columns definitions to internal state
	 */
	private void parseColumnDefinitions() {
		displayNames.clear();
		columnNames.clear();
		editableMap.clear();

		for (ColumnDefinition cd : columns) {
			columnNames.add(cd.getName());
			displayNames.add(cd.getDisplayName());
			editableMap.put(cd.getName(), cd.isEditable());
		}
	}

	/**
	 * @return the defaultTableCellRenderer
	 */
	public TableCellRenderer getDefaultTableCellRenderer() {
		return defaultTableCellRenderer;
	}

	/**
	 * @param defaultTableCellRenderer the defaultTableCellRenderer to set
	 */
	public void setDefaultTableCellRenderer(TableCellRenderer defaultTableCellRenderer) {
		this.defaultTableCellRenderer = defaultTableCellRenderer;
	}

	/**
	 * @param column column index
	 * @return sort property for column
	 */
	public String getSortPropertyName(int column) {
		String sortPropertyName = null;
		if (isPropertyColumn(column)) {
			if (columns.size() > column)
				sortPropertyName = columns.get(columnToPropertyIndex(column)).getSortProperty();
			if (sortPropertyName == null)
				sortPropertyName = getPropertyName(column);
		}

		return sortPropertyName;
	}
	


	/**
	 * @return the messageSource
	 */
	public MessageSource getMessageSource() {
		return this.messageSource.getMessageSource();
	}

	/**
	 * @param messageSource the messageSource to set
	 */
	@Autowired
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource.setMessageSource(messageSource);
	}

	/**
	 * Check all models on list
	 */
	public void checkAll() {
		fillChecks(true);
	}

	/**
	 * @param toRemove
	 */
	public void removeAll(Collection toRemove) {
		list.removeAll(toRemove);
		fireTableChanged();
	}
	
	public void remove(Object toRemove) {
		if (toRemove != null)
			list.remove(toRemove);
	}
}
