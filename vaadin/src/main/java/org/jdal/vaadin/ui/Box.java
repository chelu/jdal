/*
 * Copyright 2009-2011 the original author or authors.
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
package org.jdal.vaadin.ui;

import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * Utility class for work with box layouts
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public abstract class Box {
	
	/**
	 * Try to imitate a HorizalGlue of Swing BoxLayout.
	 * A invisible component that get all extra space.
	 * @param layout layout to add glue
	 */
	public static void addHorizontalGlue(AbstractOrderedLayout layout) {
		Label label = new Label();
		label.setWidth("100%");
		layout.addComponent(label);
		layout.setExpandRatio(label, 1.0f);
	}
	
	/**
	 * Try to imitate a VerticalGlue of Swing BoxLayout
	 */
	public static void addVerticalGlue(AbstractOrderedLayout layout) {
		Label label = new Label(" ");
		label.setHeight("100%");
		layout.addComponent(label);
		layout.setExpandRatio(label, 1.0f);
	}
	/**
	 * Try to imitate HorizontalStruct on Swing BoxLayout
	 * @param layout Layout to add the struct
	 * @param width struct withd
	 */
	public static void addHorizontalStruct(AbstractOrderedLayout layout, int width) {
		Label label = new Label();
		label.setWidth(width + "px");
		layout.addComponent(label);
		layout.setExpandRatio(label, 0f);
	}

	/**
	 * Try to imitate VerticalStruct of Swing BoxLayout
	 * @param layout  layout to add struct
	 * @param height struct height
	 */
	public static void addVerticalStruct(AbstractOrderedLayout layout, int height) {
		Label label = new Label();
		label.setHeight(height + "px");
		layout.addComponent(label);
		layout.setExpandRatio(label, 0f);
	}

	/**
	 * @return new VerticalLayout
	 */
	public static VerticalLayout createVerticalBox() {
		 VerticalLayout vl = new VerticalLayout();
		 vl.setHeight(100, Unit.PERCENTAGE);
		
		 return vl;
	}

	/**
	 * @param defaultSpace
	 * @return new struct 
	 */
	public static Component createHorizontalStrut(int defaultSpace) {
		Label label = new Label(" ");
		label.setWidth(defaultSpace, Unit.PIXELS);
		return label;
	}

	/**
	 * @return new Horizontal Layout
	 */
	public static HorizontalLayout createHorizontalBox() {
		return new HorizontalLayout();
	}

}
