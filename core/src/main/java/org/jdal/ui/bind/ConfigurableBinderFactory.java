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
package org.jdal.ui.bind;


import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdal.logic.CollectionPersistenceService;
import org.jdal.ui.View;

/**
* Configurable Binder Factory that use a Map ComponentClass -> BinderClass
* to create Binders for Swing components
* 
* @author Jose Luis Martin - (jlm@joseluismartin.info)
*/
public class ConfigurableBinderFactory implements BinderFactory {
	private final static Log log = LogFactory.getLog(BinderFactory.class);
	private static BinderFactory defaultFactory;
	private ControlAccessorFactory controlAccessorFactory;
	private Map<Class<?>, Class<?extends PropertyBinder>> binders = 
			new Hashtable<Class<?>, Class<? extends PropertyBinder>>();
	private boolean mergeBinders = true;
	
	
	public ConfigurableBinderFactory() {
		this(ConfigurableControlAccessorFactory.getDefaultFactory());
	}
	
	public ConfigurableBinderFactory(ControlAccessorFactory controlAccessorFactory) {
		this.controlAccessorFactory = controlAccessorFactory;
		initDefaultBinders();
	}

	/**
	 * Try to find a binder for a Class, use super Class if none is configured.
	 * 
	 * @param clazz Class to looking for
	 * @return a Binder for that class or null if none
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public PropertyBinder getBinder(Class<?> clazz) {
		Class<?extends PropertyBinder> binderClass = null;
		PropertyBinder binder = null;
	
		binderClass = binders.get(clazz);
		
		if (binderClass == null) { // try with superclasses

			List superclasses = ClassUtils.getAllSuperclasses(clazz);
			superclasses.addAll(ClassUtils.getAllInterfaces(clazz));
			Iterator iter = superclasses.iterator();
		
			while (iter.hasNext() && binderClass == null)  {
				binderClass = binders.get(iter.next());
			}
		}
		
		if (binderClass  != null) {	
			try {
				binder = binderClass.newInstance();
			} catch (InstantiationException e) {
				log.error(e);
			} catch (IllegalAccessException e) {
				log.error(e);
			}
		}
		else {
			binder = new ControlBinder(controlAccessorFactory);
		}

		return binder;
	}
	
	private void initDefaultBinders() {
		binders.put(View.class, ViewBinder.class);
		binders.put(CollectionPersistenceService.class, CollectionPersistentServiceBinder.class);
		
	}

	// Getters and Setters

	public static synchronized BinderFactory getDefaultFactory() {
		if (defaultFactory == null) 
			defaultFactory = new ConfigurableBinderFactory();
		
		return defaultFactory;
	}
	
	public Map<Class<?>, Class<?extends PropertyBinder>> getBinders() {
		return binders;
	}

	public void setBinders(Map<Class<?>, Class<?extends PropertyBinder>> binders) {
		if (!mergeBinders)
			this.binders.clear();
		
		this.binders.putAll(binders);
	}

	/**
	 * @return the controlAccessorFactory
	 */
	public ControlAccessorFactory getControlAccessorFactory() {
		return controlAccessorFactory;
	}

	/**
	 * @param controlAccessorFactory the controlAccessorFactory to set
	 */
	public void setControlAccessorFactory(ControlAccessorFactory controlAccessorFactory) {
		this.controlAccessorFactory = controlAccessorFactory;
	}

	/**
	 * @return the mergeBinders
	 */
	public boolean isMergeBinders() {
		return mergeBinders;
	}

	/**
	 * @param mergeBinders the mergeBinders to set
	 */
	public void setMergeBinders(boolean mergeBinders) {
		this.mergeBinders = mergeBinders;
	}

}
