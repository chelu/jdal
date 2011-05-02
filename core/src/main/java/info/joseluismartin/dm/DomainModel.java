/*
 * Copyright 2005-2011 the original author or authors.
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
package info.joseluismartin.dm;

import java.io.Serializable;
import java.util.Arrays;


/**
 * Base class for Domain Models
 * A domain model contain persistent data and behavior of bussines logic.
 *
 * @author Jose Luis Martin - (chelu.es@gmail.com)
 */

public class DomainModel implements Serializable {
	
	private static final long serialVersionUID = -4948756168601603097L;
	public static final int INVALID = -1;
	public static final int LOADING = -2;
	protected Key key;	
	int serial = LOADING;
	
	
	protected DomainModel() {
		key = new Key(0);
	}
	
	protected DomainModel(Key id) {
		key = id;
	}
	
	public Key getKey() {
		return key;
	}
	
	public void setKey(Key id) {
		key = id;
	}

	public Object[] getKeyAsArray() {
		return key.getFields();
	}

	// Order must be the same as parameters in update query
	public Object[] getValuesAsArray() {
		return new Object[] {};
	}
	
	/**
	 * Test identity of data & Key
	 */
	public boolean equals(DomainModel dm) {
		if (dm == null)
			return false;		
		if (!dm.getKey().equals(key))
			return false;
		if (!Arrays.equals(getValuesAsArray(), dm.getValuesAsArray()))
			return false;
		
		return true;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("Key id: ");
		Object[] keys = getKeyAsArray();
		Object[] values = getValuesAsArray();
		
		for (int i = 0; i < keys.length; i++) 
			sb.append(keys[i] + " ");
		
		sb.append("\nValues: ");
		
		for (int i = 0; i < values.length; i++)
			sb.append(values[i] +  " ");
	
		return sb.toString();
	}
	
	
	public boolean hasKey() {
		// FIXME: use null keys for new values 
		if (key == null || key.getFields() == null) 
			return false;
		if (key.getFields().length == 0)
			return false;
		if (key.getFields()[0] instanceof Long && ((Long) key.getFields()[0]).longValue() == 0)
			return false;
		
		return true;
	}

	public int getSerial() {
		return serial;
	}
	
	public void setSerial(int serial) {
		this.serial = serial;
	}

	public boolean isLoading() {
		return serial == LOADING;
	}
	
	
	public void invalidate() {
		serial = INVALID;
	}

	/**
	 * True if model must be reloaded for database serial
	 * @param s
	 * @return
	 */
	public boolean mustReload(int s) {
		return serial == INVALID || (!isLoading() && getSerial() != s);
	}
}

