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
package info.joseluismartin.gui.validation;

import info.joseluismartin.gui.Binder;
import info.joseluismartin.gui.bind.PropertyBinder;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;

import org.springframework.context.MessageSource;
import org.springframework.validation.FieldError;

/**
 * ErrorProcessor that change background color of JComponent and add a tooltip with error message 
 * on binding errors.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 * @since 1.1 
 * @see ErrorProcessor
 * @see info.joseluismartin.gui.AbstractView
 */
public class BackgroundErrorProcessor implements ErrorProcessor {

	private Map<JComponent, Color> colorMap = new HashMap<JComponent, Color>();
	private Color errorColor = new Color(255, 130, 130);
	private MessageSource messageSource;
	
	public void processError(Binder<?> binder, FieldError error) {
		if (binder instanceof PropertyBinder) {
			Object o = ((PropertyBinder) binder).getComponent();
			processError(o, error);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public  void processError(Object control, FieldError error) {
		if (control instanceof JComponent) {
			JComponent c = (JComponent) control;
			colorMap.put(c, c.getBackground());
			((JComponent) c).setBackground(errorColor);
			if (messageSource != null)
				c.setToolTipText(messageSource.getMessage(error, null));
		}
	}
		
	/**
	 * {@inheritDoc}
	 */
	public void reset() {
		for (JComponent c : colorMap.keySet()) {
			c.setBackground(colorMap.get(c));
			c.setToolTipText(null);
		}
		colorMap.clear();
	}

	/**
	 * @return the errorColor
	 */
	public Color getErrorColor() {
		return errorColor;
	}


	/**
	 * @param errorColor the errorColor to set
	 */
	public void setErrorColor(Color errorColor) {
		this.errorColor = errorColor;
	}


	/**
	 * @return the messageSource
	 */
	public MessageSource getMessageSource() {
		return messageSource;
	}

	/**
	 * @param messageSource the messageSource to set
	 */
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
}
