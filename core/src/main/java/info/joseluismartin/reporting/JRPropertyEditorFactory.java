/**
 * 
 */
package info.joseluismartin.reporting;

import java.sql.Date;

import net.sf.jasperreports.engine.JRParameter;

/**
 * @author Jose A. Corbacho
 *
 */
public class JRPropertyEditorFactory {

	public ReportParameterEditor getParameterEditor(JRParameter parameter) {
		System.out.println("Creating editor for parameter " + parameter.getName() + " with class " + parameter.getValueClass().getName());
		System.out.println("Parameter value class: " + parameter.getValueClass().getName());
		System.out.println("Parameter value class canonical name: " + parameter.getValueClass().getCanonicalName());
		Class<?> valueClass = parameter.getValueClass();
		ReportParameterEditor editor = null;
		if (valueClass.getName().equals("java.lang.String"))
			editor = new ReportStringParameterEditor();
		else if (valueClass.getName().equals("java.lang.Integer"))
			editor = new ReportIntegerParameterEditor();
		else if (valueClass.getName().equals("java.lang.Long")) {
			editor = new ReportLongParameterEditor();
			System.out.println("Creando editor para LONG");
		
		}
		else if (valueClass.isAssignableFrom(Date.class)) {
			editor = new DateReportParameterEditor();
		}
			
		else editor = new DefaultReportParameterEditor(valueClass);
		
		return editor;
	}

}
