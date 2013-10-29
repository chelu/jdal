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
package org.jdal.vaadin.ui.table;

import java.beans.PropertyEditor;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.jdal.beans.PropertyUtils;
import org.jdal.util.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.util.AbstractBeanContainer;
import com.vaadin.ui.Component;
import com.vaadin.ui.Table;

/**
 * Add methods to vaadin Table for friendly configuration on spring bean defintion file.
 * Use <code>info.joseluismartin.ui.table.Column</code> as inner bean for configure columns:
 * <code>
 * <property name="columns">
 * 	<list>
 * 		<bean class="info.joseluismartin.ui.table.Column"/>
 * 			<property name="name" value="a_bean_property_name"/>
 * 			<property name="displayName" value="String_to_show_in_table_header"/>
 * 			<property name="width" value="column_width"/>
 * 		    <property name="cellEditor" value="cellEditorClass"/>
 *          <property name="cellComponent" value="cellComponent"/>
 * 			...
 * 		</bean>
 * 		<bean class="info.joseluismartin.vaadin.ui.table.Column">
 * 			...
 * 		</bean>
 * 	</list>
 * <property name="service" value="a_persistent_service"/>
 * <property>
 * </code>
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
@SuppressWarnings("serial")
@Configurable
public class ConfigurableTable extends Table {
	
	private boolean usingChecks = false;
	private List<Column> columns;
	private TableSorter sorter;
	private Map<String, Column> columnMap = new HashMap<String, Column>();
	@Autowired
	private transient MessageSource messageSource;
	
	
	/**
	 * Configure Vaadin table with column definitions. 
	 * Method useful for use from context bean definition file.
	 * @param columns
	 */
	public void setColumns(List<Column> columns) {
		this.columns = columns;
		columnMap.clear();
		for (Column c : columns)
			columnMap.put(c.getName(), c);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setContainerDataSource(Container newDataSource) {
		super.setContainerDataSource(newDataSource);
		configure();
	}
	
	/**
	 * Vaadin Table throw an exception when set this properties 
	 * with an empty Container datasource. 
	 */
	protected void configure() {
		if (columns == null)
			return;
		
		int size = columns.size();
		String[] visibleColumns = new String[size];
		String[] displayNames = new String[size];
		String[] alignments = new String[size];
		int[] widths = new int[size];
		float[] expandRatios = new float[size];
				
		for (int i = 0; i < size; i++) {
			visibleColumns[i] = columns.get(i).getName();
			
			if (visibleColumns[i].contains(PropertyUtils.PROPERTY_SEPARATOR))
				addNestedPropertyIfPossible(visibleColumns[i]);
			
			displayNames[i] = intenacionalize(columns.get(i).getDisplayName());
			alignments[i] = columns.get(i).getAlign();
			widths[i] = columns.get(i).getWidth();
		}
		
		this.setVisibleColumns(visibleColumns);
		this.setColumnHeaders(displayNames);
		this.setColumnAlignments(convertAlignments(alignments));
		
		for (int i = 0; i < size; i++) {
			if ( widths[i] != -1) {
				this.setColumnWidth(visibleColumns[i], widths[i]);
			}
			else if (expandRatios[i] != 0) {
				this.setColumnExpandRatio(visibleColumns[i], expandRatios[i]);
			}
		}
	}


	private Align[] convertAlignments(String[] alignments) {
		Align[] values = new Align[alignments.length];
		for (int i = 0; i < alignments.length; i++) {
			values[i] = Align.CENTER.convertStringToAlign(alignments[i]);
		}
		
		return values;
	}


	/**
	 * Try to resolve string as i18n code, return string if none.
	 * @param name
	 * @return translated code or the string
	 */
	private String intenacionalize(String name) {
		if (messageSource == null)
			return name;
		
		try {
			return messageSource.getMessage(name, null, Locale.getDefault());
		} 
		catch (NoSuchMessageException nsme) {
			return name;
		}
	}


	/**
	 * Add nested property to datasource if is possilbe. 
	 * @param name of nested property 
	 */
	private void addNestedPropertyIfPossible(String name) {
		Container cds = getContainerDataSource();
		if (cds instanceof AbstractBeanContainer<?,?>) {
			((AbstractBeanContainer<?,?>) cds).addNestedContainerProperty(name);
		}
	}


	/**
	 * Gets usingChecks property, if true, table show checkboxes for row selection
	 * @return the usingChecks
	 */
	public boolean isUsingChecks() {
		return usingChecks;
	}


	/**
	 * Sets usingChecks property, if true, table show checkboxes for row selection
	 * @param usingChecks the usingChecks to set
	 */
	public void setUsingChecks(boolean usingChecks) {
		this.usingChecks = usingChecks;
	}


	/**
	 * Sort on container or on sorter
	 */
	@Override
	public void sort(Object[] propertyId, boolean[] ascending) {
		if (sorter != null)
			sorter.sort(propertyId, ascending);
		else {
			super.sort(propertyId, ascending);
		}
	}

	/**
	 * Override to handle server side sorting
	 * {@inheritDoc}
	 */
	@Override
	public Collection<?> getSortableContainerPropertyIds() {
		List<Object> sortableIds = new LinkedList<Object>();
		for (Column c :columns ) {
			if (c.isSortable())
				sortableIds.add(c.getName());
		}
		
		return sortableIds;
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Object getPropertyValue(Object rowId, Object colId, Property property) {
		Column column = columnMap.get(colId);
		if (column != null) {
			if (isEditable()) {
				Class<?extends Component> editorClass = column.getCellEditor();
				if (editorClass == null)
					return super.getPropertyValue(rowId, colId, property);
				else {
					return getComponentForProperty(property, editorClass);
				}
			}
			// Test cell component 
			Class<?extends Component> cellComponentClass = column.getCellComponent();
			if (cellComponentClass != null) {
				return getComponentForProperty(property, cellComponentClass);
			}

			// Last try, test property editor
			PropertyEditor pe = column.getPropertyEditor();
			if (pe != null) {
				pe.setValue(property.getValue());
				return pe.getAsText();
			}
		}

		// Default behavior
		return super.getPropertyValue(rowId, colId, property);
	}


	/**
	 * Instantiate editor Class and set property as DataSource if 
	 * the editor is Property.Viewer (it should be).
	 * @param property property to set
	 * @param editorClass class to instantiate
	 * @return editor instance
	 */
	private Component getComponentForProperty(Property property, Class<? extends Component> editorClass) {
		Component editor = BeanUtils.instantiate(editorClass);
		if (editor instanceof Property.Viewer) {
			((Property.Viewer) editor).setPropertyDataSource(property);
		}
		return editor;
	}
	
	public Column getColumn(String name) {
		return columnMap.get(name);
	}
	
	public List<Column> getColumns() {
		return this.columns;
	}
	

	public TableSorter getSorter() {
		return sorter;
	}


	public void setSorter(TableSorter sorter) {
		this.sorter = sorter;
	}
}
