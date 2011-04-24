/**
 * 
 */
package info.joseluismartin.reporting;

import javax.swing.JComponent;
import javax.swing.JTextField;

/**
 * @author Jose A. Corbacho
 *
 */
public class ReportStringParameterEditor implements ReportParameterEditor {

	private JComponent editor;
	
	/* (non-Javadoc)
	 * @see info.joseluismartin.reporting.ReportParameterEditor#getEditor()
	 */
	public JComponent getEditor() {
		if (editor == null) editor = new JTextField(15);
		return editor;
	}

	/* (non-Javadoc)
	 * @see info.joseluismartin.reporting.ReportParameterEditor#getValue()
	 */
	public Object getValue() {
		return ((JTextField) editor).getText();
	}

}
