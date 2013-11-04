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
import java.io.OutputStream;

import org.apache.catalina.websocket.WsOutbound;

/**
 * Tomcat WsOutbound OuputStream Adapter
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 * @since 2.0
 */
public class WsOutboundOutputStream extends OutputStream {
	
	WsOutbound target;

	public WsOutboundOutputStream(WsOutbound target) {
		this.target = target;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void write(int b) throws IOException {
		target.writeBinaryData(b);
	}

}
