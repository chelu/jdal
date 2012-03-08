/*
 * Copyright 2002-2010 the original author or authors.
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

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyAccessException;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

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
	/** if true, binding is readOnly, ie from model to control */
	protected boolean readOnly = false;
	private ControlBindingErrorProcessor errorProcessor = new ControlBindingErrorProcessor();
	private BindingResult bindingResult;
	
	
	/**
	 * {@inheritDoc}
	 */
	public final void bind(Object component, String propertyName, Object model) {
		bind(component, propertyName, model, false);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public final void bind(Object component, String propertyName, Object model, boolean readOnly) {
		this.propertyName = propertyName;
		this.model = model;
		this.component = component;
		this.readOnly = readOnly;
		doBind();
	}

	/**
	 * Hook method to do bind via Component Listeners
	 * By default, do nothing
	 * @param component Component to bind
	 */
	protected void doBind() {
		
	}
	
	public final void refresh() {
		doRefresh();
	}
	
	public final void update() {
		if (!readOnly && model != null) {
			bindingResult = createBindingResult();
			doUpdate();
		}
	}
	
	/**
	 * Create a new Binding result, override to set nested paths on complex binders
	 * @return a new instance of BindingResult for this model
	 */
	protected BindingResult createBindingResult() {
		return new BeanPropertyBindingResult(model, model.getClass().getSimpleName());
	}

	abstract protected void doRefresh();
	abstract protected void doUpdate();

	/**
	 * Set value on binded object using the property name.
	 * @param value the value to set
	 */
	protected void setValue(Object value) {
		if (value == null || value != oldValue) {
			BeanWrapper wrapper = getBeanWrapper();
			try {
				wrapper.setPropertyValue(propertyName, value);
				oldValue = value;
			}
			catch (PropertyAccessException pae) {
				log.error(pae);
				errorProcessor.processPropertyAccessException(component, pae, bindingResult);
			}
		}
	}
	
	/**
	 * Get value from model
	 * @return model value
	 */
	protected Object getValue() {
		BeanWrapper wrapper = getBeanWrapper();
		Object value = null;
		try {
			value = wrapper.getPropertyValue(propertyName);
		} 
		catch (BeansException e) {
			log.error(e);
		}
		
		return value;
	}
	
	private BeanWrapper getBeanWrapper() {
		BeanWrapper wrapper = PropertyAccessorFactory.forBeanPropertyAccess(getModel());
		wrapper.registerCustomEditor(Date.class, 
				new CustomDateEditor(SimpleDateFormat.getDateTimeInstance(), true));
		return wrapper;
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

	/**
	 * @return the component
	 */
	public Object getComponent() {
		return component;
	}

	/**
	 * @param component the component to set
	 */
	public void setComponent(Object component) {
		this.component = component;
	}
	
	public BindingResult getBindingResult() {
		return bindingResult;
	}
}
