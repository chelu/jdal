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

import org.jdal.vaadin.ui.form.ViewDialog;

import com.vaadin.ui.Component;

/**
 * Interface for creating Vaadin Components. 
 * 
 * @author Jose Luis Martin
 * @since 1.0
 */
public interface GuiFactory {
	
	/**
	 * Gets vaadin Component by name
	 * @param name component name
	 * @return Component
	 */
	Component getComponent(String name);
	
	/**
	 * Gets vaadin view by name
	 * @param name view name
	 * @return VaadinView
	 */
	VaadinView<?> getView(String name);
	
	/**
	 * Get a View instance by class
	 * @param clazz class instance
	 * @return a instance of class
	 */
	<T> VaadinView<T> getView(Class<?extends VaadinView<T>> clazz);
	
	/**
	 * get a View instance by Class and name
	 * @param name the view name
	 * @param type the view type
	 * @return the Vaadin View.
	 */
	<T> VaadinView<T> getView(String name, Class<?extends VaadinView<T>> type);
	
	/**
	 * Create a new ViewDialog instance for a {@link VaadinView}
	 * @param view the view 
	 * @return the Dialog showing the View
	 */
	ViewDialog newViewDialog(VaadinView<?> view);
	
	/**
	 * Create a new ViewDialog instance for a {@link VaadinView} by name
	 * @param view the view 
	 * @param name the dialog name
	 * @return a new {@link ViewDialog} showing the view.
	 */
	ViewDialog newViewDialog(VaadinView<?> view, String name);
}
