/*
 * Copyright 2002-2010 the original author or authors.
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

import javax.swing.JComboBox;

/**
 * Binder for JComboBox
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class ComboBinder extends AbstractBinder implements ActionListener {
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doBind(Object component) {
		JComboBox combo = (JComboBox) component;
		combo.addActionListener(this);
		
	}

	/**
	 * {@inheritDoc}
	 */
	public void actionPerformed(ActionEvent e) {
	}

	/**
	 * {@inheritDoc}
	 */
	public void doRefresh() {
		((JComboBox) component).setSelectedItem(getValue());
		
	}

	/**
	 * {@inheritDoc}
	 */
	public void doUpdate() {
		JComboBox combo = (JComboBox) component;
		setValue(combo.getSelectedItem());
	}

}
