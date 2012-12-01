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

import info.joseluismartin.beans.SimpleTypeConverter;
import info.joseluismartin.gui.View;

import java.awt.Component;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.text.JTextComponent;

import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.beans.TypeConverter;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.propertyeditors.CustomDateEditor;

/**
 * Base class for ControlAccessors
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public abstract class AbstractControlAccessor implements ControlAccessor {
	
	/** control object */
	private Object control;
	/** control change listener list */
	private List<ControlChangeListener> listeners = new ArrayList<ControlChangeListener>();
	/** type converter */
	private TypeConverter converter = new SimpleTypeConverter();
	
	/**
	 * Default ctor.
	 */
	public AbstractControlAccessor() {
		this(null);
	}
	
	/**
	 * Create a ContolAccesor and set the control
	 * @param control the control to set
	 */
	public AbstractControlAccessor(Object control)  {
		setControl(control);
		if (converter instanceof PropertyEditorRegistry) {
			PropertyEditorRegistry per = (PropertyEditorRegistry) converter;
			per.registerCustomEditor(Date.class, 
					new CustomDateEditor(SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT), true));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void addControlChangeListener(ControlChangeListener l) {
		if (!listeners.contains(l))
			listeners.add(l);
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void removeControlChangeListener(ControlChangeListener l) {
		if (!listeners.contains(l))
			listeners.remove(l);
		
	}
	
	/**
	 * Convert Object to required type using <code>SimpleTypeConverter</code>
	 * @param <T>
	 * @param value
	 * @param requiredType
	 * @return
	 * @throws TypeMismatchException
	 * @see org.springframework.beans.TypeConverter#convertIfNecessary(java.lang.Object, java.lang.Class)
	 */
	protected <T> T convertIfNecessary(Object value, Class<T> requiredType) throws TypeMismatchException {
		return converter.convertIfNecessary(value, requiredType);
	}
	
	/**
	 * Notifiy Listeners that control value has changed	
	 */
	protected void fireControlChange() {
		for (ControlChangeListener l : listeners)
			l.controlChange(new ControlEvent(getControl()));
	}
	
	/**
	 * @return the control
	 */
	public Object getControl() {
		return control;
	}

	/**
	 * @param control the control to set
	 */
	public void setControl(Object control) {
		this.control = control;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setEnabled(boolean enabled) {
		if (control instanceof Component) 
			((Component) control).setEnabled(enabled);
		else if (control instanceof View<?>) 
			((View<?>) control).enableView(enabled);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isEnabled() {
		if (control instanceof Component) 
			return ((Component) control).isEnabled();
		
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isTextControl() {
		return 
				control instanceof JLabel ||
				control instanceof JTextComponent;
	}

}
