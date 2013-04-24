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
package org.jdal.remoting.rmi;

import java.io.Serializable;
import java.rmi.RemoteException;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang.StringUtils;
import org.springframework.remoting.rmi.RmiClientInterceptorUtils;
import org.springframework.remoting.rmi.RmiInvocationHandler;
import org.springframework.remoting.support.RemoteInvocationBasedAccessor;


/**
 * Aop Alliance Interceptor that delegate calls to a RmiInvocationHandler.
 *
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class RmiServiceInterceptor extends RemoteInvocationBasedAccessor 
	implements MethodInterceptor, Serializable  {
	
	private RmiInvocationHandler invocationHandler; 
	private String serviceName;
	
	public RmiServiceInterceptor(RmiInvocationHandler invocationHandler) {
		this(invocationHandler, null);
	}
	
	public RmiServiceInterceptor(RmiInvocationHandler invocationHandler, String serviceName) {
		this.invocationHandler = invocationHandler;
		this.serviceName = serviceName;
	}

	/**
	 * {@inheritDoc}
	 */
	public Object invoke(MethodInvocation invocation) throws Throwable {
		try {
			return invocationHandler.invoke(createRemoteInvocation(invocation));
		}
		catch (RemoteException ex) {
				throw RmiClientInterceptorUtils.convertRmiAccessException(
				    invocation.getMethod(), ex, RmiClientInterceptorUtils.isConnectFailure(ex), 
				    extractServiceUrl());
			}
	}

	/**
	 * Try to extract service Url from invationHandler.toString() for exception info
	 * @return Service Url
	 */
	private String extractServiceUrl() {
		String toParse = invocationHandler.toString();
		String url = "rmi://" + StringUtils.substringBefore(
				StringUtils.substringAfter(toParse, "endpoint:["), "]");
		
		if (serviceName != null)
			url = StringUtils.substringBefore(url, ":") + "/" + serviceName;
		
		return url;
	}
}
