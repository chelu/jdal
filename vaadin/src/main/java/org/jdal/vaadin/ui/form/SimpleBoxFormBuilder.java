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
package org.jdal.vaadin.ui.form;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.border.Border;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdal.vaadin.ui.Box;
import org.springframework.context.MessageSource;

import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

/**
 * A FormBuilder that create form using BoxLayouts
 * Add components using a implicit cursor.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class SimpleBoxFormBuilder {
	
	public static final int SIZE_UNDEFINED = Short.MAX_VALUE;
	
	private static final Log log = LogFactory.getLog(SimpleBoxFormBuilder.class);
	private HorizontalLayout container = new HorizontalLayout();
	private List<VerticalLayout> columns = new ArrayList<VerticalLayout>();
	private List<Integer> columnsWidth = new ArrayList<Integer>();
	private List<Integer> rowsHeight = new ArrayList<Integer>();
	private int index = 0;
	private int rows = 0;
	private int rowHeight = 25;
	private int defaultRowHeight = 25;
	private int defaultSpace = 5;
	private int charWidth = 6;
	private boolean debug = false;
	private boolean fixedHeight = false;
	private MessageSource messageSource;
	
	/** 
	 * Default Ctor 
	 */
	public SimpleBoxFormBuilder() {
		this(calculateDefaultHeight());
	}
	
	public SimpleBoxFormBuilder(int defaultRowHeight) {
		this.defaultRowHeight = defaultRowHeight;
	}
	
	/**
	 * Add a component to Form at position pointer by cursor, 
	 * Increments cursor by one.
	 * @param c Component to add
	 */
	public void add(Component c) {
		addBox(c);
	}
	
	public void add(Component c, int maxWidth) {
		add(c);
		setMaxWidth(maxWidth);
	}
	
	public void addBox(Component c) {
		if (rows == 0 && rowsHeight.isEmpty()) {
			log.warn("You must call row() before adding components. I will add a row with default height for you");
			row();
		}
		VerticalLayout column = getColumn();
		
		column.addComponent(c);
		
		index++;
		
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
			if (!columns.isEmpty())
				container.addComponent(Box.createHorizontalStrut(defaultSpace));
			
			column = Box.createVerticalBox();
			columns.add(column);
			container.addComponent(column);
			columnsWidth.add(0);
		}
		
		return column;
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
	 * @param i
	 */
	public void setMaxWidth(int i) {
		if (i > columnsWidth.get(index - 1)) {
			columnsWidth.set(index - 1, i);
		}
		
	}
	
	public void row() {
		row(defaultRowHeight);
	}

	/**
	 * Move cursor to next row.
	 */
	public void row(int rowHeight) {
		index = 0;
		rows++;
		rowsHeight.add(rowHeight);
		this.rowHeight = rowHeight;
		
	}
	
	/**
	 * Builds the panel form.
	 * @return the form component
	 */
	public Component getForm() {
		// set sizes;
		int columnHeight= 0;
		for (int h : rowsHeight)
			columnHeight += h;
		
		// add space into components (fillers)
		columnHeight += (rows) * defaultSpace;
		
		for (int i = 0; i < columns.size(); i++) {
			VerticalLayout box = columns.get(i);
			int maxWidth = columnsWidth.get(i) == 0 ? Short.MAX_VALUE : columnsWidth.get(i);
			
			if (maxWidth < Short.MAX_VALUE && columnHeight < Short.MAX_VALUE) {
				box.setWidth(maxWidth, Sizeable.UNITS_PIXELS);
				box.setHeight(columnHeight, Sizeable.UNITS_PIXELS);
			}
		}
		
		
		if (isFixedHeight()) {
			container.setHeight(columnHeight, Sizeable.UNITS_PIXELS);
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

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}
	
	public MessageSource getMessageSource() {
		return messageSource;
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
	/**
	 * @return the fixedHeight
	 */
	public boolean isFixedHeight() {
		return fixedHeight;
	}

	/**
	 * @param fixedHeight the fixedHeight to set
	 */
	public void setFixedHeight(boolean fixedHeight) {
		this.fixedHeight = fixedHeight;
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
	 * @return the charWidth
	 */
	public int getCharWidth() {
		return charWidth;
	}

	/**
	 * @param charWidth the charWidth to set
	 */
	public void setCharWidth(int charWidth) {
		this.charWidth = charWidth;
	}
	
	/**
	 * @return
	 */
	private static int calculateDefaultHeight() {
		Dimension d =  new JTextField().getPreferredSize();
		return d != null ? d.height : 25;
	}

}
