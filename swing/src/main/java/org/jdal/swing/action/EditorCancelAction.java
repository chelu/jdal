/*
 * Copyright 2009-2012 the original author or authors.
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
package org.jdal.swing.action;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import org.jdal.ui.Editor;

/**
 * Action for cancel button on editors
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class EditorCancelAction<T> extends ViewCancelAction<T> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (getView().isDirty()) {
			if (JOptionPane.YES_OPTION == 
					JOptionPane.showConfirmDialog(getView().getPanel(), 
							getMessageWrapper().getMessage("ViewCancelAction.dirty"))) {
			
				getEditor().cancel();
				getDialog().dispose();
			}
		}
		else {
			getEditor().cancel();
			getDialog().dispose();
		}
	}

	/**
	 * @return editor
	 */
	@SuppressWarnings("unchecked")
	private Editor<T> getEditor() {
		return (Editor<T>) getView();
	}
	
	
}
