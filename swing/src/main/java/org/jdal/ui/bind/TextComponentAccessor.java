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
package org.jdal.ui.bind;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

/**
 * ControlAccesor for JTextComponents.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 * @since 1.1
 * @see org.jdal.ui.bind.ControlAccessor
 */
public class TextComponentAccessor extends AbstractControlAccessor implements DocumentListener {
	
	/**
	 * @param control
	 */
	public TextComponentAccessor(Object control) {
		super(control);
		getControl().getDocument().addDocumentListener(this);
	}

	/**
	 * {@inheritDoc}
	 */
	public String getControlValue() {
		return getControl().getText();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setControlValue(Object value) {
		if (value != getControlValue()) {
			getControl().setText(convertIfNecessary(value, String.class));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public JTextComponent getControl() {
		return (JTextComponent) super.getControl();
	}

	/**
	 * {@inheritDoc}
	 */
	public void insertUpdate(DocumentEvent e) {
		fireControlChange();
	}

	/**
	 * {@inheritDoc}
	 */
	public void removeUpdate(DocumentEvent e) {
		fireControlChange();		
	}

	/**
	 * {@inheritDoc}
	 */
	public void changedUpdate(DocumentEvent e) {
		fireControlChange();		
	}
}
