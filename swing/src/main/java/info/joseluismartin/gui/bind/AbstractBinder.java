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

import info.joseluismartin.gui.ModelHolder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyAccessorFactory;

/**
 * Base class for Binders. Implement doBind() to do the binding.
 * Dereference ModelHolders when setting its as binded model.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public abstract class AbstractBinder implements PropertyBinder {
	
	private static final Log log = LogFactory.getLog(AbstractBinder.class);

	/** binded property name */ 
	protected String propertyName;
	/** last value, used to revert o detect cicles */
	protected Object oldValue;
	/** binded model */
	private Object model;
	/** component object */
	protected Object component;
	
	
	/**
	 * {@inheritDoc}
	 */
	public final void bind(Object component, String propertyName, Object model) {
		this.propertyName = propertyName;
		this.model = model;
		this.component = component;
		doBind(component);
	}

	/**
	 * Hook method to do bind via Component Listeners
	 * By default, do nothing
	 * @param component Component to bind
	 */
	protected void doBind(Object component) {
		
	}

	/**
	 * Set value on binded object using the property name.
	 * @param value the value to set
	 */
	protected void setValue(Object value) {
		if (value != oldValue) {
			BeanWrapper wrapper = PropertyAccessorFactory.forBeanPropertyAccess(getModel());
			try {
				wrapper.setPropertyValue(propertyName, value);
				oldValue = value;
			
			}
			catch (BeansException e) {
				log.error(e);
			}
		}
	}
	
	/**
	 * Get value from model
	 * @return
	 */
	protected Object getValue() {
		BeanWrapper wrapper = PropertyAccessorFactory.forBeanPropertyAccess(getModel());
		Object value = null;
		try {
			value = wrapper.getPropertyValue(propertyName);
		} 
		catch (BeansException e) {
			log.error(e);
		}
		
		return value;
		
		
	}
	// Getters and Setters

	public String getPropertyName() {
		return propertyName;
	}


	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}


	protected Object getOldValue() {
		return oldValue;
	}


	protected void setOldValue(Object oldValue) {
		this.oldValue = oldValue;
	}

	public Object getModel() {
		if (model instanceof ModelHolder<?>) {
			return ((ModelHolder<?>) model).getModel();
		}
		
		return model;
	}

	public void setModel(Object model) {
		this.model = model;
	}

}
