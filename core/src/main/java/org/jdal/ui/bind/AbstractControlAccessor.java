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

import java.awt.Component;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.text.JTextComponent;

import org.jdal.beans.SpringConverter;
import org.jdal.ui.View;
import org.springframework.beans.propertyeditors.CustomDateEditor;

/**
 * Base class for {@link AbstractControlAccessor}
 * 
 * @author Jose Luis Martin.
 * @since 1.1
 */
public abstract class AbstractControlAccessor extends SpringConverter implements ControlAccessor {
	
	/** control object */
	private Object control;
	/** control change listener list */
	private List<ControlChangeListener> listeners = new ArrayList<ControlChangeListener>();
	
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
		registerCustomEditor(Date.class, 
					new CustomDateEditor(SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT), true));
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
