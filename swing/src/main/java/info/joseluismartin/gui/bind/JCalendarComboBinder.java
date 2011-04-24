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
public class JCalendarComboBinder extends AbstractBinder implements ActionListener {

	@Override
	protected void doBind(Object component) {
		((JCalendarCombo) component).addActionListener(this);
		
	}

	public void actionPerformed(ActionEvent e) {
		
		
	}

	public void refresh() {
		JCalendarCombo combo = (JCalendarCombo) component;
		Date date = (Date) getValue();
		combo.setDate(date);
		if (date == null) { // jcalendar don't clear combo when setting null date, fix it.
			combo.repaint();
		}
	}

	public void update() {
		JCalendarCombo combo = (JCalendarCombo) component;
		setValue(combo.getDate());
	}

}
