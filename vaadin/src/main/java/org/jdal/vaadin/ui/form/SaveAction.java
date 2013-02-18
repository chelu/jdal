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

import java.io.Serializable;

import javax.annotation.Resource;

import org.jdal.beans.StaticMessageSource;
import org.jdal.service.PersistentService;

import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Form;
import com.vaadin.ui.Window;

/**
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 *
 */
public class SaveAction extends FormAction {
	
	@Resource
	private PersistentService<Object, Serializable> persistentService;
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void buttonClick(ClickEvent event) {
		Form form = getForm();
		form.commit();
		BeanItem<Object> entity = (BeanItem<Object>) form.getItemDataSource();
		persistentService.save(entity.getBean());
		doSave();
		
		Window parent = form.getWindow().getParent();
		
		if (parent != null) {
			if (form.getWindow().isClosable())
				form.getWindow().getParent().removeWindow(form.getWindow());
			
			parent.showNotification(StaticMessageSource.getMessage("formAction.saved_successfuly"));
		}
	}

	/**
	 * Let subclasses do something on save
	 */
	protected void doSave() {
		
	}
}
