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
package org.jdal.vaadin.ui.table;

import com.vaadin.terminal.Resource;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

/**
 * A Swing like Action. Holds caption, icon and handler
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public abstract class ButtonListener implements ClickListener {

	private String caption;
	private String description;
	private Resource icon;
	private String defaultIcon;
	
	
	/**
	 * Creates new ButtonListener
	 */
	public ButtonListener() {
		this("");
	}

	/**
	 * Creates a new ButtonListener
	 * @param caption button caption
	 */
	public ButtonListener(String caption) {
		this(caption, null);
	}

	/**
	 * Creates a new ButtonListener
	 * @param caption the button caption
	 * @param icon the button icon
	 */
	public ButtonListener(String caption, Resource icon) {
		this.caption = caption;
		this.icon = icon;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public abstract void buttonClick(ClickEvent event);

	/**
	 * @return the caption
	 */
	public String getCaption() {
		return caption;
	}

	/**
	 * @param caption the caption to set
	 */
	public void setCaption(String caption) {
		this.caption = caption;
	}

	/**
	 * @return the icon
	 */
	public Resource getIcon() {
		return icon;
	}

	/**
	 * @param icon the icon to set
	 */
	public void setIcon(Resource icon) {
		this.icon = icon;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the defaultIcon
	 */
	public String getDefaultIcon() {
		return defaultIcon;
	}

	/**
	 * @param defaultIcon the defaultIcon to set
	 */
	public void setDefaultIcon(String defaultIcon) {
		this.defaultIcon = defaultIcon;
	}


}
