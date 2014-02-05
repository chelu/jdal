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

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;

/**
 * Configure {@link BeanDefinition} with {@link BeanDefinition#ROLE_INFRASTRUCTURE} by bean name.
 * 
 * @author Jose Luis Martin
 * @since 2.0
 *
 */
public class RoleBeanDefinitionConfigurer implements BeanDefinitionRegistryPostProcessor {
	
	private String[] beanNames;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void postProcessBeanFactory(
			ConfigurableListableBeanFactory beanFactory) throws BeansException {
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void postProcessBeanDefinitionRegistry(
			BeanDefinitionRegistry registry) throws BeansException {

		if (beanNames == null)
			return;
		
		for (String name : beanNames) {
			if (registry.containsBeanDefinition(name))
				((AbstractBeanDefinition) registry.getBeanDefinition(name)).setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
		}
	}

	public String[] getBeanNames() {
		return beanNames;
	}

	public void setBeanNames(String[] beanNames) {
		this.beanNames = beanNames;
	}

}
