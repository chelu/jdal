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

/**
 * Interface for generic access to ui controls
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public interface ControlAccessor {

	/**
	 * Gets the control value
	 * @return the control value
	 */
	Object getControlValue();
	
	/**
	 * Sets the control value
	 * @param value the value to set
	 */
	void setControlValue(Object value);
	
	/**
	 * Add a ControlChangeListener
	 * @param l the ControlChangeListener to add.
	 */
	void addControlChangeListener(ControlChangeListener l);
	
	/**
	 * Remove a ControlChangeListener
	 * @param l the ControlChangeListener to remove
	 */
	void removeControlChangeListener(ControlChangeListener l);
	
	/**
	 * Enable/disable the control
	 * @param enabled true if enabled
	 */
	void setEnabled(boolean enabled);
	
	/**
	 * Test control enabled state
	 * @return true if control is enabled
	 */
	boolean isEnabled();
	
	/**
	 * Test if control is a TextControlAccessor.
	 * @return true if you want to format text in the control, false otherwise.
	 */
	boolean isTextControl();
}
