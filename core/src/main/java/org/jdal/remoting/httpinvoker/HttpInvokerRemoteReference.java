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

import org.jdal.remoting.RemoteClient;
import org.jdal.remoting.UrlBasedRemoteReference;
import org.springframework.remoting.httpinvoker.HttpInvokerClientInterceptor;

/**
 * Remote referece for Http Invokers
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class HttpInvokerRemoteReference extends UrlBasedRemoteReference {

	private String codebaseUrl;
	
	public HttpInvokerRemoteReference() {
		super();
	}

	public HttpInvokerRemoteReference(HttpInvokerClientInterceptor accessor) {
		super(accessor);
		this.codebaseUrl = accessor.getCodebaseUrl();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RemoteClient createRemoteClient() {
		HttpInvokerProxyFactoryBean factoryBean = new HttpInvokerProxyFactoryBean();
		factoryBean.setServiceUrl(getServiceUrl());
		factoryBean.setServiceInterface(getServiceInterface());
		factoryBean.setCodebaseUrl(codebaseUrl);
		factoryBean.afterPropertiesSet();
		
		return (RemoteClient) factoryBean.getObject();
	}

	/**
	 * @return the codebaseUrl
	 */
	public String getCodebaseUrl() {
		return codebaseUrl;
	}

	/**
	 * @param codebaseUrl the codebaseUrl to set
	 */
	public void setCodebaseUrl(String codebaseUrl) {
		this.codebaseUrl = codebaseUrl;
	}

}
