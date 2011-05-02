/*
 * Copyright 2008-2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
