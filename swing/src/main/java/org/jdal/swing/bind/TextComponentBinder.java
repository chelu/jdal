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
package org.jdal.swing.bind;

import java.beans.PropertyEditor;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.text.JTextComponent;

import org.jdal.ui.bind.AbstractBinder;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.beans.PropertyEditorRegistrySupport;
import org.springframework.beans.propertyeditors.CustomDateEditor;

/**
 * Binder for JComponentText
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class TextComponentBinder extends AbstractBinder {
	
	private PropertyEditorRegistry registry = new PropertyEditorRegistrySupport();

	public TextComponentBinder() {
		registry.registerCustomEditor(Date.class,
				new CustomDateEditor(SimpleDateFormat.getDateTimeInstance(), true));
	}
	/**
	 * {@inheritDoc}
	 */
	public void doRefresh() {
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
	public void doUpdate() {
		String newValue = ((JTextComponent) component).getText();
		setValue(newValue.trim());
		
	}
}
