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
package info.joseluismartin.gui.action;

import info.joseluismartin.gui.View;
import info.joseluismartin.gui.ViewDialog;

import java.awt.event.ActionEvent;

/**
 * Accept Action for View Dialog
 * 
 * @author Jose Luis Martin
 *
 */
public class DialogAcceptAction extends ViewAction {

	/**
	 * 
	 */
	public DialogAcceptAction() {
		this.setName("Accept");
	}

	private static final long serialVersionUID = 1L;

	/**
	 * {@inheritDoc}
	 */
	public void actionPerformed(ActionEvent e) {
		View<?> view = getView();
		view.update();
		
		if (getDialog() instanceof ViewDialog) {
			((ViewDialog<?>) getDialog()).setValue(ViewDialog.OK);
		}
		
		getDialog().dispose();
	}

}
