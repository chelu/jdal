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

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ListFactoryBean;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSimpleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * Bean definitio parser for list that allow to set a default scope.
 * 
 * @author Jose Luis Martin
 * @since 2.0
 *
 */
public class ListBeanDefinitionParser extends AbstractSimpleBeanDefinitionParser {
	
	private static final String SCOPE_ATTRIBUTE = "scope";
	private String defaultScope = BeanDefinition.SCOPE_PROTOTYPE;
	
	
	
	public ListBeanDefinitionParser(String defaultScope) {
		this.defaultScope = defaultScope;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
		List<Object> columns = parserContext.getDelegate().parseListElement(element, builder.getRawBeanDefinition());
		builder.addPropertyValue("sourceList", columns);
		
		String scope = element.getAttribute(SCOPE_ATTRIBUTE);
		
		if (StringUtils.isEmpty(scope))
			scope = defaultScope;

		builder.setScope(defaultScope);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Class<?> getBeanClass(Element element) {
		return ListFactoryBean.class;
	}

}
