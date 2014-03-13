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

import com.vaadin.server.Sizeable;
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
	private static final int DEFAULT_HEIGHT = 50;
	
	private static final Log log = LogFactory.getLog(SimpleBoxFormBuilder.class);
	private HorizontalLayout container = new HorizontalLayout();
	private List<VerticalLayout> columns = new ArrayList<VerticalLayout>();
	private List<Integer> columnsWidth = new ArrayList<Integer>();
	private List<Integer> rowsHeight = new ArrayList<Integer>();
	private int index = 0;
	private int rows = 0;
	private int rowHeight = 0;
	private int defaultRowHeight = DEFAULT_HEIGHT;
	private int defaultSpace = 5;
	private int defaultWidth = SIZE_UNDEFINED;
	private MessageSource messageSource;
	private boolean spacing = true;
	private boolean rowCellSpand = true;
	private boolean fixedHeight = false;
	private boolean fixedWidth = false;
	private boolean debug;
	
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
		add(c, null, defaultWidth, Alignment.MIDDLE_CENTER);
	}
	
	public void add(Component c, String label) {
		add(c, label, defaultWidth, Alignment.MIDDLE_CENTER);
	}
	
	
	public void add(Component c, int width) {
		add(c, null, width, Alignment.MIDDLE_CENTER);
	}
	
	public void add(Component c, String label, int width) {
		add(c, label, width, Alignment.MIDDLE_CENTER);
	}
	
	public void add(Component c, Alignment alignment) {
		add(c, null, defaultWidth, alignment);
	}
	
	public void add(Component c, String label,  Alignment alignment) {
		add(c, label, defaultWidth, alignment);
	}
	
	public void add(Component c, int width,  Alignment alignment) {
		add(c, null, width, alignment);
	}
	
	public void add(Component c, String label, int width, Alignment alignment) {
		if (rows == 0 && rowsHeight.isEmpty()) {
			log.warn("You must call row() before adding components. I will add a row with default height for you");
			row();
		}
		
		if (label != null)
			c.setCaption(label);
		
		VerticalLayout column = getColumn();
		column.addComponent(c);
		index++;
		
		setWidth(width);
		
		if (alignment != null) {
			column.setComponentAlignment(c, alignment);
		}
		
		if (rowCellSpand) {
			c.setWidth(100, Unit.PERCENTAGE);
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
			column.addStyleName("jd-box-column");
			
			if (debug)
				column.addStyleName("jd-box-debug");
			
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
			else {
				container.setExpandRatio(box, 0);
				box.setWidth(Sizeable.SIZE_UNDEFINED, Unit.PIXELS);
			}
			
			for (int j = 0; j < rowsHeight.size(); j++) {
				Component c = box.getComponent(j);
				int height = rowsHeight.get(j);

				if (height > SIZE_UNDEFINED &&  height < SIZE_FULL) {
					c.setHeight(height, Unit.PIXELS);
					box.setExpandRatio(c, 0);
				}
				else if (height == SIZE_FULL) {
					c.setHeight("100%");
					box.setExpandRatio(c, 1);
				}
				else {
					box.setExpandRatio(c, 0);
				}
			}
		}
		
		if (fixedHeight) {
			container.setHeight(getFormHeight(), Unit.PIXELS);
		}
		else {
			container.setHeight(100, Unit.PERCENTAGE);
		}
		
		if (fixedWidth) {
			container.setWidth(Sizeable.SIZE_UNDEFINED, Unit.PIXELS);
		}
		else {
			container.setWidth(100, Unit.PERCENTAGE);
		}

		container.addStyleName("jd-box");
		
		if (debug)
			container.addStyleName("jd-box-debug");
		
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
	
	public int getFormHeight() {
		int height = 0;
		for (int h : rowsHeight)
			height += h == SIZE_UNDEFINED ? DEFAULT_HEIGHT : h;
		
		height += (rows - 1) * 10;
	
		return height;
	}

	// Getters & Setters
	
	public int getHeight() {
		return rowHeight;
	}

	public void setHeight(int height) {
		this.rowHeight = height < SIZE_FULL ? height : SIZE_FULL;
		
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
		setHeight(SIZE_FULL);
	}

	public void addVerticalGlue() {
		Label label = new Label();
		add(label);
		setHeight(SIZE_FULL);
		
	}
	
	public void addHorizontalGlue() {
		Label label = new Label();
		label.setHeight("0px");
		add(label, SIZE_FULL);
	}
	
	public boolean isFixedHeight() {
		return fixedHeight;
	}

	public void setFixedHeight(boolean fixedHeight) {
		this.fixedHeight = fixedHeight;
	}

	public boolean isFixedWidth() {
		return fixedWidth;
	}

	public void setFixedWidth(boolean fixedWidth) {
		this.fixedWidth = fixedWidth;
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

}
