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

import org.jdal.ui.Editor;
import org.jdal.vaadin.VaadinUtils;

import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window;

/**
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 *
 */
public class CancelAction extends ViewAction {
	
	private static final String DEFAULT_ICON = "images/cancel.png";
	
	public CancelAction() {
		setIcon(new ThemeResource(DEFAULT_ICON));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void buttonClick(ClickEvent event) {
	
		Window window = VaadinUtils.getWindow(getView().getPanel());
		
		if (window != null)
			getView().getPanel().getUI().removeWindow(window);
		
		if (getView() instanceof Editor) {
			((Editor<?>) getView()).cancel();
		}
	}
}
