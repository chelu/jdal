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

import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;

/**
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 *
 */
public class DummyRegistry implements Registry {

	/**
	 * {@inheritDoc}
	 */
	public Remote lookup(String name) throws RemoteException, NotBoundException, AccessException {
		throw new NotBoundException("This registry is dummy");
	}

	/**
	 * {@inheritDoc}
	 */
	public void bind(String name, Remote obj) throws RemoteException, AlreadyBoundException, AccessException {
		
	}

	/**
	 * {@inheritDoc}
	 */
	public void unbind(String name) throws RemoteException, NotBoundException, AccessException {
		
	}

	/**
	 * {@inheritDoc}
	 */
	public void rebind(String name, Remote obj) throws RemoteException, AccessException {
		
	}

	/**
	 * {@inheritDoc}
	 */
	public String[] list() throws RemoteException, AccessException {
		return new String[0];
	}

}
