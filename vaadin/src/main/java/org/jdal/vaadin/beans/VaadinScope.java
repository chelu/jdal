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
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdal.vaadin.UIid;
import org.jdal.vaadin.VaadinUtils;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

import com.vaadin.server.ClientConnector.DetachEvent;
import com.vaadin.server.ClientConnector.DetachListener;
import com.vaadin.ui.UI;
import com.vaadin.util.CurrentInstance;

/**
 * Spring scope for Vaadin.
 * 
 * @author Jose Luis Martin
 */
public class VaadinScope implements Scope, DetachListener {
	
	public static final String SCOPE_NAME = "vaadin";
	private static final Log log = LogFactory.getLog(VaadinScope.class);
	private Map<String, Object> beans = Collections.synchronizedMap(new HashMap<String, Object>());
	private Map<String, Runnable> callbacks = Collections.synchronizedMap(new HashMap<String, Runnable>());
	private Set<UI> uis = Collections.synchronizedSet(new LinkedHashSet<UI>());
	
	/**
	 * {@inheritDoc}
	 */
	public Object get(String name, ObjectFactory<?> objectFactory) {
	
		String key = key(name);
		if (key != null) {
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
		
		throw new RuntimeException("No UI found in scope");
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
		Integer uiId = null;
		
		UI ui =  UI.getCurrent();
		if (ui == null) {
			UIid id = CurrentInstance.get(UIid.class);
			if (id != null) {
				uiId = id.getUiId();
			}
		}
		else if (ui != null) {
			if (uis.add(ui))
				ui.addDetachListener(this);
			
			uiId = ui.getUIId();
		}
		
		return uiId != null ? getConversationId(uiId) : null;
	}
	
	private String getConversationId(Integer id) {
		return VaadinUtils.getWindowName() + ":" + id.toString();
		
	}
	
	protected String key(String name) {
		String id = getConversationId();
		
		return id != null ? id + "_" + name : null;
	}

	private void removeBeans(UI ui) {
		Set<String> keys = beans.keySet();
		Iterator<String> iter = keys.iterator();
		
		while (iter.hasNext()) {
			String key = iter.next();
			if (key.startsWith(getConversationId(ui.getUIId()))) {
				iter.remove();
				if (log.isDebugEnabled())
					log.debug("Removed bean [" + key + "]");
				Runnable callback = callbacks.remove(key);
				if (callback != null) {
					callback.run();
				}
			}
		}
		
	}

	public synchronized void detach(DetachEvent event) {
		UI ui = (UI) event.getConnector();
		if (log.isDebugEnabled())
			log.debug("UI [" + ui.getUIId() + "] detached, destroying scoped beans");
		
		removeBeans(ui);
		uis.remove(ui);
		
	}
}
