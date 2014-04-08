/*
 * Copyright 2009-2013 Jose Luis Martin.
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
package org.jdal.aop.config;

import org.jdal.aop.DeclareMixinAutoProxyCreatorConfigurer;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.parsing.BeanComponentDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;



/**
 * Bean definition parser for &lt;jdal-aop:declare-mixin-configurer&gt; element.
 * 
 * @author Jose Luis Martin
 * @since 2.0
 * @see org.jdal.aop.DeclareMixinAutoProxyCreatorConfigurer
 */
public class DeclareMixinConfigurerBeanDefinitionParser implements BeanDefinitionParser {
	
	private static final String DECLARE_MIXIN_CONFIGURER = "declareMixinConfigurer";

	@Override
	public BeanDefinition parse(Element element, ParserContext parserContext) {
		BeanDefinitionBuilder bdb = BeanDefinitionBuilder.rootBeanDefinition(
				DeclareMixinAutoProxyCreatorConfigurer.class);
		
		parserContext.registerBeanComponent(new BeanComponentDefinition(bdb.getBeanDefinition(),
				DECLARE_MIXIN_CONFIGURER));
		
		return null;
	}

}
