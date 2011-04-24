package info.joseluismartin.xml;

/**
 * Result of a schema validation 
 * 
 * @author Jose Luis Martin - (jolmarting@matchmind.es)
 */
public class ValidationResult {
	/** true if validation was valid */
	private boolean valid = false;
	/** Error Message */
	private String message = "";
	
	/** 
	 * Default Ctor.
	 */
	public ValidationResult() {} 

	/**
	 * Ctor
	 * @param valid true if document is valid
	 * @param message Informative message for logs.
	 */
	public ValidationResult(boolean valid, String message) {
		this.valid = valid;
		this.message = message;
	}
	
	/** 
	 * Ctor
	 * @param valid true if document is valid.
	 */
	public ValidationResult(boolean valid) {
		this.valid = valid;
	}
	
	/**
	 * @return the valid
	 */
	public boolean isValid() {
		return valid;
	}
	
	/**
	 * @param valid the valid to set
	 */
	public void setValid(boolean valid) {
		this.valid = valid;
	}
	
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
}
