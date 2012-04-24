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

import java.awt.Component;
import java.util.Stack;

import javax.swing.BorderFactory;
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
	 * @see info.joseluismartin.gui.form.SimpleBoxFormBuilder#add(java.awt.Component)
	 */
	public void add(Component c) {
		builder.add(c);
	}

	/**
	 * @param name
	 * @param c
	 * @see info.joseluismartin.gui.form.SimpleBoxFormBuilder#add(java.lang.String, java.awt.Component)
	 */
	public void add(String name, Component c) {
		builder.add(name, c);
	}

	/**
	 * @param i
	 * @see info.joseluismartin.gui.form.SimpleBoxFormBuilder#setMaxWidth(int)
	 */
	public void setMaxWidth(int i) {
		builder.setMaxWidth(i);
	}

	/**
	 * @see info.joseluismartin.gui.form.SimpleBoxFormBuilder#row()
	 */
	public void row() {
		builder.row();
	}

	/**
	 * @return the form
	 * @see info.joseluismartin.gui.form.SimpleBoxFormBuilder#getForm()
	 */
	public JComponent getForm() {
		return builder.getForm();
	}

	/**
	 * @see info.joseluismartin.gui.form.SimpleBoxFormBuilder#reset()
	 */
	public void reset() {
		builder.reset();
	}

	/**
	 * @see info.joseluismartin.gui.form.SimpleBoxFormBuilder#next()
	 */
	public void next() {
		builder.next();
	}

	/**
	 * @return form height
	 * @see info.joseluismartin.gui.form.SimpleBoxFormBuilder#getHeight()
	 */
	public int getHeight() {
		return builder.getHeight();
	}

	/**
	 * @param height
	 * @see info.joseluismartin.gui.form.SimpleBoxFormBuilder#setHeight(int)
	 */
	public void setHeight(int height) {
		builder.setHeight(height);
	}

	/**
	 * @return true if form debug is enabled.
	 * @see info.joseluismartin.gui.form.SimpleBoxFormBuilder#isDebug()
	 */
	public boolean isDebug() {
		return builder.isDebug();
	}

	/**
	 * @param debug
	 * @see info.joseluismartin.gui.form.SimpleBoxFormBuilder#setDebug(boolean)
	 */
	public void setDebug(boolean debug) {
		builder.setDebug(debug);
	}

	/**
	 * @return true if this form has height fixed.
	 * @see info.joseluismartin.gui.form.SimpleBoxFormBuilder#isFixedHeight()
	 */
	public boolean isFixedHeight() {
		return builder.isFixedHeight();
	}

	/**
	 * @param fixedHeight
	 * @see info.joseluismartin.gui.form.SimpleBoxFormBuilder#setFixedHeight(boolean)
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

}
