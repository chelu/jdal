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
package info.joseluismartin.gui.form;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.border.Border;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;

/**
 * A FormBuilder that create form using columns of Box.
 * Add components using a implicit cursor.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class SimpleBoxFormBuilder {
	private static final Log log = LogFactory.getLog(SimpleBoxFormBuilder.class);
	private Box container = Box.createHorizontalBox();
	private List<Box> columns = new ArrayList<Box>();
	private List<Integer> columnsWidth = new ArrayList<Integer>();
	private List<Integer> rowsHeight = new ArrayList<Integer>();
	private int index = 0;
	private int rows = 0;
	private int rowHeight = 25;
	private int defaultRowHeight = 25;
	private int charWidth = 6;
	private boolean debug = false;
	private boolean fixedHeight = false;
	private MessageSource messageSource;
	private FormFocusTransversalPolicy focusTransversal = new FormFocusTransversalPolicy();
	private Border border = null;
	
	
	/** 
	 * Default Ctor 
	 */
	public SimpleBoxFormBuilder() {
	}
	
	public SimpleBoxFormBuilder(Border border) {
		this.border = border;
	}
	
	public SimpleBoxFormBuilder(int rowHeight) {
		this(rowHeight, null);
	}
	
	public SimpleBoxFormBuilder(int rowHeight, Border border) {
		this.rowHeight = rowHeight;
		this.border = border;
	}
	
	/**
	 * Add a component to Form at position pointer by cursor, 
	 * Increments cursor by one.
	 * @param c Component to add
	 */
	public void add(Component c) {
		addBox(c);
		// don't add Labels to focus transversal
		if (!(c instanceof JLabel)) {
			focusTransversal.add(c);
		}
		if (!c.isMaximumSizeSet())
			c.setMaximumSize(new Dimension(Short.MAX_VALUE, rowHeight));
	}
	
	public void addBox(Component c) {
		if (rows == 0 && rowsHeight.isEmpty()) {
			log.warn("You must call row() before adding components. I will add a row with default height for you");
			row();
		}
		Box column = getColumn();
		column.add(c);
		column.add(Box.createVerticalStrut(5));
		index++;
	}
	
	/**
	 * Gets current column pointed to cursor, create one if none.
	 * @return a new or existent column Box.
	 */
	private Box getColumn() {
		Box column = null;
		if (index < columns.size()) {
			column = (Box) columns.get(index);
		}
		else {
			column = Box.createVerticalBox();
			columns.add(column);
			container.add(column);
			container.add(Box.createHorizontalStrut(5));
			columnsWidth.add(0);
			
			if (debug) {
				column.setBorder(BorderFactory.createLineBorder(Color.RED));
			}
		}
		return column;
	}
	
	/**
	 * Add a component with label, increments cursor by two.
	 * @param name label string
	 * @param c component.
	 */
	public void add(String name, Component c) {
		JLabel label = new JLabel(name);
		add(label);
		setMaxWidth(name.length()*charWidth);
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
	public JComponent getForm() {
		// set sizes;
		int columnHeight= 0;
		for (int h : rowsHeight)
			columnHeight += h;
		
		for (int i = 0; i < columns.size(); i++) {
			Box box = columns.get(i);
			int maxWidth = columnsWidth.get(i) == 0 ? Short.MAX_VALUE : columnsWidth.get(i);
			 box.setMaximumSize(new Dimension(maxWidth, columnHeight));
		}
		
		container.setFocusTraversalPolicy(focusTransversal);
		container.setFocusTraversalPolicyProvider(true);
		container.setSize(Short.MAX_VALUE, columnHeight);
		
		if (isFixedHeight())
			container.setMaximumSize(new Dimension(Short.MAX_VALUE, columnHeight));
		
		if (isDebug())
			container.setBorder(BorderFactory.createLineBorder(Color.GREEN));
		
		if (border != null)
			container.setBorder(border);
		
		return container;
	}
	
	/**
	 * Reset the form builder to reuse for creating a new panel
	 */
	public void reset() {
		columns = new ArrayList<Box>();
		columnsWidth = new ArrayList<Integer>();
		rowsHeight = new ArrayList<Integer>();
		container = Box.createHorizontalBox();
		
		index = 0;
		rows = 0;
		
		focusTransversal = new FormFocusTransversalPolicy();
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
	 * @return the border
	 */
	public Border getBorder() {
		return border;
	}

	/**
	 * @param border the border to set
	 */
	public void setBorder(Border border) {
		this.border = border;
	}

	

}
