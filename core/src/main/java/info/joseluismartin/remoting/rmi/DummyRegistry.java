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
import java.util.Hashtable;

/**
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 *
 */
public class DummyRegistry implements Registry {

	private Hashtable<String, Remote> registry = new Hashtable<String, Remote>();
	/**
	 * {@inheritDoc}
	 */
	public Remote lookup(String name) throws RemoteException, NotBoundException, AccessException {
		Remote obj = registry.get(name);
		
		if (obj == null)
			throw new NotBoundException(name);
		
		return registry.get(name);
	}

	/**
	 * {@inheritDoc}
	 */
	public void bind(String name, Remote obj) throws RemoteException, AlreadyBoundException, AccessException {
		if (registry.containsKey(name))
			throw new AlreadyBoundException(name);
		
		registry.put(name, obj);
	}

	/**
	 * {@inheritDoc}
	 */
	public void unbind(String name) throws RemoteException, NotBoundException, AccessException {
		Object obj = registry.remove(name);
		
		if (obj == null)
			throw new NotBoundException(name);
	}

	/**
	 * {@inheritDoc}
	 */
	public void rebind(String name, Remote obj) throws RemoteException, AccessException {
		registry.put(name, obj);
	}

	/**
	 * {@inheritDoc}
	 */
	public String[] list() throws RemoteException, AccessException {
		return registry.keySet().toArray(new String[0]);
	}

}
