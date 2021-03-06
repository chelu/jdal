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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdal.aop.config.SerializableProxyFactoryBean;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.framework.autoproxy.AutoProxyUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Utility class for creating proxy bean definitions.
 * <p>
 * Note: This class includes code from {@link org.springframework.aop.scope.ScopedProxyUtils}
 * </p>
 * @author Jose Luis Martin
 * @since 2.0
 */
public class SerializableProxyUtils {

	public static Log log = LogFactory.getLog(SerializableProxyUtils.class);
	public static final String TARGET_NAME_PREFIX = "jdalSerializableProxy.";

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
		proxyDefinition.getPropertyValues().add("singleton", !targetDefinition.isPrototype());

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
	
	
	/**
	 * Create a new Serializable proxy for the given target
	 * @param target target to proxy
	 * @param proxyTargetClass true to force CGLIB proxies
	 * @param useMemoryCache if true keep a reference to target object in memory
	 * @param beanFactory beanFactory to use.
	 * @param descriptor Dependency descriptor
	 * @param beanName name of bean holding the dependency
	 * @return a new serializable proxy
	 */
	public static Object createSerializableProxy(Object target, boolean proxyTargetClass, boolean useMemoryCache,
			ConfigurableListableBeanFactory beanFactory, DependencyDescriptor descriptor, String beanName) 
	{
		if (target instanceof SerializableAopProxy)
			return target;
		
		if (log.isDebugEnabled())
			log.debug("Creating serializable proxy for [" + descriptor.getDependencyName() + "]" + 
					" in bean [" + beanName + "]");
		
		SerializableReference reference = new SerializableReference(target, proxyTargetClass, useMemoryCache, 
				beanFactory, descriptor,  beanName);
	
		
		return createSerializableProxy(target, reference);
	}

	/**
	 * Create a new Serializable proxy for the given target
	 * @param target target to proxy
	 * @param proxyTargetClass true to force CGLIB proxies
	 * @param useMemoryCache if true keep a reference to target object in memory
	 * @param beanFactory beanFactory to use.
	 * @param targetBeanName name of target bean
	 * @return a new serializable proxy
	 * 
	 */ 
	public static Object createSerializableProxy(Object target, boolean proxyTargetClass, 
			boolean useMemoryCache, ConfigurableListableBeanFactory beanFactory, String targetBeanName) {
		
		if (target instanceof SerializableAopProxy)
			return target;
		
		if (log.isDebugEnabled())
			log.debug("Creating serializable proxy for target bean [" +  targetBeanName + "]");
		
		SerializableReference reference = new SerializableReference(target, proxyTargetClass, useMemoryCache, 
				beanFactory, targetBeanName);

		return createSerializableProxy(target, reference);
		
	}
	
	/**
	 * Creates a new serializable proxy for given target holding it in memory 
	 * until deserialization.
	 * 
	 * @param target object to proxy
	 * @return a serialziable proxy for target.
	 */
	public static Object createCachedSerializableProxy(Object target) {
		return createSerializableProxy(target, true, true, null, null);
	}
	
	/**
	 * Create a new Serializable proxy for the given target
	 * @param target target to proxy
	 * @param beanFactory beanFactory to use.
	 * @param targetBeanName name of the target bean.
	 * @return a new serializable proxy
	 */ 
	public static Object createSerializableProxy(Object target, ConfigurableListableBeanFactory beanFactory, 
			String targetBeanName) {
		
		return createSerializableProxy(target, false, false, beanFactory, targetBeanName); 
	}
	
	/**
	 * Create a new Serializable proxy for the given target.
	 * @param target target to proxy
	 * @param reference serializable reference 
	 * @return a new serializable proxy
	 */
	public static Object createSerializableProxy(Object target, SerializableReference reference) {
		ProxyFactory pf = new ProxyFactory(target);		
		pf.setExposeProxy(true);		
		pf.setProxyTargetClass(reference.isProxyTargetClass());
		pf.addInterface(SerializableObject.class);
		pf.addAdvice(new SerializableIntroductionInterceptor(reference));
		
		return pf.getProxy(reference.getBeanFactory().getBeanClassLoader());
	}
}
