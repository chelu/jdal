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
package org.jdal.vaadin.ui.form;

import org.jdal.vaadin.ui.VaadinView;
import org.jdal.vaadin.ui.table.ButtonListener;

import com.vaadin.server.Resource;

/**
 * Base class for View Actions.
 * 
 * @author Jose Luis Martin
 */
public abstract class ViewAction extends ButtonListener {
	
	public ViewAction() {
		super();
	}

	public ViewAction(String caption, Resource icon) {
		super(caption, icon);
	}

	public ViewAction(String caption) {
		super(caption);
	}

	private VaadinView<?> view;

	/**
	 * @return the form
	 */
	public VaadinView<?> getView() {
		return view;
	}

	/**
	 * @param form the form to set
	 */
	public void setView(VaadinView<?> view) {
		this.view = view;
	}
}
