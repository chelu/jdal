package info.joseluismartin.jdbc;

/**
 * Generic Application Exception for use en jdbc package.
 * 
 * @author Jose Luis Martin - (jolmarting@matchmind.es)
 */

public class ApplicationException extends RuntimeException {
	
	/** serial */
	private static final long serialVersionUID = 4169666737177254485L;
	
	/** Ctor */
	public ApplicationException() {
		super();
	}
	
	/** 
	 * Ctor
	 * @param message the exception message
	 * @param cause de upper cause 
	 */
	public ApplicationException(String message, Throwable cause) {
		super(message, cause);
	}
	
	/**
	 *  Ctor
	 *  @param message the exception message 
	 */
	public ApplicationException(String message) {
		super(message);
	}
	
	/** 
	 * Ctor
	 * @param cause the upper cause
	 */
	public ApplicationException(Throwable cause) {
		super(cause);
	}
}
