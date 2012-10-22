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

import info.joseluismartin.remoting.RemoteClient;
import info.joseluismartin.remoting.RemoteClientAdvisor;
import info.joseluismartin.remoting.UrlBasedRemoteReference;

import java.rmi.Remote;

import org.springframework.aop.framework.ProxyFactory;
import org.springframework.remoting.rmi.RmiInvocationHandler;

/**
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 *
 */
public class RmiNativeRemoteReference extends UrlBasedRemoteReference {

	private Remote remoteService;
	
	public RmiNativeRemoteReference(Remote remoteService) {
		this(remoteService, null);
	}

	/**
	 * @param remoteService
	 * @param serviceInterface
	 */
	@SuppressWarnings("rawtypes")
	public RmiNativeRemoteReference(Remote remoteService, Class serviceInterface) {
		this.remoteService = remoteService;
		setServiceInterface(serviceInterface);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public RemoteClient createRemoteClient() {
		ProxyFactory pf = null;
		if (getServiceInterface().isAssignableFrom(remoteService.getClass())) {
			pf = new ProxyFactory(remoteService);
		}
		else {
			pf = new ProxyFactory(getServiceInterface(), 
					new RmiServiceInterceptor((RmiInvocationHandler) remoteService));
		}
		
		pf.addInterface(RemoteClient.class);
		pf.addAdvisor(new RemoteClientAdvisor(this));
		
		return (RemoteClient) pf.getProxy();
	}

}
