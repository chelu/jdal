package info.joseluismartin.reporting;

import info.joseluismartin.gui.form.FormUtils;

import javax.swing.JComponent;

import org.freixas.jcalendar.JCalendarCombo;

public class DateReportParameterEditor implements ReportParameterEditor {

	private JCalendarCombo combo = FormUtils.newJCalendarCombo();
		
	public JComponent getEditor() {
		return combo;
	}

	public Object getValue() throws Exception {
		return combo.getDate();
	}
}
