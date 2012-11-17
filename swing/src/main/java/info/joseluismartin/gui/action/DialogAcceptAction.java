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
import info.joseluismartin.gui.form.FormUtils;

import java.awt.event.ActionEvent;

/**
 * Accept Action for View Dialog
 * 
 * @author Jose Luis Martin
 *
 */
public class DialogAcceptAction<T> extends ViewAction<T> {

	private static final String ICON = "/images/16x16/dialog-ok.png";
	
	public DialogAcceptAction() {
		this.setName("Accept");
	}

	public void init() {
		if (getIcon() == null)
			setIcon(FormUtils.getIcon(ICON));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public void actionPerformed(ActionEvent e) {
		View<T> view = getView();
		view.update();
		
		if (view.validateView()) {
		
			if (getDialog() instanceof ViewDialog) {
				((ViewDialog<T>) getDialog()).setValue(ViewDialog.OK);
			}
		
			getDialog().dispose();
		}
		else {
			FormUtils.showError(view.getErrorMessage());
		}
	}

}
