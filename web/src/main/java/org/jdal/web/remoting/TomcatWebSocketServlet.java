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
package org.jdal.web.remoting;

import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;
import org.jdal.remoting.websocket.WebSocketServiceExporter;

/**
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 *
 */
public class TomcatWebSocketServlet extends WebSocketServlet {

	private WebSocketServiceExporter serviceExporter;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected StreamInbound createWebSocketInbound(String arg0, HttpServletRequest arg1) {
		return new RemoteInvocationStreamInbound(serviceExporter);
	}

}
