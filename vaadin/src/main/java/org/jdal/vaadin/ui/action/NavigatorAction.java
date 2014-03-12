/*
 * Copyright 2009-2014 the original author or authors.
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
package org.jdal.vaadin.ui.action;

import org.jdal.vaadin.ui.table.ButtonListener;

import com.vaadin.navigator.Navigator;
import com.vaadin.server.Resource;
import com.vaadin.ui.Button.ClickEvent;

/**
 * Action that fire a navigation page change.
 * 
 * @author Jose Luis Martin
 * @since 2.0
 */
public class NavigatorAction extends ButtonListener {

	/** Navigator to use */
	private Navigator navigator;
	/** View name to navigate to on clicks */
	private String viewName;

	
	public NavigatorAction() {
		super();
	}

	public NavigatorAction(String caption, Resource icon) {
		super(caption, icon);
	}

	public NavigatorAction(String caption) {
		super(caption);
	}
	
	public NavigatorAction(String caption, Resource icon, String viewName) {
		super(caption, icon);
		this.viewName = viewName;
	}

	@Override
	public void buttonClick(ClickEvent event) {
		if (navigator != null)
			navigator.navigateTo(viewName);
	}

	public Navigator getNavigator() {
		return navigator;
	}

	public void setNavigator(Navigator navigator) {
		this.navigator = navigator;
	}

	public String getViewName() {
		return viewName;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
	}
	
}
