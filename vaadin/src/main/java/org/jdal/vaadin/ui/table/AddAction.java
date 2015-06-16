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

import org.jdal.util.BeanUtils;
import org.jdal.vaadin.ui.VaadinView;
import org.jdal.vaadin.ui.form.ViewDialog;
import org.springframework.beans.factory.annotation.Configurable;

import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;

/**
 * Add new model to PageableTable
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
@Configurable
public class AddAction extends TableButtonListener {

	private boolean modal = false;
	
	public AddAction() {
		setIcon(new ThemeResource("images/table/filenew.png"));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void buttonClick(ClickEvent event) {
		final TableComponent<?> table = getTable();
		VaadinView view = table.getEditorView();
		Object bean = BeanUtils.instantiate(table.getEntityClass());
		view.setModel(bean);
		ViewDialog dialog = table.getGuiFactory().newViewDialog(view);
		dialog.setModal(this.modal);
		dialog.addCloseListener(new CloseListener() {

			public void windowClose(CloseEvent e) {
				table.refresh();
			}
		});
		table.getUI().addWindow(dialog);
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
