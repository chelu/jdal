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
 * @author Jose Luis Martin - (jolmarting@matchmind.es)
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
