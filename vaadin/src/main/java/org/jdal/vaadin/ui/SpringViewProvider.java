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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdal.annotation.SerializableProxy;
import org.jdal.auth.AuthService;
import org.jdal.vaadin.annotation.ViewConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.access.AccessDeniedException;
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
	
	private static final Log log = LogFactory.getLog(SpringViewProvider.class);
	@Autowired
	private ConfigurableListableBeanFactory beanFactory;
	@SerializableProxy
	@Autowired(required = false)
	private AuthService authService;
	
	private String defaultView = "mainView";
	/** Maps navigator views to view bean names */
	private Map<String, String> viewNames = new ConcurrentHashMap<String, String>();
	/** Maps view bean names to security access expressions */
	private Map<String, String> viewAccess = new ConcurrentHashMap<String, String>();
	
	/** 
	 * Parse @VaadinView annotations and craate view mappings.
	 */
	@PostConstruct
	public void init() {
		String[] beanNames = this.beanFactory.getBeanNamesForAnnotation(ViewConfig.class);
		
		for (String name : beanNames) {
			Class<?> viewClass = this.beanFactory.getType(name);
			ViewConfig vc = AnnotationUtils.getAnnotation(viewClass, ViewConfig.class);
			if (!StringUtils.isEmpty(vc.value()) && !this.viewNames.containsKey(vc.value())) {
				if (log.isDebugEnabled())
					log.debug("Mapping navigator view [" + vc.value() + "] to bean name [" + name + "]");
				
				this.viewNames.put(vc.value(), name);
				
				if (!StringUtils.isEmpty(vc.access()))
					this.viewAccess.put(name, vc.access());
			}
			else {
				if (log.isDebugEnabled()) 
					log.debug("Skipping already registered navigator view ["+ vc.value() + "]");
			}
		}
	}
	
	
	@Override
	public String getViewName(String viewAndParameters) {
		if (this.viewNames.containsKey(viewAndParameters))
			return viewNames.get(viewAndParameters);
		
		return viewAndParameters;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public View getView(String viewName) {
		
		if (StringUtils.isEmpty(viewName))
			viewName = defaultView;
		
		Class<?> viewClass = beanFactory.getType(viewName);
		checkAccess(viewClass, viewName);
		
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
	 * @param viewName
	 */
	private void checkAccess(Class<?> viewClass, String viewName) {
		if (this.authService == null)
			return;
		
		String access = this.viewAccess.get(viewName);
		
		if (access == null) {
			// lookup annotation and cache result
			ViewConfig vc = AnnotationUtils.findAnnotation(viewClass, ViewConfig.class);
			access = vc == null ? "" : vc.access();
			this.viewAccess.put(viewName, access);
		}
			
		if (!StringUtils.isEmpty(access) && !this.authService.checkAccess(viewClass, access, null))
			throw new AccessDeniedException("Access is Denied");
	}

	/**
	 * @return the beanFactory
	 */
	public ConfigurableListableBeanFactory getBeanFactory() {
		return beanFactory;
	}

	/**
	 * @param beanFactory the beanFactory to set
	 */
	public void setBeanFactory(ConfigurableListableBeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}

	public String getDefaultView() {
		return defaultView;
	}

	public void setDefaultView(String defaultView) {
		this.defaultView = defaultView;
	}
	
	public AuthService getAuthService() {
		return authService;
	}

	public void setAuthManager(AuthService authService) {
		this.authService = authService;
	}

}
