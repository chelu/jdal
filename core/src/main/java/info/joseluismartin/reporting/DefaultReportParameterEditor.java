package info.joseluismartin.reporting;

import javax.swing.JComponent;
import javax.swing.JTextField;

import org.springframework.beans.SimpleTypeConverter;

public class DefaultReportParameterEditor implements ReportParameterEditor {
	
	private JComponent editor;
	private Class<?> clazz;
	
	public DefaultReportParameterEditor(Class<?> valueClass) {
		this.clazz = valueClass;
	}

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
		SimpleTypeConverter stc = new SimpleTypeConverter();
		return stc.convertIfNecessary(strValue, clazz);
	}


}
