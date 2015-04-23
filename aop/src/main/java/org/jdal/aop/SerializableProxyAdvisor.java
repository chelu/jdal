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
import org.jdal.annotation.SerializableProxy;
import org.springframework.aop.aspectj.TypePatternClassFilter;
import org.springframework.aop.framework.autoproxy.ProxyCreationContext;
import org.springframework.aop.support.DefaultIntroductionAdvisor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.annotation.AnnotationUtils;

/**
 * Advisor for SerializableProxy annotation on types.
 * 
 * @author Jose Luis Martin.
 * @since 2.0
 */
public class SerializableProxyAdvisor extends DefaultIntroductionAdvisor 
	implements BeanFactoryAware {
	
	private static final Log log = LogFactory.getLog(SerializableProxyAdvisor.class);
	private ConfigurableListableBeanFactory beanFactory;
	private TypePatternClassFilter typePatternClassFilter = new TypePatternClassFilter();
	

	public SerializableProxyAdvisor() {
		super(new SerializableIntroductionInterceptor());
	}

	/**
	 * Checks for {@link SerializableProxy} annotation at type level.
	 * @return true if annotation found.
	 */
	@Override
	public boolean matches(Class<?> clazz) {
		String beanName = ProxyCreationContext.getCurrentProxiedBeanName();
		// skip serializable proxies and targets
		if (SerializableAopProxy.class.isAssignableFrom(clazz) || isTargetSerializableProxyBean(beanName) )
			return false;
		
		// check annotation
		SerializableProxy ann = AnnotationUtils.findAnnotation(clazz, SerializableProxy.class);
		if (ann != null) {
			if (isProxyInCreation(beanName)) {
				configureReference(ann, beanName);
			}
			return true;
		}
		// check type pattern
		if (getTypePattern() != null && this.typePatternClassFilter.matches(clazz)) {
			 if (isProxyInCreation(beanName))
				 configureReference(beanName);
		
			return true;
		}
		
		return false;
	}

	/**
	 * Configure reference with target bean name
	 * @param beanName target bean name
	 */
	private void configureReference(String beanName) {
		if (log.isDebugEnabled())
			log.debug("Configuring serializable reference for bean [" + beanName +"]");
		
		SerializableReference reference = getReference();
		reference.setTargetBeanName(beanName);
		reference.setBeanFactory(this.beanFactory);
		
	}

	/**
	 * Test if autoproxy creator is creating the proxy.
	 * @param beanName bean name
	 * @return true if beanName is not null
	 */
	private boolean isProxyInCreation(String beanName) {
		return beanName != null;
	}
	
	/**
	 * Test if beanName match a serializable proxy target bean created by {@link SerializableProxyUtils}
	 * @param beanName the name to check
	 * @return true if is a target bean of a serializable proxy
	 */
	private boolean isTargetSerializableProxyBean(String beanName) {
		return beanName != null && beanName.startsWith(SerializableProxyUtils.TARGET_NAME_PREFIX);
	}

	/**
	 * Configure the serializable reference with annotation parameters and target bean name.
	 * @param ann annotation.
	 */
	private void configureReference(SerializableProxy ann, String beanName) {
		SerializableReference reference = getReference();
		reference.setProxyTargetClass(ann.proxyTargetClass());
		reference.setUseMemoryCache(ann.useCache());
		configureReference(beanName);
	}

	/**
	 * @return the serializable reference
	 */
	private SerializableReference getReference() {
		SerializableIntroductionInterceptor advice = (SerializableIntroductionInterceptor) getAdvice();
		return advice.getReference();
	}
	

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = (ConfigurableListableBeanFactory) beanFactory;
		
	}
	
	public String getTypePattern() {
		return this.typePatternClassFilter.getTypePattern();
	}
	
	public void setTypePattern(String typePattern) {
		this.typePatternClassFilter.setTypePattern(typePattern);
	}
	
	public boolean isUseMemoryCache() {
		return getReference().isUseMemoryCache();
	}
	
	public void setUseMemoryCache(boolean useMemoryCache) {
		getReference().setUseMemoryCache(useMemoryCache);
	}
	
}
