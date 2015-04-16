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
package org.jdal.beans;

import java.beans.PropertyEditor;
import java.io.Serializable;

import org.springframework.beans.TypeMismatchException;
import org.springframework.core.MethodParameter;
import org.springframework.util.ClassUtils;

/**
 * Type converter that also convert Objects to String.
 * 
 * @author Jose Luis Martin
 * @sice 1.1
 */
public class SimpleTypeConverter extends org.springframework.beans.SimpleTypeConverter 
	implements Serializable {
	
	@Override
	public <T> T convertIfNecessary(Object value, Class<T> requiredType) throws TypeMismatchException {
		return convertIfNecessary(value, requiredType, (MethodParameter) null);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T convertIfNecessary(Object value, Class<T> requiredType, MethodParameter methodParam)
			throws TypeMismatchException {

		T convertedValue = null;
		try {
			convertedValue = super.convertIfNecessary(value, requiredType, methodParam);
		}
		catch (TypeMismatchException tme) {
			// Try Object to String conversion
			if (ClassUtils.isAssignable(String.class, requiredType)) {
				if (value != null) {
					PropertyEditor pe = findCustomEditor(value.getClass(), null);
					if (pe != null) {
						pe.setValue(value);
						return (T) pe.getAsText();
					}
					else {  // Object to String
						return (T) value.toString();
					}
				}
				else {  // null to String
					return (T) "";
				}
			}
			else {
				throw tme;
			}
		}

		return convertedValue;
	}

}
