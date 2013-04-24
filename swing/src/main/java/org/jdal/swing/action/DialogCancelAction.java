/*
 * Copyright 2002-2010 the original author or authors.
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


import java.awt.Window;
import java.awt.event.ActionEvent;

import org.jdal.swing.form.FormUtils;

/**
 * Cancel Action for ViewDialog
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class DialogCancelAction extends DialogAction {
	
	private static final String ICON = "/images/16x16/dialog-cancel.png";

	public DialogCancelAction() {
		this.setName("Cancel");
	}
	

	public void init() {
		if (getIcon() == null)
			setIcon(FormUtils.getIcon(ICON));
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void actionPerformed(ActionEvent e) {
		Window dialog = getDialog();
		dialog.setVisible(false);
		dialog.dispose();
	}
}
