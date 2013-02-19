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

import org.jdal.dao.Filter;
import org.jdal.ui.View;

import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Form;
import com.vaadin.ui.Window.Notification;


/**
 * Actionf to apply the filter on PageableTable
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 * @since 1.1
 */
public class FindAction extends TableButtonListener {
	
	public FindAction() {
		setIcon(new ThemeResource("images/table/edit-find.png"));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void buttonClick(ClickEvent event) {
		Object filterForm = getTable().getFilterForm();
		
		if (filterForm != null) {
			if (filterForm instanceof Form) {
				Form f = (Form) filterForm;
				f.commit();
			}
			else if (filterForm instanceof View) {
				View<Filter> view = (View<Filter>) filterForm;
				view.update();
				if (!view.validateView()) {
					getTable().getApplication().getMainWindow()
					.showNotification(view.getErrorMessage(), Notification.TYPE_ERROR_MESSAGE);
				}
			}
			getTable().firstPage();
		}
	}
}
