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
package org.jdal.swing.action;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.ComboBoxEditor;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTextField;

/**
 * JComboBox Listener that refresh the list of items based on text that is typed on
 * the ComboBoxEditor. Implement abstract getList() to provide the new item list.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public abstract class AutoCompletionListener extends KeyAdapter  {
	
	private JComboBox combo;
	
	public AutoCompletionListener() {}

	/**
	 * Create a new AutocompletionListener and attach it to combo
	 * @param combo combobox to add the auto completion listener
	 */
	public AutoCompletionListener(JComboBox combo) {
		listenOn(combo);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		char ch = e.getKeyChar();
		if (ch == KeyEvent.CHAR_UNDEFINED || Character.isISOControl(ch))
			return;
		
		ComboBoxEditor editor = (ComboBoxEditor) combo.getEditor();
		String editing =  ((JTextField) editor.getEditorComponent()).getText();
		combo.removeAllItems();
		addList(getList(editing));
		combo.setPopupVisible(true);
		((JTextField) editor.getEditorComponent()).setText(editing);
		combo.repaint();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void keyTyped(KeyEvent e) {
		char ch = e.getKeyChar();
		if (ch == KeyEvent.CHAR_UNDEFINED || Character.isISOControl(ch))
			return;
		
		if (combo.getSelectedItem() !=  null) {
			combo.setSelectedItem(null);
			combo.getEditor().setItem(null);
		}
	}
	
	/**
	 * Add the AutoCompletionListener to a JComboBox
	 * @param combo the combo to add on.
	 */
	public void listenOn(JComboBox combo) {
		if (this.combo != null) {
			this.combo.getEditor().getEditorComponent().removeKeyListener(this);
		}
		
		this.combo = combo;
		combo.setEditable(true);
		combo.getEditor().getEditorComponent().addKeyListener(this);
	}

	/**
	 * @param model
	 * @param list
	 */
	private void addList(List<?> list) {
		ComboBoxModel model = new DefaultComboBoxModel(list.toArray());
		model.setSelectedItem(null);
		combo.setModel(model);
	}

	/**
	 * @param editing String
	 * @return a List of objects with match editing string
	 */
	protected abstract List<?> getList(String editing);

	
	public JComboBox getCombo() {
		return combo;
	}

	public void setCombo(JComboBox combo) {
		this.combo = combo;
	}
	
}
