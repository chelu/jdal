/*
 * Copyright 2009-2013 Jose Luis Martin.
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
package org.jdal.aop;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.ObjectUtils;

/**
 * Default serializable object implementation.
 * 
 * @author Jose Luis Martin
 * @since 2.0
 */
public class DefaultSerializableObject implements SerializableObject {
	
	private static final Log log = LogFactory.getLog(DefaultSerializableObject.class);
	private static final Map<String, Object> serializedObjects = new ConcurrentHashMap<String, Object>();
	private Object serializedObject;
	

	public DefaultSerializableObject() {
		
	}
	
	public DefaultSerializableObject(Object serializedObject) {
		this.serializedObject = serializedObject;
	}
	
	public Object writeReplace() throws ObjectStreamException {
			return new SerializedReference(this.serializedObject);
	}


	private static class SerializedReference implements Serializable {
		
		private final String id;
		
		public SerializedReference(Object obj) {
			this.id = ObjectUtils.identityToString(obj);
			serializedObjects.put(id, obj);
			
			if (log.isDebugEnabled())
				log.debug("Added new serialized reference. serialized objects size [" + serializedObjects.size() + "]");
		}
		
		private Object readResolve() {
			Object ret = serializedObjects.remove(this.id);
			
			if (log.isDebugEnabled())
				log.debug("Removed a serialized reference. serialized objects size [" + serializedObjects.size() + "]");
	
			return ret;
		}
	}


	public Object getSerializedObject() {
		return serializedObject;
	}

	public void setSerializedObject(Object serializedObject) {
		this.serializedObject = serializedObject;
	}
}
