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

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.beans.factory.xml.XmlReaderContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * {@link BeanDefinitionParser} for extending bean elements.
 * 
 * @author Jose Luis Martin
 * @since 2.1
 */
public class CustomBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {
	
	private Class<?> beanClass;
	private String beanName;
	
	public CustomBeanDefinitionParser(Class<?> beanClass) {
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
	
	/**
	 * Parse bean like a real bean definition.
	 * @param ele element
	 * @param parserContext parserContext
	 * @param builder builder
	 */
	protected void parseBeanDefinition(Element ele, ParserContext parserContext, BeanDefinitionBuilder builder) {
		
		BeanDefinitionParserDelegate delegate = parserContext.getDelegate();
		AbstractBeanDefinition bd = builder.getRawBeanDefinition();
		XmlReaderContext reader =  parserContext.getReaderContext();
		
		try {
			delegate.parseBeanDefinitionAttributes(ele, beanName, null , bd);
			bd.setDescription(DomUtils.getChildElementValueByTagName(ele, "description"));

			delegate.parseMetaElements(ele, bd);
			delegate.parseLookupOverrideSubElements(ele, bd.getMethodOverrides());
			delegate.parseReplacedMethodSubElements(ele, bd.getMethodOverrides());

			delegate.parseConstructorArgElements(ele, bd);
			delegate.parsePropertyElements(ele, bd);
			delegate.parseQualifierElements(ele, bd);

		}
		catch (NoClassDefFoundError err) {
			reader.error("Class that bean class [" + this.beanClass + "] depends on not found", ele, err);
		}
		catch (Throwable ex) {
			reader.error("Unexpected failure during bean definition parsing", ele, ex);
		}
		
	}

	@Override
	protected String resolveId(Element element,
			AbstractBeanDefinition definition, ParserContext parserContext)
			throws BeanDefinitionStoreException {

		this.beanName = super.resolveId(element, definition, parserContext);
		
		return this.beanName;
	}
}
