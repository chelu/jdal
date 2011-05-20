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
package info.joseluismartin.gui.bind;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ReflectionUtils;

/**
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 *
 */
public class ConfigurableControlAccesorFactory implements ControlAccessorFactory {
	
	private final static Log log = LogFactory.getLog(ConfigurableControlAccesorFactory.class);
	private Map<Class<?>, Class<ControlAccessor>> accessors;

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public ControlAccessor getControlAccessor(Object control) {
		Class<ControlAccessor> accessorClass = null;
		ControlAccessor accessor = null;
		Class<?extends Object> clazz = control.getClass();
	
		accessorClass = accessors.get(clazz);
		
		if (accessorClass == null) { // try with superclasses

			List superclasses = ClassUtils.getAllSuperclasses(clazz);
			superclasses.addAll(ClassUtils.getAllInterfaces(clazz));
			Iterator iter = superclasses.iterator();
		
			while (iter.hasNext() && accessorClass == null)  {
				accessorClass = accessors.get(iter.next());
			}
		}
		
		if (accessorClass  != null) {	
			try {
				Constructor<ControlAccessor> ctor = accessorClass.getConstructor(Object.class);
				accessor = ctor.newInstance(control);
			} catch (InstantiationException e) {
				log.error(e);
			} catch (IllegalAccessException e) {
				log.error(e);
			} catch (SecurityException e) {
				log.error(e);
			} catch (NoSuchMethodException e) {
				log.error(e);
			} catch (IllegalArgumentException e) {
				log.error(e);
			} catch (InvocationTargetException e) {
				log.error(e);
			}
		}
		else {
			log.warn("Can't find a accessor for class: " + clazz.getName());
		}

		return accessor;
	}

	// Getters and Setters
	
	public Map<Class<?>, Class<ControlAccessor>> getAccessors() {
		return accessors;
	}

	/**
	 * @param accessors the accessors to set
	 */
	public void setAccessors(Map<Class<?>, Class<ControlAccessor>> accessors) {
		this.accessors = accessors;
	}

}
