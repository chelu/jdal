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
package org.jdal.remoting.httpinvoker;

import org.jdal.remoting.ReferenceRemoteInvocationFactory;
import org.jdal.remoting.RemoteClient;
import org.jdal.remoting.RemoteClientAdvisor;
import org.jdal.remoting.RemoteReference;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.FactoryBean;

/**
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class HttpInvokerProxyFactoryBean extends 
	org.springframework.remoting.httpinvoker.HttpInvokerClientInterceptor implements FactoryBean<Object> {
	
	private Object serviceProxy;
	
	public HttpInvokerProxyFactoryBean() {
		setRemoteInvocationFactory(new ReferenceRemoteInvocationFactory());
	}


	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		if (getServiceInterface() == null) {
			throw new IllegalArgumentException("Property 'serviceInterface' is required");
		}
		ProxyFactory pf = new ProxyFactory(new Class[] {getServiceInterface(), RemoteClient.class});
		pf.addAdvisor(new RemoteClientAdvisor(getRemoteReference()));
		pf.addAdvice(this);
		
		this.serviceProxy = pf.getProxy(getBeanClassLoader());
	}


	public Object getObject() {
		return this.serviceProxy;
	}

	public Class<?> getObjectType() {
		return getServiceInterface();
	}

	public boolean isSingleton() {
		return true;
	}
	
	public RemoteReference getRemoteReference() {
		return new HttpInvokerRemoteReference(this);
	}

}
