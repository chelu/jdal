/*
 * Copyright 2009-2012 Jose Luis Martin.
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
package org.jdal.remoting.websocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import org.springframework.remoting.rmi.RemoteInvocationSerializingExporter;
import org.springframework.remoting.support.RemoteInvocation;
import org.springframework.remoting.support.RemoteInvocationResult;

/**
 * Remote Service Exporter using WebSockets
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 * @since 2.0
 */
public class WebSocketServiceExporter extends RemoteInvocationSerializingExporter {
	
	/**
	 * Handle binary data
	 * @param is 
	 * @param os 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void handle(InputStream is, OutputStream os) throws IOException, ClassNotFoundException {
		RemoteInvocation invocation = readRemoteInvocation(is);
		writeRemoteInvocationResult(invokeAndCreateResult(invocation, getProxy()), os);
	}
	
	protected RemoteInvocation readRemoteInvocation(InputStream is)
			throws IOException, ClassNotFoundException {

		ObjectInputStream ois = createObjectInputStream(decorateInputStream(is));
		
		return doReadRemoteInvocation(ois);
	}
	
	protected InputStream decorateInputStream(InputStream is) throws IOException {
		return is;
	}
	
	protected OutputStream decorateOutputStream(OutputStream os) throws IOException {
		return os;
	}
	
	protected void writeRemoteInvocationResult(RemoteInvocationResult result, OutputStream os)
			throws IOException {

		ObjectOutputStream oos = createObjectOutputStream(decorateOutputStream(os));
		doWriteRemoteInvocationResult(result, oos);
	}


}
