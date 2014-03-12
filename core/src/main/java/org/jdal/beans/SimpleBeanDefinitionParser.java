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

import java.util.List;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSimpleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * A non abstract simple bean definition parser.
 * 
 * @author Jose Luis Martin
 * @since 2.0
 */
public class SimpleBeanDefinitionParser extends AbstractSimpleBeanDefinitionParser {
	
	public static final String SCOPE_ATTRIBUTE = "scope";
	private Class<?> beanClass;
	
	public SimpleBeanDefinitionParser(Class<?> beanClass) {
		this.beanClass = beanClass;
	}

	@Override
	protected Class<?> getBeanClass(Element element) {
		return beanClass;
	}

	@Override
	protected String getBeanClassName(Element element) {
		if (beanClass != null)
			return beanClass.getName();
		
		return null;
	}

	/**
	 * Parse a list element
	 * @param element parent element
	 * @param parserContext parse context
	 * @param builder bean definition builder.
	 * @param name the child list name.
	 */
	protected void parseList(Element element, ParserContext parserContext, BeanDefinitionBuilder builder,
			String name) {
		
		NodeList nl = element.getElementsByTagNameNS(element.getNamespaceURI(), name);

		if (nl.getLength() > 0) {
			Element e = (Element) nl.item(0);
			List<Object> actionList = parserContext.getDelegate().parseListElement(e, builder.getRawBeanDefinition());
			builder.addPropertyValue(name, actionList);
		}
	}
	
	protected void parseDefaultAttributes(Element element, BeanDefinitionBuilder builder) {
		if (element.hasAttribute(SCOPE_ATTRIBUTE))
			builder.setScope(element.getAttribute(SCOPE_ATTRIBUTE));
	}

	@Override
	protected boolean isEligibleAttribute(String attributeName) {
		return super.isEligibleAttribute(attributeName) &&
				!SCOPE_ATTRIBUTE.equals(attributeName);
	}
	
	
}
