package info.joseluismartin.table;

/**
 * Result of validation
 * 
 * @author Manuel Jesus Antunez - (mjantunez@matchmind.es)
 */
public class ValidationResult {

	private boolean valid = true;
	private String message;
	private String id;
	
	/**
	 * Default Constructor
	 */
	public ValidationResult() {
	}
	
	/**
	 * Ctor
	 * @param valid true if valid
	 * @param message the error message
	 */
	public ValidationResult(boolean valid, String message) {
		this.valid = valid;
		this.message = message;
	}
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
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

