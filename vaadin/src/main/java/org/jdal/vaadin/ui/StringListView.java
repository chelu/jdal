/*
 * Copyright 2009-2014 Jose Luis Martin
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jdal.vaadin.ui.AbstractView;
import org.jdal.vaadin.ui.FormUtils;
import org.jdal.vaadin.ui.form.BoxFormBuilder;
import org.jdal.vaadin.ui.form.SimpleBoxFormBuilder;
import org.jdal.vaadin.ui.table.ButtonListener;

import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.TextField;

/**
 * View for a List of Strings. Use a {@link ListSelect} to show the list
 * and a {@link TextField} with buttons for editing,
 * 
 * @author Jose Luis Martin
 * @since 2.1
 */
public class StringListView extends AbstractView<List<String>> implements Serializable {
	
	private ListSelect listSelect = new ListSelect();
	private Button addButton;
	private Button removeButton;
	private TextField textField = FormUtils.newTextField();
	private Label caption = new Label();
	
	public StringListView() {
		this(false);
	}
	
	public StringListView(boolean useNativeButtons) {
		addButton = FormUtils.newButton(new AddAction(), useNativeButtons);
		removeButton = FormUtils.newButton(new RemoveAction(), useNativeButtons);
		setModel(new ArrayList<String>());
	}

	public boolean removeItem(String item) {
		return getModel().remove(item);
	}
	
	public boolean add(String item) {
		return getModel().add(item);
	}

	@Override
	protected Component buildPanel() {
		BoxFormBuilder fb = new BoxFormBuilder();
	
		fb.row();
		fb.add(caption);
		fb.row(BoxFormBuilder.SIZE_FULL);
		fb.add(listSelect, SimpleBoxFormBuilder.SIZE_FULL);
		fb.row();
		fb.startBox();
		fb.row();
		fb.add(textField, SimpleBoxFormBuilder.SIZE_FULL);
		fb.add(addButton, 48);
		fb.add(removeButton, 48);
		fb.endBox();
		
		return fb.getForm();		
	}
	
	public void setCaption(String caption) {
		this.caption.setValue(caption);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void doUpdate() {
		Collection<String> items = (Collection<String>) listSelect.getItemIds();
		getModel().clear();
		
		for (String item : items)
			getModel().add(item);
	}

	@Override
	protected void doRefresh() {
		listSelect.removeAllItems();
		
		for (String item : getModel()) {
			listSelect.addItem(item);
		}
	}
	
	private class AddAction extends ButtonListener {
		
		public AddAction() {
			setIcon(new ThemeResource("images/16x16/list-add.png"));
		}

		@Override
		public void buttonClick(ClickEvent event) {
			String value = textField.getValue();
			
			if (StringUtils.isNotEmpty(value)) {
				String[] items = value.split(",");
				
				for (String item : items) {
					if (StringUtils.isNotEmpty(item)) {
						listSelect.addItem(item.trim());
						textField.setValue("");
					}
				}
			}
		}
		
	}
	
	private class RemoveAction extends ButtonListener {
		
		public RemoveAction() {
			setIcon(new ThemeResource("images/16x16/list-remove.png"));
		}

		@Override
		public void buttonClick(ClickEvent event) {
			Object value = listSelect.getValue();
			
			if (value != null)
				listSelect.removeItem(value);
		}
		
	}

}
