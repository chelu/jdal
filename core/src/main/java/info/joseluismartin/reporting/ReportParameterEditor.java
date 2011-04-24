/**
 * 
 */
package info.joseluismartin.reporting;

import javax.swing.JComponent;

/**
 * Interface to be implemented by editors for the parameters in a report
 * @author Jose A. Corbacho
 *
 */
public interface ReportParameterEditor{

	/**
	 * Get the actual editor
	 * @return
	 */
	public JComponent getEditor();
	
	/**
	 * Get the value in the editor
	 * @return
	 */
	public Object getValue() throws Exception;

}
