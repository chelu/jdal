/*
 * Copyright 2008-2011 the original author or authors.
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
package info.joseluismartin.gui.bind;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Configurable Binder Factory that use a Map ComponentClass -> BinderClass
 * to create Binders for Swing components
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class BinderFactory  {
	
	private final static Log log = LogFactory.getLog(BinderFactory.class);
	private Map<Class<?>, Class<PropertyBinder>> binders;
	
	/**
	 * Try to find a binder for a Class, use super Class if none is configured.
	 * 
	 * @param clazz Class to looking for
	 * @return a Binder for that class or null if none
	 */
	@SuppressWarnings("unchecked")
	public PropertyBinder getBinder(Class<?> clazz) {
		Class<PropertyBinder> binderClass = null;
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
			log.warn("Can't find a Binder for class: " + clazz.getName());
		}

		return binder;
	}

	// Getters and Setters
	
	public Map<Class<?>, Class<PropertyBinder>> getBinders() {
		return binders;
	}

	public void setBinders(Map<Class<?>, Class<PropertyBinder>> binders) {
		this.binders = binders;
	}
}
