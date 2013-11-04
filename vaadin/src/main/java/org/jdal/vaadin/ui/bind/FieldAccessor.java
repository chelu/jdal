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

import org.jdal.ui.bind.AbstractControlAccessor;

import com.vaadin.ui.AbstractField;

/**
 * Field ControlAccessor
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 * @since 2.0
 */
public class FieldAccessor extends AbstractControlAccessor {
	
	/**
	 * @param control
	 */
	public FieldAccessor(Object control) {
		super(control);
	}

	/**
	 * {@inheritDoc}
	 */
	public Object getControlValue() {
		AbstractField field = (AbstractField) getControl();
		
		return field.getValue();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setControlValue(Object value) {
		AbstractField field = (AbstractField) getControl();
		
		field.setValue(value);
		
	}

}
