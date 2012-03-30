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
package info.joseluismartin.vaadin.ui.form;

import info.joseluismartin.service.PersistentService;
import info.joseluismartin.vaadin.ui.FormUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Button;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;

/**
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 *
 */
@SuppressWarnings("unchecked")
public class FormEditor extends Form implements Editor {

	/** TableAction List */
	private List<FormAction> actions = new ArrayList<FormAction>();
	/** button panel */
	private HorizontalLayout buttonPanel = new HorizontalLayout();
	/** persistent service */
	private PersistentService<Object, Serializable> persistentService;
	
	/**
	 * Build layout and add configured ButtonListeners
	 */
	public void init() {
		for (FormAction a : actions) {
			a.setForm(this);
			Button b = FormUtils.newButton(a);
			buttonPanel.addComponent(b);
		}	
		buttonPanel.setSpacing(true);
		setFooter(buttonPanel);
		setWriteThrough(false);
		
	}

	/**
	 * {@inheritDoc}
	 */
	public void save() {
		commit();
		BeanItem<Object> entity = (BeanItem<Object>) getItemDataSource();
		persistentService.save(entity.getBean());
	}

	/**
	 * {@inheritDoc}
	 */
	public void cancel() {
		discard();
	}

	/**
	 * @return the actions
	 */
	public List<FormAction> getActions() {
		return actions;
	}

	/**
	 * @param actions the actions to set
	 */
	public void setActions(List<FormAction> actions) {
		this.actions = actions;
	}

	/**
	 * @return the persistentService
	 */
	public PersistentService<Object, Serializable> getPersistentService() {
		return persistentService;
	}

	/**
	 * @param persistentService the persistentService to set
	 */
	public void setPersistentService(PersistentService<Object, Serializable> persistentService) {
		this.persistentService = persistentService;
	}
}
