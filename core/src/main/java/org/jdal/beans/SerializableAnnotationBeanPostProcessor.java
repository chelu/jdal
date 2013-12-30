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
package org.jdal.beans;

import java.lang.reflect.AnnotatedElement;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdal.annotations.AnnotationUtils;
import org.jdal.annotations.SerializableObject;
import org.jdal.aop.SerializableIntroductionInterceptor;
import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DelegatingIntroductionInterceptor;
import org.springframework.aop.target.SingletonTargetSource;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;

/**
 * BeanPostProcessor that process {@link org.jdal.annotations.SerializableObject} annotation 
 * for replacing fields or properties with Serialiazable proxies.
 * 
 * @author Jose Luis Martin
 * @since 2.0
 */
public class SerializableAnnotationBeanPostProcessor extends InstantiationAwareBeanPostProcessorAdapter 
	implements BeanFactoryAware {
	
	private ConfigurableListableBeanFactory beanFactory;

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName)
			throws BeansException {

		List<AnnotatedElement> elements = AnnotationUtils.findAnnotatedElements(SerializableObject.class, bean.getClass());
		
		for (AnnotatedElement element : elements) {
			Object value = AnnotationUtils.getValue(element, bean);
			if (value != null) {
				Object proxy = getProxy(value);
				if (proxy != null)
					AnnotationUtils.setValue(element, bean, proxy);
			}
				
		}
		
		return bean;
	}

	/**
	 * Return serializable proxy from object.
	 * @param value object to proxy
	 * @return a new serializable proxy
	 */
	protected Object getProxy(Object value) {
		ProxyFactory pf = new ProxyFactory();
		pf.setProxyTargetClass(true);
		pf.setExposeProxy(true);
		pf.setTarget(value);
		pf.addAdvice(new SerializableIntroductionInterceptor());
		Object proxy = pf.getProxy(beanFactory.getBeanClassLoader());
		
		return proxy;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = (ConfigurableListableBeanFactory) beanFactory;
	}
}
