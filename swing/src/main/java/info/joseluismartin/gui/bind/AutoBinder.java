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

import info.joseluismartin.gui.Binder;
import info.joseluismartin.gui.ModelHolder;

import java.beans.PropertyDescriptor;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.ConfigurablePropertyAccessor;
import org.springframework.beans.PropertyAccessorFactory;

/**
 * Do an automatic binding of a view using reflection. Bind
 * controls with name equals to property names in model.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 * @see info.joseluismartin.gui.bind.Binder
 */
public class AutoBinder<T> implements Binder<T> {
	
	/** Log */
	private static final Log log = LogFactory.getLog(AutoBinder.class);
	/** View to bind on */
	private ModelHolder<T> view;
	/** Control accessor factory to use for create control accessors */
	private ControlAccessorFactory controlAccessorFactory;
	/** PropertyAccessor for access view fields */
	private ConfigurablePropertyAccessor viewPropertyAccessor;
	/** PropertyAccessor for access model properties */
	private BeanWrapper modelPropertyAccessor;
	/** Set with property names to ingnore on binding commands */
	private Set<String> ignoredProperties = new HashSet<String>();

	/**
	 * Create an AutoBinder for a View
	 * @param view View to bind.
	 */
	public AutoBinder(ModelHolder<T> view) {
		this.view = view;
		viewPropertyAccessor = new DirectFieldAccessor(view);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void refresh() {
		executeBinderCommand(new RefreshCommand());
	}

	/**
	 * {@inheritDoc}
	 */
	public void update() {
		executeBinderCommand(new UpdateCommand());
	}
	
	/**
	 * Execute BinderCommand (update or refresh) for all model properties
	 * @param command Command to execute.
	 */
	private void  executeBinderCommand(BinderCommand command) {
		modelPropertyAccessor = PropertyAccessorFactory.forBeanPropertyAccess(view.getModel());
		// iterate on model properties
		for (PropertyDescriptor pd :modelPropertyAccessor.getPropertyDescriptors()) {
			String propertyName = pd.getName();
			if ( !ignoredProperties.contains(propertyName) && viewPropertyAccessor.isReadableProperty(propertyName)) {
				Object control = viewPropertyAccessor.getPropertyValue(propertyName);
				if (control != null) {
					if (log.isDebugEnabled()) 
						log.debug("Found control: " + control.getClass().getSimpleName() + " for property: " + propertyName);

					ControlAccessor controlAccessor = controlAccessorFactory.getControlAccessor(control);
					if (controlAccessor != null)
						command.execute(controlAccessor, propertyName);
				}
			}
		}
	}
		
	/**
	 * {@inheritDoc}
	 */
	public T getModel() {
		return view.getModel();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setModel(T model) {
		view.setModel(model);
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
	 * Add a property name  to ignore on binding.
	 * @param propertyName property name to ignore
	 */
	public void ignoreProperty(String propertyName) {
		ignoredProperties.add(propertyName);
	}
	

	/**
	 * @return the ignoredProperties
	 */
	public Set<String> getIgnoredProperties() {
		return ignoredProperties;
	}

	/**
	 * @param ignoredProperties the ignoredProperties to set
	 */
	public void setIgnoredProperties(Set<String> ignoredProperties) {
		this.ignoredProperties = ignoredProperties;
	}

	/**
	 * Add a Collection of property names to ignore on binding
	 * @param c Collection of property names.
	 */
	public void ignoreProperties(Collection<? extends String> c) {
		ignoredProperties.addAll(c);
	}
	
	/**
	 *  Binder Command Callback
	 * @author Jose Luis Martin - (jlm@joseluismartin.info)
	 */
	interface BinderCommand {
		void execute(ControlAccessor accessor, String propertyName);
	}
	
	/**
	 * Update Command Callback
	 * @author Jose Luis Martin - (jlm@joseluismartin.info)
	 */
	class UpdateCommand implements  BinderCommand {

		public void execute(ControlAccessor controlAccessor, String name) {
			modelPropertyAccessor.setPropertyValue(name, controlAccessor.getControlValue());
		}
	}
	
	/**
	 * Refresh Command Callback
	 * @author Jose Luis Martin - (jlm@joseluismartin.info)
	 */
	class RefreshCommand implements BinderCommand {

		public void execute(ControlAccessor controlAccessor, String name) {
			controlAccessor.setControlValue(modelPropertyAccessor.getPropertyValue(name));
		}
		
	}

}


