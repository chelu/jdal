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
import java.io.Serializable;

import com.vaadin.server.Resource;
import com.vaadin.ui.Component;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.Table.ColumnGenerator;

/**
 * Holder for configurable components by colum in a vaadin table.
 * For use friendly in Spring bean application context as inner bean.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class Column implements Serializable {
	
	private String name;
	private String displayName;
	private int width = -1;
	private Float expandRatio;
 	private Align align;
	private Resource icon;
	private boolean sortable = true;
	private String sortPropertyName;
	private Class<? extends Component> cellComponent;
	private Class<? extends Component> cellEditor;
	private PropertyEditor propertyEditor;
	private ColumnGenerator columnGenerator;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return the displayName
	 */
	public String getDisplayName() {
		return displayName;
	}
	
	/**
	 * @param displayName the displayName to set
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * @param width the width to set
	 */
	public void setWidth(int width) {
		this.width = width;
	}
	
	/**
	 * @return the icon
	 */
	public Resource getIcon() {
		return icon;
	}
	
	/**
	 * @param icon the icon to set
	 */
	public void setIcon(Resource icon) {
		this.icon = icon;
	}

	/**
	 * @return the align
	 */
	public Align getAlign() {
		return align;
	}

	/**
	 * @param align the align to set
	 */
	public void setAlign(Align align) {
		this.align = align;
	}

	/**
	 * @return true if is a sortable column
	 */
	public boolean isSortable() {
		return sortable;
	}

	/**
	 * @param sortable the sortable to set
	 */
	public void setSortable(boolean sortable) {
		this.sortable = sortable;
	}

	/**
	 * @return the sortPropertyName
	 */
	public String getSortPropertyName() {
		return sortPropertyName != null ? sortPropertyName : name;
	}

	/**
	 * @param sortPropertyName the sortPropertyName to set
	 */
	public void setSortPropertyName(String sortPropertyName) {
		this.sortPropertyName = sortPropertyName;
	}

	/**
	 * @return the cellComponent
	 */
	public Class<?extends Component> getCellComponent() {
		return cellComponent;
	}

	/**
	 * @param cellComponent the cellComponent to set
	 */
	public void setCellComponent(Class<?extends Component> cellComponent) {
		this.cellComponent = cellComponent;
	}

	/**
	 * @return the cellEditor
	 */
	public Class<?extends Component> getCellEditor() {
		return cellEditor;
	}

	/**
	 * @param cellEditor the cellEditor to set
	 */
	public void setCellEditor(Class<?extends Component> cellEditor) {
		this.cellEditor = cellEditor;
	}

	/**
	 * @return the propertyEditor
	 */
	public PropertyEditor getPropertyEditor() {
		return propertyEditor;
	}

	/**
	 * @param propertyEditor the propertyEditor to set
	 */
	public void setPropertyEditor(PropertyEditor propertyEditor) {
		this.propertyEditor = propertyEditor;
	}

	/**
	 * @return the expandRatio
	 */
	public Float getExpandRatio() {
		return expandRatio;
	}

	/**
	 * @param expandRatio the expandRatio to set
	 */
	public void setExpandRatio(Float expandRatio) {
		this.expandRatio = expandRatio;
	}

	public ColumnGenerator getColumnGenerator() {
		return columnGenerator;
	}

	public void setColumnGenerator(ColumnGenerator columnGenerator) {
		this.columnGenerator = columnGenerator;
	}
}
