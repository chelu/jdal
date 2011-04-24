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
public class ReportLongParameterEditor implements ReportParameterEditor {

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
	public Object getValue() throws Exception{
		String strValue = ((JTextField) editor).getText();
		// Convert to number
		return Long.parseLong(strValue);
	}


}
