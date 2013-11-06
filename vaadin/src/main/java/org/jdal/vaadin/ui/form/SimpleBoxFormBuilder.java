/*
 * Copyright 2008-2013 the original author or authors.
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
package org.jdal.vaadin.ui.form;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdal.vaadin.ui.Box;
import org.springframework.context.MessageSource;

import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * A FormBuilder that create Layouts using horizontal and vertical layouts.
 * Add components using a implicit cursor.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class SimpleBoxFormBuilder {
	
	public static final int SIZE_FULL = Short.MAX_VALUE;
	public static final int SIZE_UNDEFINED = 0;
	
	private static final Log log = LogFactory.getLog(SimpleBoxFormBuilder.class);
	private HorizontalLayout container = new HorizontalLayout();
	private List<VerticalLayout> columns = new ArrayList<VerticalLayout>();
	private List<Integer> columnsWidth = new ArrayList<Integer>();
	private List<Integer> rowsHeight = new ArrayList<Integer>();
	private int index = 0;
	private int rows = 0;
	private int rowHeight = 0;
	private int defaultRowHeight = 0;
	private int defaultSpace = 5;
	private int defaultWidth = SIZE_UNDEFINED;
	private MessageSource messageSource;
	private boolean spacing = true;
	private boolean rowCellSpand = true;
	
	/** 
	 * Default Ctor 
	 */
	public SimpleBoxFormBuilder() {
		this(SIZE_UNDEFINED);
	}
	
	public SimpleBoxFormBuilder(int defaultRowHeight) {
		this.defaultRowHeight = defaultRowHeight;
		container.setSpacing(spacing);
	}
	
	/**
	 * Add a component to Form at position pointer by cursor, 
	 * Increments cursor by one.
	 * @param c Component to add
	 */
	public void add(Component c) {
		add(c, defaultWidth, null);
	}
	
	public void add(Component c, int width) {
		add(c, width, Alignment.MIDDLE_CENTER);
	}
	
	public void add(Component c, Alignment alignment) {
		add(c, defaultWidth, alignment);
	}
	
	public void add(Component c, int width, Alignment alignment) {
		if (rows == 0 && rowsHeight.isEmpty()) {
			log.warn("You must call row() before adding components. I will add a row with default height for you");
			row();
		}
		
		VerticalLayout column = getColumn();
		HorizontalLayout wrapper = new HorizontalLayout();
		wrapper.addComponent(c);
		column.addComponent(wrapper);
		index++;
		
		setWidth(width);
		
		if (alignment != null) {
			wrapper.setComponentAlignment(c, alignment);
			column.setComponentAlignment(wrapper, alignment);
		}
		
		if (rowCellSpand) {
			wrapper.setWidth("100%");
			c.setWidth("100%");
		}
	}
	
	/**
	 * Add a component with label, increments cursor by two.
	 * @param name label string
	 * @param c component.
	 */
	public void add(String name, Component c) {
		c.setCaption(name);
		add(c);
	}
	
	
	/**
	 * Gets current column pointed to cursor, create one if none.
	 * @return a new or existent column Box.
	 */
	private VerticalLayout getColumn() {
		VerticalLayout column = null;
		if (index < columns.size()) {
			column = (VerticalLayout) columns.get(index);
		}
		else {
			column = Box.createVerticalBox();
			column.setSpacing(spacing);
			columns.add(column);
			container.addComponent(column);
			columnsWidth.add(SIZE_UNDEFINED);
		}
		
		return column;
	}
	
	/**
	 * @param width
	 */
	public void setWidth(int width) {
		if (width > columnsWidth.get(index - 1)) {
			columnsWidth.set(index - 1, width);
		}
		
	}
	
	public void row() {
		row(defaultRowHeight, true);
	}
	
	public void row(boolean rowCellSpand) {
		row(defaultRowHeight, this.rowCellSpand);
	}
	
	public void row(int height) {
		row(height, true);
	}
	

	/**
	 * Move cursor to next row.
	 */
	public void row(int rowHeight, boolean rowCellSpand) {
		index = 0;
		rows++;
		rowsHeight.add(rowHeight);
		this.rowHeight = rowHeight;
		this.rowCellSpand = rowCellSpand;
		
	}
	
	/**
	 * Builds the panel form.
	 * @return the form component
	 */
	public Component getForm() {
		
		for (int i = 0; i < columns.size(); i++) {
			VerticalLayout box = columns.get(i);
			int width = columnsWidth.get(i);
			
			if (width > SIZE_UNDEFINED && width < SIZE_FULL) {
				box.setWidth(width, Unit.PIXELS);
				// shrink container
				container.setExpandRatio(box, 0);
			}
			else if (width == SIZE_FULL) {
				box.setWidth("100%");
				container.setExpandRatio(box, 1);
			}
			
			for (int j = 0; j < rowsHeight.size(); j++) {
				Component c = box.getComponent(j);
				int height = rowsHeight.get(j);

				if (height > SIZE_UNDEFINED &&  height < SIZE_FULL) {
					c.setHeight(height, Unit.PIXELS);
				}
				else if (height == SIZE_FULL) {
					c.setHeight("100%");
				}
			}
		}

		return container;
	}
	
	/**
	 * Reset the form builder to reuse for creating a new panel
	 */
	public void reset() {
		columns = new ArrayList<VerticalLayout>();
		columnsWidth = new ArrayList<Integer>();
		rowsHeight = new ArrayList<Integer>();
		container = Box.createHorizontalBox();
		
		index = 0;
		rows = 0;
		
	}
	
	public void next() {
		getColumn();
		index++;
	}
	

	// Getters & Setters
	
	public int getHeight() {
		return rowHeight;
	}

	public void setHeight(int height) {
		this.rowHeight = height;
		if (rowsHeight.size() > 0 && rows > 0) {
			rowsHeight.remove(rows -1);
			rowsHeight.add(height);
		}
	}
	
	public MessageSource getMessageSource() {
		return messageSource;
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
	/**
	 * @return the defaultRowHeight
	 */
	public int getDefaultRowHeight() {
		return defaultRowHeight;
	}

	/**
	 * @param defaultRowHeight the defaultRowHeight to set
	 */
	public void setDefaultRowHeight(int defaultRowHeight) {
		this.defaultRowHeight = defaultRowHeight;
	}

	/**
	 * @return the defaultSpace
	 */
	public int getDefaultSpace() {
		return defaultSpace;
	}

	/**
	 * @param defaultSpace the defaultSpace to set
	 */
	public void setDefaultSpace(int defaultSpace) {
		this.defaultSpace = defaultSpace;
	}

	/**
	 * @return the spacing
	 */
	public boolean isSpacing() {
		return spacing;
	}

	/**
	 * @param spacing the spacing to set
	 */
	public void setSpacing(boolean spacing) {
		this.spacing = spacing;
		container.setSpacing(spacing);
	}

	/**
	 * @return the rowCellSpand
	 */
	public boolean isRowCellSpand() {
		return rowCellSpand;
	}

	/**
	 * @param rowCellSpand the rowCellSpand to set
	 */
	public void setRowCellSpand(boolean rowCellSpand) {
		this.rowCellSpand = rowCellSpand;
	}

	/**
	 * @return the defaultWidth
	 */
	public int getDefaultWidth() {
		return defaultWidth;
	}

	/**
	 * @param defaultWidth the defaultWidth to set
	 */
	public void setDefaultWidth(int defaultWidth) {
		this.defaultWidth = defaultWidth;
	}

	public void addGlue() {
		Label label = new Label();
		label.setSizeFull();
		add(label, SIZE_FULL);	
	}
}
