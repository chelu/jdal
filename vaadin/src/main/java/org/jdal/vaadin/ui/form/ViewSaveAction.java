/*
 * Copyright 2009-2012 the original author or authors.
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

import org.jdal.beans.StaticMessageSource;
import org.jdal.service.PersistentService;
import org.jdal.ui.View;
import org.jdal.vaadin.ui.table.ButtonListener;

import com.vaadin.ui.Button.ClickEvent;

/**
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 * @since 2.0
 */
@SuppressWarnings("rawtypes")
public class ViewSaveAction extends ButtonListener {
	
	
	private PersistentService persistentService;
	private View view;

	/**
	 * @param persistentService
	 * @param view
	 */
	public ViewSaveAction(PersistentService persistentService, View view) {
		super(StaticMessageSource.getMessage("ViewSaveAction.text"));
		this.persistentService = persistentService;
		this.view = view;
	}



	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void buttonClick(ClickEvent event) {
		view.update();
		
		if (view.validateView()) {
			persistentService.save(view.getModel());
		}
		
	}

}
