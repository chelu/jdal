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
package org.jdal.swing.form;

import java.awt.Component;
import java.util.Stack;

import javax.swing.JComponent;
import javax.swing.border.Border;

/**
 * A FormBuilder that create form using Box.
 * Add components using a implicit cursor.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class BoxFormBuilder {
	/** hold form builders */
	private Stack<SimpleBoxFormBuilder> stack = new Stack<SimpleBoxFormBuilder>();
	/** current form builder */
	SimpleBoxFormBuilder builder;
	
	/** 
	 * Default Ctor 
	 */
	public BoxFormBuilder() {
		this.builder = new SimpleBoxFormBuilder();
	}
	
	public BoxFormBuilder(Border border) {
		this.builder = new SimpleBoxFormBuilder(border);
	}
	
	public BoxFormBuilder(int rowHeight) {
		this(rowHeight, null);
	}
	
	public BoxFormBuilder(int rowHeight, Border border) {
		this.builder = new SimpleBoxFormBuilder(rowHeight, border);
	}
	
	public void startBox(Border border) {
		boolean debug = builder.isDebug();
		stack.push(builder);
		builder = new SimpleBoxFormBuilder(border);
		builder.setDebug(debug);
	}

	public void startBox() {
		startBox(null);
	}
	
	
	public JComponent endBox() {
		JComponent c = builder.getForm();
		builder = stack.pop();
		builder.addBox(c);
		builder.setHeight(c.getHeight());
		
		return c;
	}
	/**
	 * @param c
	 */
	public void add(Component c) {
		builder.add(c);
	}

	public void add(Component c, int maxWidth) {
		builder.add(c, maxWidth);
	}
	/**
	 * @param name
	 * @param c
	 */
	public void add(String name, Component c) {
		builder.add(name, c);
	}

	/**
	 * @param i
	 */
	public void setMaxWidth(int i) {
		builder.setMaxWidth(i);
	}

	/**
	 */
	public void row() {
		builder.row();
	}

	/**
	 * @return the form
	 */
	public JComponent getForm() {
		return builder.getForm();
	}

	/**
	 */
	public void reset() {
		builder.reset();
	}

	/**
	 */
	public void next() {
		builder.next();
	}

	/**
	 * @return form height
	 */
	public int getHeight() {
		return builder.getHeight();
	}

	/**
	 * @param height
	 */
	public void setHeight(int height) {
		builder.setHeight(height);
	}

	/**
	 * @return true if form debug is enabled.
	 */
	public boolean isDebug() {
		return builder.isDebug();
	}

	/**
	 * @param debug
	 */
	public void setDebug(boolean debug) {
		builder.setDebug(debug);
	}

	/**
	 * @return true if this form has height fixed.
	 */
	public boolean isFixedHeight() {
		return builder.isFixedHeight();
	}

	/**
	 * @param fixedHeight
	 */
	public void setFixedHeight(boolean fixedHeight) {
		builder.setFixedHeight(fixedHeight);
	}	
	
	public void setElastic() {
		setHeight(Short.MAX_VALUE);
	}

	/**
	 * @param rowHeight
	 */
	public void row(int rowHeight) {
		builder.row(rowHeight);
	}

	/**
	 * @return default row height
	 */
	public int getDefaultRowHeight() {
		return builder.getDefaultRowHeight();
	}

	/**
	 * @param defaultRowHeight
	 */
	public void setDefaultRowHeight(int defaultRowHeight) {
		builder.setDefaultRowHeight(defaultRowHeight);
	}

	/**
	 * @return default space
	 */
	public int getDefaultSpace() {
		return builder.getDefaultSpace();
	}

	/**
	 * @param defaultSpace
	 */
	public void setDefaultSpace(int defaultSpace) {
		builder.setDefaultSpace(defaultSpace);
	}

}
