/*
 * Copyright 2009-2014 Jose Luis Martin.
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

import org.jdal.beans.StaticMessageSource;
import org.jdal.vaadin.ui.form.BoxFormBuilder;
import org.jdal.vaadin.ui.form.SimpleBoxFormBuilder;
import org.jdal.vaadin.ui.table.ButtonListener;

import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

/**
 * Window to show {@link Component}s like dialogs.
 * 
 * @author Jose Luis Martin
 * @since 2.0
 */
public class Dialog extends Window {

	private static final long serialVersionUID = 1L;

	private Component component;
	private CloseAction closeAction = new CloseAction();
	private Button acceptButton;


	public Dialog() {

	}

	public Dialog(Component component) {
		this.component = component;
		init();
	}

	public void init() {
		closeAction.setCaption(StaticMessageSource.getMessage("Accept"));

		HorizontalLayout wrapper = new HorizontalLayout();
		wrapper.addComponent(component);

		BoxFormBuilder fb = new BoxFormBuilder();
		fb.row();
		fb.setElastic();
		fb.add(wrapper, SimpleBoxFormBuilder.SIZE_FULL);
		fb.row();
		fb.add(createButtonBox());

		this.setContent(fb.getForm());
	}

	/**
	 * Create box with accept/cancel buttons
	 * @return new button box
	 */
	protected Component createButtonBox() {
		HorizontalLayout hl = new HorizontalLayout();
		hl.setSizeUndefined();
		Box.addHorizontalGlue(hl);
		acceptButton = FormUtils.newButton(closeAction);
		hl.addComponent(acceptButton);
		Box.addHorizontalGlue(hl);

		return hl;
	}
	
	public void show() {
		UI.getCurrent().addWindow(this);
	}
	
	public Component getComponent() {
		return component;
	}

	public void setComponent(Component component) {
		this.component = component;
	}
	
	private class CloseAction extends ButtonListener {
		private static final String DEFAULT_ICON = "images/ok.png";
		
		public CloseAction() {
			setIcon(new ThemeResource(DEFAULT_ICON));
		}

		@Override
		public void buttonClick(ClickEvent event) {
			UI.getCurrent().removeWindow(Dialog.this);
		}
	}
}

