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

import info.joseluismartin.gui.Selector;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JList;
import javax.swing.JToggleButton;
import javax.swing.text.JTextComponent;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.freixas.jcalendar.JCalendarCombo;

/**
 * Implementation of ControlAccessorFactory using an asociative Map&lt;Class, ControlAccessor&gt;
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 * @since 1.1
 * @see info.joseluismartin.gui.bind.ControlAccessor
 * @see info.joseluismartin.gui.bind.ControlAccessorFactory
 */
public class ConfigurableControlAccessorFactory implements ControlAccessorFactory {
	
	private final static Log log = LogFactory.getLog(ConfigurableControlAccessorFactory.class);
	private Map<Class<?>, Class<?extends ControlAccessor>> accessors = 
			new Hashtable<Class<?>, Class<? extends ControlAccessor>>();
	private boolean mergeAccessors = true;
	
	public ConfigurableControlAccessorFactory() {
		initDefaultAccessors();
	}


	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public ControlAccessor getControlAccessor(Object control) {
		Class<?extends ControlAccessor> accessorClass = null;
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
				Constructor<?extends ControlAccessor> ctor = accessorClass.getConstructor(Object.class);
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
	
	/**
	 * 
	 */
	private void initDefaultAccessors() {
		accessors.put(JTextComponent.class, TextComponentAccessor.class);
		accessors.put(JCalendarCombo.class, JCalendarComboAccessor.class);
		accessors.put(JList.class, ListAccessor.class);
		accessors.put(Selector.class, SelectorAccessor.class);
		accessors.put(JToggleButton.class, ToggleButtonAccessor.class);
	}

	// Getters and Setters
	
	public Map<Class<?>, Class<?extends ControlAccessor>> getAccessors() {
		return accessors;
	}

	/**
	 * @param accessors the accessors to set
	 */
	public void setAccessors(Map<Class<?>, Class<?extends ControlAccessor>> accessors) {
		if (mergeAccessors)
			this.accessors.putAll(accessors);
		else 
			this.accessors = accessors;
	}


	/**
	 * @return the mergeAccessors
	 */
	public boolean isMergeAccessors() {
		return mergeAccessors;
	}


	/**
	 * @param mergeAccessors the mergeAccessors to set
	 */
	public void setMergeAccessors(boolean mergeAccessors) {
		this.mergeAccessors = mergeAccessors;
	}

}
