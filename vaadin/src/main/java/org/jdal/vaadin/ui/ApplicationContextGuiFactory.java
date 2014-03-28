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
package org.jdal.vaadin.ui;

import java.io.Serializable;

import org.jdal.vaadin.ui.form.ViewDialog;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

import com.vaadin.ui.Component;

/**
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 *
 */
public class ApplicationContextGuiFactory implements GuiFactory, BeanFactoryAware, Serializable {
	
	public static final String VIEW_DIALOG = "viewDialog";
	
	protected BeanFactory beanFactory;

	/**
	 * {@inheritDoc}
	 */
	public Component getComponent(String name) {
		return (Component) beanFactory.getBean(name);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	/**
	 * {@inheritDoc}
	 */
	public VaadinView<?> getView(String name) {
		return (VaadinView<?>) beanFactory.getBean(name);
	}
	
	@SuppressWarnings("rawtypes")
	public ViewDialog newViewDialog() {
		ViewDialog dlg = null;
		try {
			dlg = beanFactory.getBean(VIEW_DIALOG, ViewDialog.class);
		}
		catch(BeansException be)  {
			
		}
		
		if (dlg == null) {
			dlg = new ViewDialog();
		}
		
		return dlg;
	}

	public ViewDialog newViewDialog(VaadinView<?> view) {
		ViewDialog dlg =  newViewDialog();
		dlg.setView(view);
		dlg.init();
		
		return dlg;
	}

}
