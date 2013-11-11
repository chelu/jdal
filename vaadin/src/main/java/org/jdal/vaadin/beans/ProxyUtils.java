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

import org.springframework.aop.scope.ScopedProxyUtils;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Utility class for Vaadin serializable proxies.
 * 
 * @author Jose Luis Martin
 */
public class ProxyUtils {
	
	/**
	 * @param definition
	 * @param registry
	 * @param proxyTargetClass
	 * @return
	 */
	public static BeanDefinitionHolder createProxy(
			BeanDefinitionHolder definition, BeanDefinitionRegistry registry,
			boolean proxyTargetClass) {
		
		BeanDefinitionHolder holder = ScopedProxyUtils.createScopedProxy(definition, registry, proxyTargetClass);
		RootBeanDefinition def = (RootBeanDefinition) holder.getBeanDefinition();
		def.setBeanClass(VaadinProxyFactoryBean.class);
		
		return holder;
	}

}
