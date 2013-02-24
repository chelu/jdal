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

import java.util.Stack;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;


/**
 * A FormBuilder that create form using Vertical and Horizontal layouts.
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
	
	public BoxFormBuilder(int rowHeight) {
		this.builder = new SimpleBoxFormBuilder(rowHeight);
	}
	
	public void startBox() {
		stack.push(builder);
		builder = new SimpleBoxFormBuilder();
	}
	
	
	public void endBox() {
		Component c = builder.getForm();
		builder = stack.pop();
		builder.add(c);
	}
	/**
	 * @param c component to add
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
	
	public void add(Component c, Alignment aligment) {
		builder.add(c, aligment);
	}
	
	/**
	 * @param c
	 * @param width
	 * @param alignment
	 */
	public void add(Component c, int width, Alignment alignment) {
		builder.add(c, width, alignment);
		
	}

	/**
	 * @param i
	 */
	public void setWidth(int i) {
		builder.setWidth(i);
	}

	/**
	 */
	public void row() {
		builder.row();
	}

	/**
	 * @return the form
	 */
	public Component getForm() {
		return builder.getForm();
	}

	public void reset() {
		builder.reset();
	}

	public void next() {
		builder.next();
	}

	public int getHeight() {
		return builder.getHeight();
	}

	public void setHeight(int height) {
		builder.setHeight(height);
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

	public void row(boolean rowCellSpand) {
		builder.row(rowCellSpand);
	}

	public void row(int rowHeight, boolean rowCellSpand) {
		builder.row(rowHeight, rowCellSpand);
	}

	public boolean isSpacing() {
		return builder.isSpacing();
	}

	public void setSpacing(boolean spacing) {
		builder.setSpacing(spacing);
	}

	public boolean isRowCellSpand() {
		return builder.isRowCellSpand();
	}

	public void setRowCellSpand(boolean rowCellSpand) {
		builder.setRowCellSpand(rowCellSpand);
	}

	public int getDefaultWidth() {
		return builder.getDefaultWidth();
	}

	public void setDefaultWidth(int defaultWidth) {
		builder.setDefaultWidth(defaultWidth);
	}

}
