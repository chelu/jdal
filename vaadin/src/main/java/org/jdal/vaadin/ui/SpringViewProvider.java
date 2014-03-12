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


import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewProvider;
import com.vaadin.ui.Component;

/**
 * ViewProvider that delegates on a Spring BeanFactory to find Views
 * 
 * @author Jose Luis Martin
 * @since 2.0
 */
public class SpringViewProvider implements ViewProvider {
	
	@Autowired
	private BeanFactory beanFactory;
	private String defaultView = "mainView";

	@Override
	public String getViewName(String viewAndParameters) {
		return viewAndParameters;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public View getView(String viewName) {
		
		if (StringUtils.isEmpty(viewName))
			viewName = defaultView;
		
		Object view  =  beanFactory.getBean(viewName);
		
		Component c = null;
		
		if (view instanceof org.jdal.vaadin.ui.VaadinView) {
			c = ((VaadinView) view).getPanel();
		}
		else if (view instanceof Component) {
			c= (Component) view;
		}
		
		return (View) (c instanceof View ? c : new ComponentViewAdapter(c));
	}

	/**
	 * @return the beanFactory
	 */
	public BeanFactory getBeanFactory() {
		return beanFactory;
	}

	/**
	 * @param beanFactory the beanFactory to set
	 */
	public void setBeanFactory(BeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}

	public String getDefaultView() {
		return defaultView;
	}

	public void setDefaultView(String defaultView) {
		this.defaultView = defaultView;
	}


}
