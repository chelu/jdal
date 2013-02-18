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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdal.remoting.RemoteClient;
import org.jdal.remoting.UrlBasedRemoteReference;
import org.springframework.remoting.support.UrlBasedRemoteAccessor;

/**
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 *
 */
public class RmiRemoteReference extends UrlBasedRemoteReference {

	private static final Log log = LogFactory.getLog(RmiRemoteReference.class);
	
	public RmiRemoteReference(UrlBasedRemoteAccessor accessor) {
		super(accessor);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public RemoteClient createRemoteClient() {
		RmiProxyFactoryBean factory = new RmiProxyFactoryBean();
		factory.setServiceInterface(getServiceInterface());
		factory.setServiceUrl(getServiceUrl());
		factory.afterPropertiesSet();

		try {
			return (RemoteClient) factory.getObject();
		} catch (Exception e) {
			log.error(e);
			return null;
		}
	}

}
