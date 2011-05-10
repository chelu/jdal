/*
 * Copyright 2009-2011 Jose Lus Martin.
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

import javax.swing.JToggleButton;

/**
 * Property binder for ToggleButtons
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class ToogleButtonBinder extends AbstractBinder {

	/**
	 * {@inheritDoc}
	 */
	public void refresh() {
		((JToggleButton) component).setSelected((Boolean) getValue());
	}

	/**
	 * {@inheritDoc}
	 */
	public void update() {
		setValue(((JToggleButton) component).isSelected());
	}
	
}
