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


import java.util.List;

import org.jdal.vaadin.ui.table.Column;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ListFactoryBean;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSimpleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 *
 */
public class ColumnsBeanDefinitionParser extends AbstractSimpleBeanDefinitionParser {

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("rawtypes")
	protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
	
		List columns = parserContext.getDelegate().parseListElement(element, builder.getRawBeanDefinition());
		builder.addPropertyValue("sourceList", columns);
		builder.addPropertyValue("targetListClass", Column.class);
		builder.setScope(BeanDefinition.SCOPE_PROTOTYPE);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Class<?> getBeanClass(Element element) {
		return ListFactoryBean.class;
	}
	
}
