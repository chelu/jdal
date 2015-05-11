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
package org.jdal.remoting.caucho;

import org.jdal.remoting.RemoteClient;
import org.jdal.remoting.RemoteClientAdvisor;
import org.jdal.remoting.RemoteReference;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.remoting.caucho.BurlapClientInterceptor;

import com.caucho.burlap.client.BurlapProxyFactory;

/**
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 *
 */
@SuppressWarnings("deprecation")
public class BurlapProxyFactoryBean extends BurlapClientInterceptor implements FactoryBean<Object>, RemoteClient {

	private Object serviceProxy;
	private BurlapProxyFactory proxyFactory;


	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		ProxyFactory pf = new ProxyFactory(new Class[] {getServiceInterface(), RemoteClient.class});
		pf.addAdvisor(new RemoteClientAdvisor(getRemoteReference()));
		pf.addAdvice(this);
		this.serviceProxy = pf.getProxy(getBeanClassLoader());
	}


	public RemoteReference getRemoteReference() {
		return new BurlapRemoteReference(this);
	}
	
	// Override to get access to proxyFactory
	@Override
	public void setProxyFactory(BurlapProxyFactory proxyFactory) {
		this.proxyFactory = (proxyFactory != null ? proxyFactory : new BurlapProxyFactory());
		super.setProxyFactory(this.proxyFactory);
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
	
	public boolean isOverloadEnabled() {
		return proxyFactory.isOverloadEnabled();
	}
	
}
