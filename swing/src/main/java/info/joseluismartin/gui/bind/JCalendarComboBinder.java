/**
 * 
 */
package info.joseluismartin.gui.bind;

import info.joseluismartin.gui.bind.AbstractBinder;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import org.freixas.jcalendar.JCalendarCombo;

/**
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 *
 */
public class JCalendarComboBinder extends AbstractBinder {


	public void doRefresh() {
		JCalendarCombo combo = (JCalendarCombo) component;
		Date date = (Date) getValue();
		combo.setDate(date);
		if (date == null) { // jcalendar don't clear combo when setting null date, fix it.
			combo.repaint();
		}
	}

	public void doUpdate() {
		JCalendarCombo combo = (JCalendarCombo) component;
		setValue(combo.getDate());
	}

}
