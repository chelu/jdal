/**
 * 
 */
package info.joseluismartin.reporting;

/**
 * @author Jose A. Corbacho
 *
 */
public class ReportingException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7457804399442640547L;

	public ReportingException(){
		super();
	}
	
	public ReportingException(String message, Throwable origin){
		super(message, origin);
	}
	
	public ReportingException(String message){
		super(message);
	}
}
