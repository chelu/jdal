/*
 * Created on 28-feb-2006
 *
 */

package info.joseluismartin.cmd;

/**
 * A Command Exception
 * 
 * @author Jose Luis Martin - (chelu.es@gmail.com)
 */
public class CommandException extends RuntimeException {
	
	/** serial */
    private static final long serialVersionUID = -4113289411547556084L;
    /** Generic command data */
    private Object arg;

    /**
     * Ctor.
     */
	public CommandException() {
		super();
	}

	/**
	 * Ctor.
	 * @param message exception message
	 * @param cause the upper cause
	 */
	public CommandException(String message, Throwable cause) {
		super(message, cause);
	}

	/** 
	 * Ctor.
	 * @param message exception message
	 */
	public CommandException(String message) {
		super(message);
	}

	/**
	 * Ctor.
	 * @param cause the upper cause
	 */
	public CommandException(Throwable cause) {
		super(cause);
	}
	
	/**
	 * Getter for arg.
	 * @return the arg
	 */
	public Object getArg() {
		return arg;
	}
	
	/**
	 * Setter for arg
	 * @param arg to set
	 */
	public void setArg(Object arg) {
		this.arg = arg;
	}

}
