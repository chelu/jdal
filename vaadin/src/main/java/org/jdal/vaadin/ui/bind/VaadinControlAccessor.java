/*
 * Copyright 2009-2012 the original author or authors.
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
package org.jdal.vaadin.ui.bind;

import org.jdal.ui.View;
import org.jdal.ui.bind.AbstractControlAccessor;

import com.vaadin.ui.Component;

/**
 * Base class for Vaadin ControlAccessors.
 * 
 * @author Jose Luis Martin.
 * @since 2.1
 */
public abstract class VaadinControlAccessor extends AbstractControlAccessor {
	
	
	public VaadinControlAccessor() {

	}

	public VaadinControlAccessor(Object control) {
		super(control);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setEnabled(boolean enabled) {
		Object control = getControl();
		
		if (control instanceof Component) 
			((Component) control).setEnabled(enabled);
		else if (control instanceof View<?>) 
			((View<?>) control).enableView(enabled);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isEnabled() {
		Object control = getControl();
		
		if (control instanceof Component) 
			return ((Component) control).isEnabled();
		
		return true;
	}
}
