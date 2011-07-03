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

import org.hibernate.mapping.Collection;
import org.vaadin.addon.customfield.CustomField;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

/**
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 *
 */
public class Selector extends CustomField implements ClickListener {
	private ListSelect available = new ListSelect();;
	private ListSelect selected = new ListSelect(); 
	private Button selectButton = new Button();
	private Button unselectButton = new Button();
	
	public Selector() {
		HorizontalLayout layout = new HorizontalLayout();
		layout.setSpacing(true);
		layout.addComponent(available);
		layout.addComponent(createButtons());
		layout.addComponent(selected);
		selectButton.addListener((ClickListener) this);
		selectButton.addListener((ClickListener) this);
		
		
		setCompositionRoot(layout);
	}

	/**
	 * @return
	 */
	private Component createButtons() {
		HorizontalLayout hl = new HorizontalLayout();
		hl.setSpacing(true);
		hl.addComponent(unselectButton);
		hl.addComponent(selectButton);
		
		return hl;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?> getType() {
		return Collection.class;
	}

	/**
	 * {@inheritDoc}
	 */
	public void buttonClick(ClickEvent event) {
	
		
	}
	

}
