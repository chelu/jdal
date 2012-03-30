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
package info.joseluismartin.vaadin.ui.table;

import info.joseluismartin.service.PersistentService;
import info.joseluismartin.vaadin.ui.form.FormDialog;

import java.io.Serializable;

import org.springframework.beans.BeanUtils;
import org.springframework.context.MessageSource;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Form;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;

/**
 * Add new model to PageableTable
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class AddAction extends TableButtonListener {
	
	@Autowired
	private MessageSource messageSource;
	private boolean modal = true;
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void buttonClick(ClickEvent event) {
		final PageableTable<?> table = getTable();
		Form f = getTable().getEditorForm();
		Object bean = BeanUtils.instantiate(table.getEntityClass());
		
		if (f.getVisibleItemProperties() != null) {
			f.setItemDataSource(new BeanItem<Object>(bean), f.getVisibleItemProperties());
		}
		else {
			f.setItemDataSource(new BeanItem<Object>(bean));
		}
		
		FormDialog dialog = new FormDialog(f, "New " + table.getEntityClass().getSimpleName());
		dialog.setPersistentService((PersistentService<Object, Serializable>) table.getService());
		dialog.setMessageSource(messageSource);
		dialog.setModal(modal);
		dialog.init();
		dialog.addListener(new CloseListener() {
			
			public void windowClose(CloseEvent e) {
				table.refresh();
			}
		});
		table.getWindow().addWindow(dialog);
	}

	/**
	 * @return the messageSource
	 */
	public MessageSource getMessageSource() {
		return messageSource;
	}

	/**
	 * @param messageSource the messageSource to set
	 */
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	/**
	 * @return the modal
	 */
	public boolean isModal() {
		return modal;
	}

	/**
	 * @param modal the modal to set
	 */
	public void setModal(boolean modal) {
		this.modal = modal;
	}
}
