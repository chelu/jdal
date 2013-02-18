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


import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JToggleButton;
import javax.swing.text.JTextComponent;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdal.ui.Selector;
import org.jdal.ui.View;
import org.jdal.ui.table.TablePanel;

/**
 * Implementation of ControlAccessorFactory using an asociative Map&lt;Class, ControlAccessor&gt;
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 * @since 1.1
 * @see org.jdal.ui.bind.ControlAccessor
 * @see org.jdal.ui.bind.ControlAccessorFactory
 */
public class ConfigurableControlAccessorFactory implements ControlAccessorFactory {
	
	private final static Log log = LogFactory.getLog(ConfigurableControlAccessorFactory.class);
	
	private static ControlAccessorFactory defaultFactory;
	
	private Map<Class<?>, Class<?extends ControlAccessor>> accessors = 
			new Hashtable<Class<?>, Class<? extends ControlAccessor>>();
	private boolean mergeAccessors = true;
	
	public ConfigurableControlAccessorFactory() {
		initDefaultAccessors();
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
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
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				e.printStackTrace(new PrintStream(baos));
				log.error(baos);
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
		accessors.put(JList.class, ListAccessor.class);
		accessors.put(Selector.class, SelectorAccessor.class);
		accessors.put(JToggleButton.class, ToggleButtonAccessor.class);
		accessors.put(JComboBox.class, ComboAccessor.class);
		accessors.put(View.class, ViewAccessor.class);
		accessors.put(JLabel.class, LabelAccessor.class);
		accessors.put(TablePanel.class, TablePanelAccessor.class);
	}

	// Getters and Setters
	
	/**
	 * For use without DI Container
	 * @return a default singleton ControlAccesorFactory
	 */
	public static synchronized ControlAccessorFactory getDefaultFactory() {
		if (defaultFactory == null)
			defaultFactory = new ConfigurableControlAccessorFactory();
		
		return defaultFactory;
	}
	
	public Map<Class<?>, Class<?extends ControlAccessor>> getAccessors() {
		return accessors;
	}

	/**
	 * @param accessors the accessors to set
	 */
	public void setAccessors(Map<Class<?>, Class<?extends ControlAccessor>> accessors) {
		if (!mergeAccessors)
			this.accessors.clear();
		
		this.accessors.putAll(accessors);
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
