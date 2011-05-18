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

import info.joseluismartin.gui.Editor;
import info.joseluismartin.gui.View;
import info.joseluismartin.gui.ViewDialog;
import info.joseluismartin.service.PersistentService;

import java.awt.event.ActionEvent;
import java.io.Serializable;

/**
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 *
 */
public class ViewSaveAction extends ViewAction {

	private static final long serialVersionUID = 1L;
	
	PersistentService<Object, Serializable> service;
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		View<Object> view = getView();
		view.update();
		if (view.validateView()) {
			service.save(getView().getModel());
			getDialog().setVisible(false);
			getDialog().dispose();
			
			// FIXME: move to Editor
			if (getDialog() instanceof ViewDialog) {
				((ViewDialog) getDialog()).setValue(ViewDialog.OK);
			}
			
			if (getDialog() instanceof Editor) {
				((Editor) getDialog()).save();
			}
		}
	}

	public PersistentService<Object, Serializable> getService() {
		return service;
	}

	public void setService(PersistentService<Object, Serializable> service) {
		this.service = service;
	}

}
