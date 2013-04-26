/*
 * Copyright 2009-2012 Jose Luis Martin.
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

import org.jdal.swing.Editor;
import org.jdal.swing.form.FormUtils;

/**
 * Action for editors save button. Delegates to editor.save 
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class EditorSaveAction<T> extends ViewAction<T> {
	
	private static final String DEFAULT_ICON = 	"/images/16x16/dialog-ok.png";
	private static final String DEFAULT_NAME = "Accept";

	public EditorSaveAction() {
		setIcon(FormUtils.getIcon(DEFAULT_ICON));
		setName(DEFAULT_NAME);
	}

	/**
	 * {@inheritDoc}
	 */
	public void actionPerformed(ActionEvent e) {
		getEditor().save();
		getDialog().dispose();
	}

	/**
	 * @return the editor
	 */
	@SuppressWarnings("unchecked")
	protected Editor<T> getEditor() {
		return (Editor<T>) getView();
	}

}
