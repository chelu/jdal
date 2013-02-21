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
package org.jdal.vaadin.beans;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.jdal.vaadin.VaadinUtils;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

import com.vaadin.Application;

/**
 * Scope for Vaadin.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class VaadinScope implements Scope {
	
	Map<String, Map<String, Object>> beans = Collections.synchronizedMap(new HashMap<String, Map<String,Object>>());

	
	/**
	 * {@inheritDoc}
	 */
	public Object get(String name, ObjectFactory<?> objectFactory) {
		Application app = VaadinUtils.getApplication();
		Object bean = null;
		
		if (app == null)
			return objectFactory.getObject();
		
		Map<String, Object> applicationBeans = beans.get(app.getURL());
		
		if (applicationBeans != null) 
			bean = applicationBeans.get(name);
		
		return bean != null ? bean : objectFactory.getObject();
	}


	/**
	 * {@inheritDoc}
	 */
	public Object remove(String name) {
		Application app = VaadinUtils.getApplication();
		Object bean = null;
		
		if (app != null) {
			Map<String, Object> applicationBeans = beans.get(app.getURL().toString());
		
			if (applicationBeans != null) 
				bean = applicationBeans.remove(name);
		}
		
		return bean; 
	}

	/**
	 * {@inheritDoc}
	 */
	public void registerDestructionCallback(String name, Runnable callback) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * {@inheritDoc}
	 */
	public Object resolveContextualObject(String key) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getConversationId() {
		return VaadinUtils.getApplication().getURL().toString();
	}

}
