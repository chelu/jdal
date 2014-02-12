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
 * Interface to create Vaadin Components.
 * 
 * @author Jose Luis Martin
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
	
	ViewDialog newViewDialog(VaadinView<?> view);
}
