/*
 * Copyright 2009-2011 Jose Lus Martin.
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
package org.jdal.swing;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;

import org.jdal.ui.View;

/**
 * Factory interface for creating visual components.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public interface GuiFactory {
	
	/**
	 * Get a JComponent reference of named widget
	 * @param name the name of component
	 * @return the component or null if not exist or not a JComponent
	 */
	JComponent getComponent(String name);
	
	/**
	 * Get a JPanel reference of named widget.
	 * @param name the name of component
	 * @return the component or null if not exist or not a JPanel
	 */
	JPanel getPanel(String name);
	
	/**
	 * Gets a JDialog reference of named widget
	 * @param name the name of component
	 * @return the component or null if not exist or not is a JDialog
	 */
	JDialog getDialog(String name);
	
	/**
	 * Gets a Object reference of named widget
	 * @param name the name of component
	 * @return the component or null if not exist.
	 */
	Object getObject(String name);
	
	Object getObject(String name, Object[] params);
	
	/**
	 * Gets a named view instance
	 * @param name the view name
	 * @return the view or null if none.
	 */
	View<?> getView(String name);
	
	/** 
	 * Gets a named view instance.
	 * @param name view name
	 * @param clazz view class template
	 * @return the view or null if none.
	 */
	public <T> View<T> getView(String name, Class<T> clazz);

}
