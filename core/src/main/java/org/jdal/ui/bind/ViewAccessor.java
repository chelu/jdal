/*
 * Copyright 2009-2015 Jose Luis Martin
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

import org.jdal.ui.View;

/**
 * {@link ControlAccessor} for {@link View Views}
 * 
 * @author Jose Luis Martin
 * @since 1.2
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class ViewAccessor extends AbstractControlAccessor {


	public ViewAccessor(Object control) {
		super(control);
	}

	/**
	 * {@inheritDoc}
	 */
	public Object getControlValue() {
		View v = getControl();
		v.update();
		return v.getModel();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setControlValue(Object value) {
		View v = getControl();
		v.setModel(value);
		v.refresh();
	}
	
	public View  getControl() {
		return (View) super.getControl();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addControlChangeListener(ControlChangeListener l) {
		getControl().addControlChangeListener(l);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeControlChangeListener(ControlChangeListener l) {
		getControl().removeControlChangeListener(l);
	}

}
