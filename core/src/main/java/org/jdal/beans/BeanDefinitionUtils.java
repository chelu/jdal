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

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.core.Conventions;
import org.w3c.dom.Element;

/**
 * Utility class for dealing with {@link BeanDefinition}s
 * 
 * @author Jose Luis Martin
 * @since 2.0
 */
public abstract class BeanDefinitionUtils {
	
	/**
	 * Add property value to {@link BeanDefinitionBuilder} if needed, following the naming {@link Conventions}
	 * @param bdb BeanDefintionBuilder to operate on.
	 * @param element Element holding the attribute
	 * @param attributeName the attribute name
	 */
	public static void addPropertyValueIfNeeded(BeanDefinitionBuilder bdb, Element element, String attributeName) {
		if (element.hasAttribute(attributeName))
			bdb.addPropertyValue(Conventions.attributeNameToPropertyName(attributeName), 
					element.getAttribute(attributeName));
	}
	
	/**
	 * Add property reference to {@link BeanDefinitionBuilder} if needed, following the naming {@link Conventions}
	 * @param bdb BeanDefintionBuilder to operate on.
	 * @param element Element holding the attribute
	 * @param attributeName the attribute name
	 */
	public static void addPropertyReferenceIfNeeded(BeanDefinitionBuilder bdb, Element element, String attributeName) {
		if (element.hasAttribute(attributeName))
			bdb.addPropertyReference(Conventions.attributeNameToPropertyName(attributeName), 
					element.getAttribute(attributeName));
	}
	
}
