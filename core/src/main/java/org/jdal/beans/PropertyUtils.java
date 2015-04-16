/*
 * Copyright 2008-2010 the original author or authors.
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

import java.beans.PropertyDescriptor;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.jdal.util.BeanUtils;

/**
 * Utility class for dealing with bean properties.
 * 
 * @author Jose Luis Martin
 */
public abstract class PropertyUtils {

	public static String PROPERTY_SEPARATOR = ".";
	
	public static String getPropertyName(String propertyPath) {
		if (propertyPath.contains(PROPERTY_SEPARATOR)) 
			return StringUtils.substringAfterLast(propertyPath, PROPERTY_SEPARATOR);
		
		return propertyPath;
	}
	
	public static String getPath(String propertyPath) {
		return StringUtils.substringBeforeLast(propertyPath, PROPERTY_SEPARATOR);
	}

	public static boolean isNested(String propertyPath) {
		return propertyPath.contains(PROPERTY_SEPARATOR);
	}
	
	public static String getFirstPropertyName(String propertyPath) {
		return isNested(propertyPath) ? 
				StringUtils.substringBefore(propertyPath, PROPERTY_SEPARATOR) : propertyPath;
	}
	
	public static String getNestedPath(String propertyPath) { 
		return StringUtils.substringAfter(propertyPath, PROPERTY_SEPARATOR);
	}
	
	public static String toHumanReadable(String propertyName) {
		String humanReadable = getPropertyName(propertyName).replaceAll("([A-Z])", " $1").trim();
		return WordUtils.capitalize(humanReadable);
	}
	
	public static PropertyDescriptor getPropertyDescriptor(Class<?> clazz, String propertyPath) {
		PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(clazz, 
				getFirstPropertyName(getFirstPropertyName(propertyPath)));
			
		if (isNested(propertyPath))  // recurse
			return getPropertyDescriptor(pd.getPropertyType(), getNestedPath(propertyPath));
			
		return pd;
	}
}
