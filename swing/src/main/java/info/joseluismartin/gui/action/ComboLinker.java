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
package info.joseluismartin.gui.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;

/**
 * Links two combos by a property name
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class ComboLinker implements ActionListener {
	
		private JComboBox primary;
		private JComboBox dependent;
		private String propertyName;

	public ComboLinker(JComboBox primary, JComboBox dependent, String propertyName) {
		this.primary = primary;
		this.dependent = dependent;
		this.propertyName = propertyName;
		
		primary.addActionListener(this);
	}

	/**
	 * {@inheritDoc}
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	
	@SuppressWarnings("unchecked")
	public void actionPerformed(ActionEvent e) {
		Object selected = primary.getSelectedItem();
		if (selected != null) {
			BeanWrapper wrapper = PropertyAccessorFactory.forBeanPropertyAccess(selected);
			Collection<Object> collection = (Collection<Object>) wrapper.getPropertyValue(propertyName);
			DefaultComboBoxModel model = new DefaultComboBoxModel(new Vector<Object>(collection));
			dependent.setModel(model);
		}
	}
}
