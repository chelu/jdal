/*
 * Copyright 2009-2014 Jose Luis Martin.
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

import javax.xml.bind.Binder;

import org.jdal.ui.bind.ControlAccessor;
import org.springframework.core.convert.ConversionService;
import org.springframework.format.support.DefaultFormattingConversionService;

/**
 * Support class for classes that do type conversion. 
 * Mainly to use as base class of {@link Binder Binders} 
 * and {@link ControlAccessor ControlAccessors}.
 * @author Jose Luis Martin
 * @since 2.1
 */
public class SpringConverter implements Serializable {
	
	private SimpleTypeConverter converter = new SimpleTypeConverter();
	
	public SpringConverter() {
		converter.setConversionService(new DefaultFormattingConversionService());
	}
	
	/**
	 * Try to convert value to required type.
	 * @param value value to convert
	 * @param requiredType required type
	 * @return converted value.
	 */
	public <T> T convertIfNecessary(Object value, Class<T> requiredType) {
		return converter.convertIfNecessary(value, requiredType);
	}

	public SimpleTypeConverter getConverter() {
		return converter;
	}

	public void setConverter(SimpleTypeConverter converter) {
		this.converter = converter;
	}

	public ConversionService getConversionService() {
		return converter.getConversionService();
	}

	public void setConversionService(ConversionService conversionService) {
		converter.setConversionService(conversionService);
	}

	public void overrideDefaultEditor(Class<?> requiredType,
			PropertyEditor propertyEditor) {
		converter.overrideDefaultEditor(requiredType, propertyEditor);
	}

	public void registerCustomEditor(Class<?> requiredType,
			PropertyEditor propertyEditor) {
		converter.registerCustomEditor(requiredType, propertyEditor);
	}

	public void registerCustomEditor(Class<?> requiredType,
			String propertyPath, PropertyEditor propertyEditor) {
		converter.registerCustomEditor(requiredType, propertyPath,
				propertyEditor);
	}
}
