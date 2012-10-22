/*
 * Copyright 2009-2012 the original author or authors.
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
package info.joseluismartin.remoting.rmi;

import info.joseluismartin.remoting.ReferenceRemoteInvocationFactory;
import info.joseluismartin.remoting.RemoteClient;
import info.joseluismartin.remoting.RemoteClientAdvisor;
import info.joseluismartin.remoting.RemoteReference;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang.StringUtils;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.remoting.rmi.RmiClientInterceptor;
import org.springframework.remoting.rmi.RmiInvocationHandler;

/**
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 *
 */
public class RmiProxyFactoryBean extends RmiClientInterceptor  
	implements FactoryBean<Object>, BeanClassLoaderAware { 
	
	private Object serviceProxy;
	private boolean useNativeReferences = true;
	
	public RmiProxyFactoryBean() {
		this.setRemoteInvocationFactory(new ReferenceRemoteInvocationFactory());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		
		if (getServiceInterface() == null) {
			throw new IllegalArgumentException("Property 'serviceInterface' is required");
		}
		ProxyFactory pf = new ProxyFactory(new Class[] { getServiceInterface(), RemoteClient.class });
		pf.addAdvisor(new RemoteClientAdvisor(getRemoteReference()));
		pf.addAdvice(this);
		serviceProxy = pf.getProxy();
	}

	/**
	 * {@inheritDoc}
	 */
	public Object getObject() throws Exception {
		return serviceProxy;
	}
	/**
	 * {@inheritDoc}
	 */
	public Class<?> getObjectType() {
		return getServiceInterface();
	}
	/**
	 * {@inheritDoc}
	 */
	public boolean isSingleton() {
		return true;
	}

	/**
	 * @return
	 */
	private RemoteReference getRemoteReference() {
		if (isUseNativeReferences()) {
			return new RmiNativeRemoteReference(getStub(), getServiceInterface());
				
		}
		
		return new RmiRemoteReference(this);
	}

	/**
	 * @return the useNativeReferences
	 */
	public boolean isUseNativeReferences() {
		return useNativeReferences;
	}

	/**
	 * @param useNativeReferences the useNativeReferences to set
	 */
	public void setUseNativeReferences(boolean useNativeReferences) {
		this.useNativeReferences = useNativeReferences;
	}
	
	

}
