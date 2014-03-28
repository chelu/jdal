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

import org.jdal.aop.ProxyUtils;
import org.jdal.aop.SerializableTargetSource;
import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.ProxyConfig;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

/**
 * ProxyFactoryBean to make serializable proxy for non serializable beans, including
 * prototypes allowing session replication
 * 
 * @author Jose Luis Martin
 * @since 2.0
 */
public class SerializableProxyFactoryBean  extends ProxyConfig implements FactoryBean<Object>,
	BeanFactoryAware {

	private ConfigurableListableBeanFactory beanFactory;
	private boolean singleton;
	private String targetBeanName;
	private Object proxy;

	public SerializableProxyFactoryBean() {
		setProxyTargetClass(true);
	}

	public Object getObject() throws Exception {
		if (isSingleton()) {
			if (proxy == null)
				proxy = createProxy();
			
			return proxy;
		}
		
		return createProxy();
	}
	
	 protected Object createProxy() {
		Object target  = beanFactory.getBean(this.targetBeanName);
		
		return  ProxyUtils.createSerializableProxy(target, isProxyTargetClass(), false,
				this.beanFactory, this.targetBeanName);
	}

	protected TargetSource createTargetSource() {
		return new SerializableTargetSource(this.beanFactory, this.targetBeanName, !isSingleton());
	}

	public Class<?> getObjectType() {
		if (beanFactory != null) 
			return beanFactory.getType(targetBeanName);

		return null;
	}

	public boolean isSingleton() {
		return singleton;
	}

	public void setSingleton(boolean singleton) {
		this.singleton = singleton;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = (ConfigurableListableBeanFactory) beanFactory;
	}

	public String getTargetBeanName() {
		return targetBeanName;
	}

	public void setTargetBeanName(String targetBeanName) {
		this.targetBeanName = targetBeanName;
	}

}

