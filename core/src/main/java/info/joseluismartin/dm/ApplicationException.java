package info.joseluismartin.dm;

public class ApplicationException extends Exception {
	
	private static final long serialVersionUID = 4169666737177254485L;
	
	public ApplicationException() {
		super();
	}

	public ApplicationException(String message, Throwable cause) {
		super(message, cause);
	}

	public ApplicationException(String message) {
		super(message);
	}

	public ApplicationException(Throwable cause) {
		super(cause);
	}

	

}
