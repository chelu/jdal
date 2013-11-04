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
package org.jdal.remoting;

import org.springframework.remoting.support.UrlBasedRemoteAccessor;

/**
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 *
 */
@SuppressWarnings("rawtypes")
public abstract class UrlBasedRemoteReference implements RemoteReference {
	
	private String serviceUrl;
	private Class serviceInterface;
	
	public UrlBasedRemoteReference() {
		
	}

	public UrlBasedRemoteReference(UrlBasedRemoteAccessor accessor) {
		setServiceUrl(accessor.getServiceUrl());
		setServiceInterface(accessor.getServiceInterface());
	}
	
	/**
	 * {@inheritDoc}
	 */
	public abstract RemoteClient createRemoteClient();

	/**
	 * @return the serviceUrl
	 */
	public String getServiceUrl() {
		return serviceUrl;
	}

	/**
	 * @param serviceUrl the serviceUrl to set
	 */
	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}

	/**
	 * @return the serviceInterface
	 */
	public Class getServiceInterface() {
		return serviceInterface;
	}

	/**
	 * @param serviceInterface the serviceInterface to set
	 */
	public void setServiceInterface(Class serviceInterface) {
		this.serviceInterface = serviceInterface;
	} 
}
