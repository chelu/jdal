/*
 * Copyright 2008-2011 the original author or authors.
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
package info.joseluismartin.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Serialize/Deserialize utility library class.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */

public abstract class Serializer {
	/** serial */
	private static Log log = LogFactory.getLog(Serializer.class);
	/**
	 * Serialize Serializable to byte[]
	 * @param ser Serializable to serialize
	 * @return byte array wiht serilized data
	 * @throws IOException if fail
	 */
	public static byte[] serialize(Serializable ser) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(ser);
		return baos.toByteArray();
	}
	
	/**
	 * Deserialize Object from byte[]
	 * @param bytes serialized data
	 * @return Object or null on exception
	 */
	public static Object deSerialize(byte[] bytes) {
		Object obj = null;
		ByteArrayInputStream bais = new java.io.ByteArrayInputStream(bytes);
		try {
			ObjectInputStream ois = new java.io.ObjectInputStream(bais);
			obj = ois.readObject();
		} catch (IOException e) {
			log.error(e);
		} catch (ClassNotFoundException e) {
			log.error(e);
		}
		return obj;
	}
}
