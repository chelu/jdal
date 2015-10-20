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
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;


/**
 * A FormBuilder that create form using Vertical and Horizontal layouts.
 * Add components using a implicit cursor.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class BoxFormBuilder {
	
	static public int SIZE_FULL = SimpleBoxFormBuilder.SIZE_FULL;
	static public int SIZE_UNDEFINED = SimpleBoxFormBuilder.SIZE_UNDEFINED;
	
	/** hold form builders */
	private Stack<SimpleBoxFormBuilder> stack = new Stack<SimpleBoxFormBuilder>();
	/** current form builder */
	private SimpleBoxFormBuilder builder;
	/** use margins */
	private boolean margin = true;
	private int defaultWidth;
	
	public boolean isMargin() {
		return margin;
	}

	public void setMargin(boolean margin) {
		this.margin = margin;
	}

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
		SimpleBoxFormBuilder old = builder;
		stack.push(builder);
		builder = new SimpleBoxFormBuilder();
		builder.setDefaultWidth(this.defaultWidth);
		builder.setDebug(old.isDebug());
		builder.setDefaultRowHeight(old.getDefaultRowHeight());
		builder.setSpacing(builder.isSpacing());

		if (old.isUseTabIndex()) {
			builder.setUseTabIndex(old.isUseTabIndex());
			builder.setTabIndex(old.getTabIndex());
		}
	}
	
	
	/**
	 * Ends a Box and add to current cursor position.
	 * @return the box component.
	 */
	public Component endBox() {
		Component c = builder.getForm();
		int height = builder.getFormHeight();
		SimpleBoxFormBuilder old = builder;
		builder = stack.pop();
		builder.add(c);
		builder.setHeight(height);
		
		if (old.isUseTabIndex()) {
			builder.setTabIndex(old.getTabIndex());
		}
		
		return c;
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
	
	public void add(Component c, String label) {
		builder.add(c, label);
	}

	public void add(Component c, String label, int width) {
		builder.add(c, label, width);
	}

	public void add(Component c, String label, Alignment alignment) {
		builder.add(c, label, alignment);
	}

	public void add(Component c, String label, int width, Alignment alignment) {
		builder.add(c, label, width, alignment);
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
		HorizontalLayout form = (HorizontalLayout) builder.getForm();
		form.setMargin(margin);
		
		return form;
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
		return this.defaultWidth;
	}

	public void setDefaultWidth(int defaultWidth) {
		this.defaultWidth = defaultWidth;
		builder.setDefaultWidth(defaultWidth);
	}

	public void addGlue() {
		builder.addGlue();
	}

	public void addVerticalGlue() {
		builder.addVerticalGlue();
		
	}
	
	public void addHorizontalGlue() {
		builder.addHorizontalGlue();
	}
	
	public void setFixedHeight() {
		builder.setFixedHeight(true);
	}

	public boolean isDebug() {
		return builder.isDebug();
	}

	public void setDebug(boolean debug) {
		builder.setDebug(debug);
	}
	
	public void setTabIndex(int tabIndex) {
		builder.setTabIndex(tabIndex);
	}
	
	public int getTabIndex() {
		return builder.getTabIndex();
	}

	public boolean isUseTabIndex() {
		return builder.isUseTabIndex();
	}

	public void setUseTabIndex(boolean useTabIndex) {
		builder.setUseTabIndex(useTabIndex);
	}

}
