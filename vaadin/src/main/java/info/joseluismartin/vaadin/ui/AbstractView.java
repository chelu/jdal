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
package info.joseluismartin.vaadin.ui;

import com.vaadin.ui.Component;

/**
 * <p>
 * Base class for Views. Subclases must implements buildPanel()  method
 * to create the Vaadin component of the view.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public abstract class AbstractView<T> implements View<T> {
	
	Component component;
	T model;

	/**
	 * Build the Component of view.
	 * @return
	 */
	protected abstract Component buildPanel();


	/**
	 * {@inheritDoc}
	 */
	public Component getComponent() {
		if (component == null) 
			component = buildPanel();
		
		return component;
	}

	/**
	 * {@inheritDoc}
	 */
	public T getModel() {
		return model;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setModel(T model) {
		this.model = model;
	}
}
