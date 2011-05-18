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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JToggleButton;

/**
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 *
 */
public class ToggleButtonAccessor extends AbstractControlAccessor implements ActionListener {

	public ToggleButtonAccessor(JToggleButton button) {
		super(button);
		button.addActionListener(this);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Boolean getControlValue() {
		return getControl().isSelected();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setControlValue(Object value) {
		getControl().setSelected(convertIfNecessary(value, Boolean.class));
	}
	
	public JToggleButton getControl() {
		return (JToggleButton) super.getControl();
	}

	/**
	 * {@inheritDoc}
	 */
	public void actionPerformed(ActionEvent e) {
		fireControlChange();
		
	}

}
