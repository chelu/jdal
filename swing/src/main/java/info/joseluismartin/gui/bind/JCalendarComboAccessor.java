/*
 * Copyright 2009-2011 the original author or authors.
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

import java.util.Date;

import org.freixas.jcalendar.JCalendarCombo;

/**
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 *
 */
public class JCalendarComboAccessor extends ComboAccessor {
	
	public JCalendarComboAccessor(Object combo) {
		super(combo);
		if (combo != null)
			getControl().addActionListener(this);
	}

	/**
	 * {@inheritDoc}
	 */
	public Object getControlValue() {
		return getControl().getDate();
		
	}

	/**
	 * {@inheritDoc}
	 */
	public void setControlValue(Object value) {
		Date date = (Date) value;
		getControl().setDate(date);
		if (date == null) { 
			// jcalendar don't clear combo when setting null date, fix it.
			getControl().repaint();
		}
		
	}

	public JCalendarCombo getControl() {
		return (JCalendarCombo) super.getControl();
	}

}
