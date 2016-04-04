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
package org.jdal.ui.bind;

import java.beans.PropertyDescriptor;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdal.beans.PropertyUtils;
import org.jdal.beans.SpringConverter;
import org.jdal.ui.DefaultModelHolder;
import org.jdal.ui.ModelHolder;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyAccessException;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.core.convert.Property;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

/**
 * Base class for Binders. Implement doBind() to do the binding.
 * Dereference ModelHolders when setting its as binded model.
 * 
 * @author Jose Luis Martin
 * @since 1.1
 */
public abstract class AbstractBinder extends SpringConverter implements PropertyBinder {
	
	private static final Log log = LogFactory.getLog(AbstractBinder.class);

	/** binded property name */ 
	protected String propertyName;
	/** last value, used to revert o detect cicles */
	protected Object oldValue;
	/** binded model */
	private ModelHolder<Object> modelHolder;
	/** component object */
	protected Object component;
	/** if true, binding is readOnly, ie from model to control */
	protected boolean readOnly = false;
	private ControlBindingErrorProcessor errorProcessor = new ControlBindingErrorProcessor();
	private BindingResult bindingResult;
	
	
	public AbstractBinder() {
		registerCustomEditor(Date.class, 
				new CustomDateEditor(SimpleDateFormat.getDateTimeInstance(), true));
	}
	
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
		setModel(model);
		this.component = component;
		this.readOnly = readOnly;
		doBind();
	}

	/**
	 * Hook method to do bind via Component Listeners
	 * By default, do nothing
	 */
	protected void doBind() {
		
	}
	
	public final void refresh() {
		doRefresh();
	}
	
	public final void update() {
		if (!readOnly &&  getModel() != null) {
			bindingResult = createBindingResult();
			doUpdate();
		}
	}
	
	/**
	 * Create a new Binding result, override to set nested paths on complex binders
	 * @return a new instance of BindingResult for this model
	 */
	protected BindingResult createBindingResult() {
		Object model = getModel();
		return new BeanPropertyBindingResult(model, model.getClass().getSimpleName());
	}

	abstract protected void doRefresh();
	abstract protected void doUpdate();

	/**
	 * Set value on binded object using the property name.
	 * @param value the value to set
	 */
	protected void setValue(Object value) {
		BeanWrapper wrapper = getBeanWrapper();
		Object convertedValue = convertIfNecessary(value, wrapper.getPropertyType(this.propertyName));
		try {
			wrapper.setPropertyValue(propertyName, convertedValue);
			oldValue = value;
		}
		catch (PropertyAccessException pae) {
			log.error(pae);
			errorProcessor.processPropertyAccessException(component, pae, bindingResult);
		}
	}
	
	/**
	 * Get value from model
	 * @return model value
	 */
	protected Object getValue() {
		if (getModel() == null)
			return null;
		
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
		wrapper.setConversionService(getConversionService());
		wrapper.registerCustomEditor(Date.class, 
				new CustomDateEditor(SimpleDateFormat.getDateTimeInstance(), true));
		
		return wrapper;
	}
	
	/**
	 * @return Property for property binder
	 */
	protected Property getProperty() {
		PropertyDescriptor pd = getPropertyDescriptor();
		return new Property(getModel().getClass(), pd.getReadMethod(), pd.getWriteMethod());
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
		return modelHolder != null ? modelHolder.getModel() : null;
	}

	@SuppressWarnings("unchecked")
	public void setModel(Object model) {
		if (model instanceof ModelHolder) {
			this.modelHolder = (ModelHolder<Object>) model;
		}
		else {
			model = new DefaultModelHolder<Object>(model);
		}
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
	
	public Class<?> getPropertyType() {
		return getPropertyDescriptor().getPropertyType();
	}
	
	public PropertyDescriptor getPropertyDescriptor() {
		return PropertyUtils.getPropertyDescriptor(getModel().getClass(), propertyName);
	}
}
