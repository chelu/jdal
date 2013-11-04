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

import org.jdal.beans.StaticMessageSource;
import org.jdal.swing.form.FormUtils;

/**
 * Cancel action for view dialogs, ask user before close if the view 
 * is in dirty state.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class ViewCancelAction<T> extends ViewAction<T> {

	private static final String ICON = "/images/16x16/dialog-cancel.png";
	
	public ViewCancelAction() {
		this.setName("Cancelar");
	}
	
	public void init() {
		if (getIcon() == null)
			setIcon(FormUtils.getIcon(ICON));
		
		setName(StaticMessageSource.getMessage(getName()));
	}

	/**
	 * {@inheritDoc}
	 */
	public void actionPerformed(ActionEvent e) {
		if (getView().isDirty()) {
			if (JOptionPane.YES_OPTION == 
					JOptionPane.showConfirmDialog(getView().getPanel(), 
							getMessageWrapper().getMessage("ViewCancelAction.dirty"))) {
			
				getDialog().dispose();
			}
		}
		else {
			getDialog().dispose();
		}
	}
}
