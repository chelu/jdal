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

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyEditor;

import javax.swing.text.JTextComponent;

import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.beans.PropertyEditorRegistrySupport;

/**
 * Binder for JComponentText
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class TextComponentBinder extends AbstractBinder implements FocusListener {
	
	private PropertyEditorRegistry registry = new PropertyEditorRegistrySupport();
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doBind(Object component) {
		((JTextComponent) component).addFocusListener(this);
		
	}

	/**
	 * {@inheritDoc}
	 */
	public void refresh() {
		Object value = getValue();
		String text = getAsText(value);
		((JTextComponent) component).setText(text);
		
	}

	private String getAsText(Object value) {
		String text = "";
		
		if (value != null) {
			PropertyEditor pe = registry.findCustomEditor(value.getClass(), null);
			if (pe != null) {
				pe.setValue(value);
				text = pe.getAsText();
			}
			else {
				text = value.toString();
			}
		}
		return text;
	}

	/**
	 * {@inheritDoc}
	 */
	public void update() {
		String newValue = ((JTextComponent) component).getText();
		setValue(newValue);
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void focusGained(FocusEvent e) {
	}

	/**
	 * {@inheritDoc}
	 */
	public void focusLost(FocusEvent e) {
		
	}
}
