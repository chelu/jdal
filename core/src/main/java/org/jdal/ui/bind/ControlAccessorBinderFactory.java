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

/**
 * Binder Factory that use a ControlAccessorFactory to create PropertyBinders.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 * @since 1.1
 *
 */
public class ControlAccessorBinderFactory implements BinderFactory {
	
	private ControlAccessorFactory controlAccessorFactory;

	/**
	 * Default Ctor
	 */
	public ControlAccessorBinderFactory() {
		
	}
	
	/**
	 * Ctor
	 * @param controlAccessorFactory
	 */
	public ControlAccessorBinderFactory(ControlAccessorFactory controlAccessorFactory) {
		this.controlAccessorFactory = controlAccessorFactory;
	}

	/**
	 * {@inheritDoc}
	 */
	public PropertyBinder getBinder(Class<?> clazz) {
		ControlBinder binder = new ControlBinder(controlAccessorFactory);
		
		return binder;
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

}
