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

import org.springframework.aop.config.AopConfigUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;


/**
 * Register the AspectJ AutoProxyCreator with DeclareMixin Support.
 * 
 * @author Jose Luis Martin
 * @since 2.0
 *
 */
public class DeclareMixinAutoProxyCreatorConfigurer implements BeanFactoryPostProcessor {
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void postProcessBeanFactory(
			ConfigurableListableBeanFactory beanFactory) throws BeansException {
		
		AopConfigUtils.registerAspectJAnnotationAutoProxyCreatorIfNecessary((BeanDefinitionRegistry) beanFactory);
		BeanDefinition bd = beanFactory.getBeanDefinition(AopConfigUtils.AUTO_PROXY_CREATOR_BEAN_NAME);
		bd.getPropertyValues().add("aspectJAdvisorFactory", new DeclareMixinAspectJAdvisorFactory());
	}
	
}
