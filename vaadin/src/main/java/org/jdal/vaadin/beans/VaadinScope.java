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

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdal.vaadin.VaadinUtils;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

import com.vaadin.ui.UI;

/**
 * Spring scope for Vaadin.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class VaadinScope implements Scope {
	
	public static final String SCOPE_NAME = "vaadin";
	private static final Log log = LogFactory.getLog(VaadinScope.class);
	private Map<String, Object> beans = Collections.synchronizedMap(new HashMap<String, Object>());
	private Map<String, Runnable> callbacks = Collections.synchronizedMap(new HashMap<String, Runnable>());
	
	/**
	 * {@inheritDoc}
	 */
	public Object get(String name, ObjectFactory<?> objectFactory) {
	
		String key = key(name);
		Object bean = beans.get(key);
		
		if (bean == null) {
			if (log.isDebugEnabled()) {
				log.debug("Bean not found in scope: [" + key + "]. Creating new one");
			}
			bean = objectFactory.getObject();
			beans.put(key, bean);
		}
		else {
			if (log.isDebugEnabled()) {
				log.debug("Bean found in scope: [" + key + "]");
			}
		}

		return bean;
	}


	/**
	 * {@inheritDoc}
	 */
	public Object remove(String name) {
		return beans.remove(key(name));
	}

	/**
	 * {@inheritDoc}
	 */
	public void registerDestructionCallback(String name, Runnable callback) {
		callbacks.put(key(name), callback);
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
		return VaadinUtils.getSession().getId() + "_" + UI.getCurrent().toString();
	}
	
	protected String key(String name) {
		return getConversationId() + "_" + name;
	}

	private void removeBeans(String prefix) {
		for (String key : beans.keySet()) {
			if (key.startsWith(prefix)) {
				beans.remove(key);
				if (log.isDebugEnabled())
					log.debug("Removed bean [" + key + "]");
				Runnable callback = callbacks.remove(key);
				if (callback != null)
					callback.run();
			}
		}
	}


	/**
	 * @param session
	 */
	public synchronized void onSessionClose(HttpSession session) {
		if (log.isDebugEnabled()) 
			log.debug("Sesssion closing, destroying scoped beans");
			
		removeBeans(session.getId());
	}
}
