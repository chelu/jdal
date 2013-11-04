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

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * BeanDefinitionParser for swing:editor 
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 * @since 1.3.2
 */
public class EditorBeanDefinitionParser implements BeanDefinitionParser {

	private static final String VIEW = "view";
	private static final String FRAME_CLASS = "info.joseluismartin.gui.ViewFrame";
	private static final String DIALOG_CLASS = "info.joseluismartin.gui.ViewDialog";
	private static final String ID = "id";
	/**
	 * {@inheritDoc}
	 */
	public BeanDefinition parse(Element element, ParserContext parserContext) {
		
		return null;
	}

}
