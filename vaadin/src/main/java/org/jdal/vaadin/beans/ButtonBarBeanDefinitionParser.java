/*
 * Copyright 2009-2013 the original author or authors.
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
import org.jdal.beans.CustomBeanDefinitionParser;
import org.jdal.vaadin.ui.ButtonBar;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * {@link BeanDefinitionParser} for button-bar element.
 * 
 * @author Jose Luis Martin
 * @since 2.0
 */
public class ButtonBarBeanDefinitionParser extends CustomBeanDefinitionParser {

	private static final String ACTIONS_ELEMENT = "actions";
	private static final String NATIVE_BUTTONS_ELEMENT = "native-buttons";
	
	public ButtonBarBeanDefinitionParser() {
		super(ButtonBar.class);
	}

	@Override
	protected void doParse(Element element, ParserContext parserContext,
			BeanDefinitionBuilder builder) {
		
	    parseBeanDefinition(element, parserContext, builder);
		parseList(element, parserContext, builder, ACTIONS_ELEMENT);
		BeanDefinitionUtils.addPropertyValueIfNeeded(builder, element, NATIVE_BUTTONS_ELEMENT);
	}
	
}
