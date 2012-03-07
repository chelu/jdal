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

import info.joseluismartin.gui.bind.BinderFactory;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;

import org.springframework.context.MessageSource;

/**
 * A FormBuilder that create form using columns of Box.
 * Add components using a implicit cursor.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class BoxFormBuilder {
	private Box container = Box.createHorizontalBox();
	private List<Box> columns = new ArrayList<Box>();
	private List<Integer> columnsWidth = new ArrayList<Integer>();
	private List<Integer> columnsHeight = new ArrayList<Integer>();
	private int index = 0;
	private int rows = 0;
	private int height = 30;
	private int charWidth = 6;
	private boolean debug = false;
	private BinderFactory binderFactory;
	private MessageSource messageSource;
	private FormFocusTransversalPolicy focusTransversal = new FormFocusTransversalPolicy();
	
	
	/** 
	 * Default Ctor 
	 */
	public BoxFormBuilder() {
	}
	
	/** 
	 * Default Ctor 
	 */
	public BoxFormBuilder(BinderFactory binderFactory) {
		this.binderFactory = binderFactory;
	}
	
	/**
	 * Add a component to Form at position pointer by cursor, 
	 * Increments cursor by one.
	 * @param c Component to add
	 */
	public void add(Component c) {
		Box column = getColumn();
		
		if (!c.isMaximumSizeSet())
			c.setMaximumSize(new Dimension(Short.MAX_VALUE, height));
		
		column.add(c);
		column.add(Box.createVerticalStrut(5));
		index++;
		
		// don't add Labels to focus transversal
		if (!(c instanceof JLabel)) {
			focusTransversal.add(c);
		}
			
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
			columnsHeight.add(0);
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

	/**
	 * Move cursor to next row.
	 */
	public void row() {
		index = 0;
		rows++;
	}
	
	/**
	 * Builds the panel form.
	 * @return the form component
	 */
	public JComponent getForm() {
		// set sizes;
		for (int i = 0; i < columns.size(); i++) {
			Box box = columns.get(i);
			int maxWidth = columnsWidth.get(i) == 0 ? Short.MAX_VALUE : columnsWidth.get(i);
			int maxHeight = columnsHeight.get(i) == 0 ? (rows +1)*height : columnsHeight.get(i);
			box.setMaximumSize(new Dimension(maxWidth, maxHeight));
		}
		
		container.setFocusTraversalPolicy(focusTransversal);
		container.setFocusTraversalPolicyProvider(true);
		
		return container;
	}
	
	/**
	 * Reset the form builder to reuse for creating a new panel
	 */
	public void reset() {
		columns = new ArrayList<Box>();
		columnsWidth = new ArrayList<Integer>();
		columnsHeight = new ArrayList<Integer>();
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
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	// TODO Auto-generated method stub
	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}
	
	public BinderFactory getBinderFactory() {
		return binderFactory;
	}

	public void setBinderFactory(BinderFactory binderFactory) {
		this.binderFactory = binderFactory;
	}
	
	public MessageSource getMessageSource() {
		return messageSource;
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
	

}
