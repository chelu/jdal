/*
 * Copyright 2008-2011 the original author or authors.
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

import info.joseluismartin.gui.Selector;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Binder for Selector
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class SelectorBinder extends AbstractBinder implements ActionListener{

	/**
	 * {@inheritDoc}
	 * @see info.joseluismartin.gui.bind.AbstractBinder#doBind(java.awt.Component)
	 */
	protected void doBind(Object component) {
		Selector<?> selector = (Selector<?>) component;
		selector.addActionListener(this);
		
	}

	/**
	 * {@inheritDoc}
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
	
	}

	@SuppressWarnings("unchecked")
	public void refresh() {
		List values = new ArrayList((Collection) getValue());
		Selector selector = (Selector) component;
		selector.setSelected(values);
		
	}

	public void update() {
		Selector<?> selector = (Selector<?>) component;
		setValue(selector.getSelected());
		
	}

}
