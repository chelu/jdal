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
package org.jdal.swing.bind;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import org.jdal.swing.Selector;
import org.jdal.ui.bind.AbstractControlAccessor;

/**
 * ControlAccessor for Selector
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 * @since 1.1
 * @see org.jdal.ui.bind.ControlAccessor
 * @see org.jdal.swing.Selector
 */
public class SelectorAccessor extends AbstractControlAccessor implements ActionListener {

	public SelectorAccessor(Object selector) {
		super(selector);
		getControl().addActionListener(this);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void actionPerformed(ActionEvent e) {
		fireControlChange();
	}

	/**
	 * {@inheritDoc}
	 */
	public List<?> getControlValue() {
		List<?> controlValues = getControl().getSelected();
		ArrayList<?> values = new ArrayList<>(controlValues);
		
		return values;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public void setControlValue(Object value) {
		Selector selector = (Selector) getControl();
		selector.setSelected(convertIfNecessary(value, List.class));
	}
	
	public Selector<?> getControl() {
		return (Selector<?>) super.getControl();
	}
	
	public void setControl(Selector<?> selector) {
		super.setControl(selector);
		selector.addActionListener(this);
	}
}
