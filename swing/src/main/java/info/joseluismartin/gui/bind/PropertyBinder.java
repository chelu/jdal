/*
 * Copyright 2002-2010 the original author or authors.
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
package info.joseluismartin.gui.bind;

import info.joseluismartin.gui.Binder;


/**
 * Bind a component on model via property name
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public interface PropertyBinder extends Binder<Object> {
	
	/**
	 * Bind changes on component to a model property
	 * @param component component to bind
	 * @param propertyName the property name to bind
	 * @param model model to bind.
	 */
	void bind(Object component, String propertyName, Object model);
	
	/**
	 * Bind changes on component to a model property
	 * @param component component to bind
	 * @param propertyName the property name to bind
	 * @param model model to bind.
	 * @param readOnly if true, the binding is readOnly, ie from model to control.
	 */
	void bind(Object component, String propertyName, Object model, boolean readOnly);

	
	/**
	 * @return component
	 */
	 Object getComponent();
	 
	 /**
	  * return property name
	  * @return
	  */
	 String getPropertyName();
}
