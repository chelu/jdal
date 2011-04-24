package info.joseluismartin.xml;

/**
 * XML Mapping Exception
 * 
 * @author Jose Luis Martin - (jolmarting@matchmind.es)
 */
public class XMLMappingException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public XMLMappingException() {
		super();
	}

	public XMLMappingException(String message, Throwable cause) {
		super(message, cause);
	}

	public XMLMappingException(String message) {
		super(message);
	}

	public XMLMappingException(Throwable cause) {
		super(cause);
	}

}
