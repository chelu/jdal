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

	public SerializableProxyAdvisor() {
		super(new SerializableIntroductionInterceptor());
	}

	/**
	 * Checks for {@link SerializableProxy} anntation at type level.
	 * @return true if annotation found.
	 */
	@Override
	public boolean matches(Class<?> clazz) {
		SerializableProxy ann = AnnotationUtils.findAnnotation(clazz, SerializableProxy.class);
		
		if (ann != null && !SerializableAopProxy.class.isAssignableFrom(clazz)) {
			String beanName = ProxyCreationContext.getCurrentProxiedBeanName();
			if (beanName != null && !beanName.startsWith(SerializableProxyUtils.TARGET_NAME_PREFIX)) {
				configureReference(ann, beanName);
				return true;
			}
		}
		
		return false;
	}

	/**
	 * Configure the serializable reference with annotation parameters and target bean name.
	 * @param ann annotation.
	 */
	private void configureReference(SerializableProxy ann, String beanName) {
		if (log.isDebugEnabled())
			log.debug("Configuring serializable reference for bean [" + beanName +"]");
		
		SerializableIntroductionInterceptor advice = (SerializableIntroductionInterceptor) getAdvice();
		SerializableReference reference = advice.getReference();
		reference.setTargetBeanName(beanName);
		reference.setBeanFactory(beanFactory);
		reference.setProxyTargetClass(ann.proxyTargetClass());
		reference.setUseMemoryCache(ann.useCache());
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = (ConfigurableListableBeanFactory) beanFactory;
		
	}
	
}
