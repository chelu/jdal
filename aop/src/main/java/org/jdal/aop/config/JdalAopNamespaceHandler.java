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
package org.jdal.aop.config;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
* NamespaceHandler for jdal-aop (jda) spring namespace
* 
* @author Jose Luis Martin
* @since 2.0
*/
public class JdalAopNamespaceHandler extends NamespaceHandlerSupport {

	@Override
	public void init() {
		registerBeanDefinitionParser("aspectj-autoproxy", null);
		registerBeanDefinitionDecorator("serializable-proxy", new SerializableProxyBeanDefinitionDecorator());
	}
}
