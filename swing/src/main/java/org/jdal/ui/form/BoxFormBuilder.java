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
package org.jdal.ui.form;

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
	
	
	public void endBox() {
		JComponent c = builder.getForm();
		builder = stack.pop();
		builder.addBox(c);
		builder.setHeight(c.getHeight());
	}
	/**
	 * @param c
	 * @see org.jdal.ui.form.SimpleBoxFormBuilder#add(java.awt.Component)
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
	 * @see org.jdal.ui.form.SimpleBoxFormBuilder#add(java.lang.String, java.awt.Component)
	 */
	public void add(String name, Component c) {
		builder.add(name, c);
	}

	/**
	 * @param i
	 * @see org.jdal.ui.form.SimpleBoxFormBuilder#setMaxWidth(int)
	 */
	public void setMaxWidth(int i) {
		builder.setMaxWidth(i);
	}

	/**
	 * @see org.jdal.ui.form.SimpleBoxFormBuilder#row()
	 */
	public void row() {
		builder.row();
	}

	/**
	 * @return the form
	 * @see org.jdal.ui.form.SimpleBoxFormBuilder#getForm()
	 */
	public JComponent getForm() {
		return builder.getForm();
	}

	/**
	 * @see org.jdal.ui.form.SimpleBoxFormBuilder#reset()
	 */
	public void reset() {
		builder.reset();
	}

	/**
	 * @see org.jdal.ui.form.SimpleBoxFormBuilder#next()
	 */
	public void next() {
		builder.next();
	}

	/**
	 * @return form height
	 * @see org.jdal.ui.form.SimpleBoxFormBuilder#getHeight()
	 */
	public int getHeight() {
		return builder.getHeight();
	}

	/**
	 * @param height
	 * @see org.jdal.ui.form.SimpleBoxFormBuilder#setHeight(int)
	 */
	public void setHeight(int height) {
		builder.setHeight(height);
	}

	/**
	 * @return true if form debug is enabled.
	 * @see org.jdal.ui.form.SimpleBoxFormBuilder#isDebug()
	 */
	public boolean isDebug() {
		return builder.isDebug();
	}

	/**
	 * @param debug
	 * @see org.jdal.ui.form.SimpleBoxFormBuilder#setDebug(boolean)
	 */
	public void setDebug(boolean debug) {
		builder.setDebug(debug);
	}

	/**
	 * @return true if this form has height fixed.
	 * @see org.jdal.ui.form.SimpleBoxFormBuilder#isFixedHeight()
	 */
	public boolean isFixedHeight() {
		return builder.isFixedHeight();
	}

	/**
	 * @param fixedHeight
	 * @see org.jdal.ui.form.SimpleBoxFormBuilder#setFixedHeight(boolean)
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
	 * @see org.jdal.ui.form.SimpleBoxFormBuilder#getDefaultRowHeight()
	 */
	public int getDefaultRowHeight() {
		return builder.getDefaultRowHeight();
	}

	/**
	 * @param defaultRowHeight
	 * @see org.jdal.ui.form.SimpleBoxFormBuilder#setDefaultRowHeight(int)
	 */
	public void setDefaultRowHeight(int defaultRowHeight) {
		builder.setDefaultRowHeight(defaultRowHeight);
	}

	/**
	 * @return default space
	 * @see org.jdal.ui.form.SimpleBoxFormBuilder#getDefaultSpace()
	 */
	public int getDefaultSpace() {
		return builder.getDefaultSpace();
	}

	/**
	 * @param defaultSpace
	 * @see org.jdal.ui.form.SimpleBoxFormBuilder#setDefaultSpace(int)
	 */
	public void setDefaultSpace(int defaultSpace) {
		builder.setDefaultSpace(defaultSpace);
	}

}
