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

import java.io.Serializable;

import com.vaadin.server.Resource;
import com.vaadin.ui.Component;

/**
 * Vaadin Component Holder
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class VaadinComponentHolder implements ComponentHolder, Serializable {

	private String name;
	private Resource icon;
	private Component component;
	
	/**
	 * {@inheritDoc}
	 */
	public String getName() {
		return name;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * {@inheritDoc}
	 */
	public Resource getIcon() {
		return icon;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setIcon(Resource icon) {
		this.icon = icon;
	}

	/**
	 * {@inheritDoc}
	 */
	public Component getComponent() {
		return component;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setComponent(Component component) {
		this.component = component;
	}
}
