package info.joseluismartin.xml;

/**
 * Interface to marshal/unmarshall java objects to/from xml.
 * Use to hide the xml binding framework.
 * 
 * @author Jose Luis Martin - (jolmarting@matchmind.es)
 */
public interface XMLMapper {
	
	/**
	 * Serialize an object to xml string
	 * @param obj the object to serialize
	 * @return xml string representation of obj.
	 */
	String serialize(Object obj);
	/**
	 * Deserialize a xml string to an java object
	 * @param xml to deserialize
	 * @return a java object.
	 */
	Object deserialize(String xml);
}
