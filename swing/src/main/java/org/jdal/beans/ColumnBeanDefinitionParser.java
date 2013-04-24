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


import org.jdal.ui.ColumnDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSimpleBeanDefinitionParser;
import org.w3c.dom.Element;

/**
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 *
 */
public class ColumnBeanDefinitionParser extends AbstractSimpleBeanDefinitionParser {
	
	private static final String SCOPE_ATTRIBUTE = "scope";
	private static final String RENDERER_ATTRIBUTE = "renderer"; 
	private static final String EDITOR_ATTRIBUTE = "editor";
	
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected Class getBeanClass(Element element) {
		return ColumnDefinition.class;
	}

	@Override
	protected boolean isEligibleAttribute(String attributeName) {
		return super.isEligibleAttribute(attributeName) && !SCOPE_ATTRIBUTE.equals(attributeName)
				&& !RENDERER_ATTRIBUTE.equals(attributeName) 
				&& !EDITOR_ATTRIBUTE.equals(attributeName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void postProcess(BeanDefinitionBuilder beanDefinition, Element element) {
		beanDefinition.setScope("prototype");
		
		if (element.hasAttribute(RENDERER_ATTRIBUTE))
			beanDefinition.addPropertyReference(RENDERER_ATTRIBUTE, element.getAttribute(RENDERER_ATTRIBUTE));
	}
	
	
}
