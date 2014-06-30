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
import org.jdal.vaadin.ui.VaadinView;

import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification;


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
	@Override
	public void buttonClick(ClickEvent event) {
		PageableTable<?> table = (PageableTable<?>) getTable();
		VaadinView<Filter> filterView = table.getFilterForm();
		
		if (filterView != null) {
			filterView.update();
			
			if (!filterView.validateView()) {
					Notification.show(filterView.getErrorMessage(), Notification.Type.ERROR_MESSAGE);
			}
		}
		table.firstPage();
	}
}
