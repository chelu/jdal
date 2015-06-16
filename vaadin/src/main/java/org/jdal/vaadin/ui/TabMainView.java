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
package org.jdal.vaadin.ui;

import org.jdal.beans.MessageSourceWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * Base class for main views using a {@link TabSheet}
 * 
 * @author Jose Luis Martin
 * @since 2.1
 */
public class TabMainView extends TabSheet implements View {
	
	private MessageSourceWrapper messageSource = new MessageSourceWrapper();

	public TabMainView() {
		setStyleName(Reindeer.TABSHEET_MINIMAL);
	}
	
	/**
	 * Create a new {@link VerticalLayout}
	 * @return a new VerticalLayout.
	 */
	protected VerticalLayout createVerticalLayout() {
		VerticalLayout vl = new VerticalLayout();
		vl.setMargin(true);
		vl.setSizeFull();
		vl.setSpacing(true);
		
		return vl;
	}
	
	/**
	 * Add a component as Tab wrapped in a {@link VerticalLayout}
	 * @param component component to add
	 * @param caption message code for caption.
	 */
	public void addTabComponent(Component component, String caption) {
		VerticalLayout vl = createVerticalLayout();
		component.setSizeFull();
		vl.addComponent(component);
		addTab(vl, messageSource.getMessage(caption));
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		// do nothing by default
	}
	
	public String getMessage(String code) {
		return messageSource.getMessage(code);
	}
	
	public MessageSource getMessageSource() {
		return this.messageSource.getMessageSource();
	}
	
	@Autowired
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource.setMessageSource(messageSource);
	}

}