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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;

import org.apache.catalina.websocket.StreamInbound;
import org.jdal.remoting.websocket.WebSocketServiceExporter;

/**
 * Tomcat StreamInbound to handle remote service invocations.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 * @since 2.0
 */
public class RemoteInvocationStreamInbound extends StreamInbound {

	private WebSocketServiceExporter exporter;
	
	/**
	 * @param exporter the service exporter to use
	 */
	public RemoteInvocationStreamInbound(WebSocketServiceExporter exporter) {
		this.exporter = exporter;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onBinaryData(InputStream is) throws IOException {
		try {
			exporter.handle(is, getOuputStream());
		} catch (ClassNotFoundException e) {
			
		}
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onTextData(Reader arg0) throws IOException {
		// NOP
	}
	
	protected OutputStream getOuputStream() {
		return new WsOutboundOutputStream(getWsOutbound());
	}

}
