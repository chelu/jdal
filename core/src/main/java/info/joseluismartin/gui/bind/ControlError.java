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

import org.springframework.validation.FieldError;

/**
 * FieldError for Swing controls
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class ControlError extends FieldError {

	private Object component;
	
	/**
	 * @param objectName
	 * @param field
	 * @param rejectedValue
	 * @param bindingFailure
	 * @param codes
	 * @param arguments
	 * @param defaultMessage
	 */
	public ControlError(String objectName, String field, Object rejectedValue, boolean bindingFailure, String[] codes,
			Object[] arguments, String defaultMessage) {
		super(objectName, field, rejectedValue, bindingFailure, codes, arguments, defaultMessage);
	}

	/**
	 * @param objectName
	 * @param field
	 * @param defaultMessage
	 */
	public ControlError(String objectName, String field, String defaultMessage) {
		super(objectName, field, defaultMessage);
	}

	/**
	 * @param control
	 * @param objectName
	 * @param field
	 * @param rejectedValue
	 * @param bindingFailure
	 * @param codes
	 * @param arguments
	 * @param defaultMessage
	 */
	public ControlError(Object control, String objectName, String field, Object rejectedValue,
				boolean bindingFailure, String[] codes, Object[] arguments, String defaultMessage) {
		this(objectName, field, rejectedValue, bindingFailure, codes, arguments, defaultMessage);
		this.component = control;
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
	
	
}
