/*
 * Copyright 2009-2012 Jose Luis Martin.
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

import java.lang.reflect.Modifier;

import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.AopInfrastructureBean;
import org.springframework.aop.framework.ProxyConfig;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DelegatingIntroductionInterceptor;
import org.springframework.aop.target.SingletonTargetSource;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.util.ClassUtils;

/**
 * ProxyFactoryBean to make serializable non serializable objects.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class SerializableProxyFactoryBean  extends ProxyConfig implements FactoryBean<Object>,
	BeanFactoryAware {

	private ConfigurableBeanFactory beanFactory;
	private boolean singleton = false;
	private String targetBeanName;
	private TargetSource targetSource;
	
	public SerializableProxyFactoryBean() {
		setProxyTargetClass(true);
	}

	public Object getObject() throws Exception {

		targetSource = createTargetSource();
		ProxyFactory pf = new ProxyFactory();
		pf.copyFrom(this);
		pf.setTargetSource(targetSource);	

		Class<?> beanType = beanFactory.getType(this.targetBeanName);
		if (beanType == null) {
			throw new IllegalStateException("Cannot create serializable proxy for bean '" + this.targetBeanName +
					"': Target type could not be determined at the time of proxy creation.");
		}
		if (!isProxyTargetClass() || beanType.isInterface() || Modifier.isPrivate(beanType.getModifiers())) {
			pf.setInterfaces(ClassUtils.getAllInterfacesForClass(beanType, beanFactory.getBeanClassLoader()));
		}
		
		DefaultSerializableObject  dso = new DefaultSerializableObject(targetSource.getTarget());
		pf.addAdvice(new DelegatingIntroductionInterceptor(dso));

		pf.addInterface(AopInfrastructureBean.class);

		Object proxy =  pf.getProxy(beanFactory.getBeanClassLoader());
		dso.setSerializedObject(proxy);
		
		return proxy;
	}

	/**
	 * @return
	 */
	protected TargetSource createTargetSource() {
		return new SingletonTargetSource(beanFactory.getBean(targetBeanName));
	}

	public Class<?> getObjectType() {
		if (targetSource != null)
			return targetSource.getTargetClass();
		
		if (beanFactory != null) 
			return beanFactory.getType(targetBeanName);
		
		return null;
	}

	public boolean isSingleton() {
		return singleton;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = (ConfigurableBeanFactory) beanFactory;
	}

	public String getTargetBeanName() {
		return targetBeanName;
	}

	public void setTargetBeanName(String targetBeanName) {
		this.targetBeanName = targetBeanName;
	}
}

