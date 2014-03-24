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
package org.jdal.aop;

import org.jdal.aop.config.SerializableProxyFactoryBean;
import org.springframework.aop.framework.autoproxy.AutoProxyUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Utility class for creating proxy bean definitions.
 * <p>
 * Note: This class includes code from {@link org.springframework.aop.scope.ScopedProxyUtils}
 * </p>
 * @author Jose Luis Martin
 */
public class ProxyUtils {

	private static final String TARGET_NAME_PREFIX = "serializableProxy.";

	public static  BeanDefinitionHolder createSerializableProxy(BeanDefinitionHolder definition,
			BeanDefinitionRegistry registry, boolean proxyTargetClass) {

		String originalBeanName = definition.getBeanName();
		BeanDefinition targetDefinition = definition.getBeanDefinition();

		// Create a scoped proxy definition for the original bean name,
		// "hiding" the target bean in an internal target definition.
		RootBeanDefinition proxyDefinition = new RootBeanDefinition(SerializableProxyFactoryBean.class);
		proxyDefinition.setOriginatingBeanDefinition(definition.getBeanDefinition());
		proxyDefinition.setSource(definition.getSource());
		proxyDefinition.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);

		String targetBeanName = getTargetBeanName(originalBeanName);
		proxyDefinition.getPropertyValues().add("targetBeanName", targetBeanName);

		if (proxyTargetClass) {
			targetDefinition.setAttribute(AutoProxyUtils.PRESERVE_TARGET_CLASS_ATTRIBUTE, Boolean.TRUE);
		}
		else {
			proxyDefinition.getPropertyValues().add("proxyTargetClass", Boolean.FALSE);
		}

		// Copy autowire settings from original bean definition.
		proxyDefinition.setAutowireCandidate(targetDefinition.isAutowireCandidate());
		proxyDefinition.setPrimary(targetDefinition.isPrimary());
		if (targetDefinition instanceof AbstractBeanDefinition) {
			proxyDefinition.copyQualifiersFrom((AbstractBeanDefinition) targetDefinition);
		}
		
		// Set singleton property of FactoryBean
		proxyDefinition.getPropertyValues().add("singleton", targetDefinition.isSingleton());

		// The target bean should be ignored in favor of the scoped proxy.
		targetDefinition.setAutowireCandidate(false);
		targetDefinition.setPrimary(false);

		// Register the target bean as separate bean in the factory.
		registry.registerBeanDefinition(targetBeanName, targetDefinition);

		// Return the scoped proxy definition as primary bean definition
		// (potentially an inner bean).
		return new BeanDefinitionHolder(proxyDefinition, originalBeanName, definition.getAliases());
	}

	public static String getTargetBeanName(String originalBeanName) {
		return TARGET_NAME_PREFIX + originalBeanName;
	}

}
