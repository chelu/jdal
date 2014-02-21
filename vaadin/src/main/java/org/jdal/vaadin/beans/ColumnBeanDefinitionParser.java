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
package org.jdal.vaadin.beans;


import org.jdal.beans.BeanDefinitionUtils;
import org.jdal.vaadin.ui.table.Column;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSimpleBeanDefinitionParser;
import org.w3c.dom.Element;

import com.vaadin.ui.Table.Align;

/**
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 *
 */
public class ColumnBeanDefinitionParser extends AbstractSimpleBeanDefinitionParser {
	private static final String SCOPE_ATTRIBUTE = "scope";
	private static final String PROPERTY_EDITOR_ATTRIBUTE = "property-editor";
	private static final String ALIGN_ATTRIBUTE = "align";
	private static final String RENDERER_ATTRIBUTE = "renderer";
	private static final String COLUMN_GENERATOR_ATTRIBUTE = "column-generator";

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected Class getBeanClass(Element element) {
		return Column.class;
	}

	@Override
	protected boolean isEligibleAttribute(String attributeName) {
		return super.isEligibleAttribute(attributeName) && !SCOPE_ATTRIBUTE.equals(attributeName) &&
				!PROPERTY_EDITOR_ATTRIBUTE.equals(attributeName) && 
				!ALIGN_ATTRIBUTE.equals(attributeName) && 
				!COLUMN_GENERATOR_ATTRIBUTE.equals(attributeName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void postProcess(BeanDefinitionBuilder beanDefinition, Element element) {
		beanDefinition.setScope("prototype");
		
		
		BeanDefinitionUtils.addPropertyReferenceIfNeeded(beanDefinition, element, COLUMN_GENERATOR_ATTRIBUTE);
		BeanDefinitionUtils.addPropertyReferenceIfNeeded(beanDefinition, element, RENDERER_ATTRIBUTE);
		BeanDefinitionUtils.addPropertyReferenceIfNeeded(beanDefinition, element, PROPERTY_EDITOR_ATTRIBUTE);
		
		if (element.hasAttribute(ALIGN_ATTRIBUTE))
			beanDefinition.addPropertyValue(ALIGN_ATTRIBUTE, getAlignValue(element.getAttribute(ALIGN_ATTRIBUTE)));
	}

	/**
	 * Return the Vaadin align attribute
	 * @param attribute
	 * @return
	 */
	private Align getAlignValue(String align) {
		if ("right".equalsIgnoreCase(align))
			return Align.RIGHT;
		else if ("center".equalsIgnoreCase(align))
			return Align.CENTER;

		return Align.LEFT;
		
	}
	
}
