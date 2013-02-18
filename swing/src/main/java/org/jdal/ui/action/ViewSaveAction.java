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
package org.jdal.ui.action;


import java.awt.event.ActionEvent;
import java.io.Serializable;

import javax.swing.JOptionPane;

import org.jdal.service.PersistentService;
import org.jdal.service.PersistentServiceAware;
import org.jdal.ui.Editor;
import org.jdal.ui.View;
import org.jdal.ui.ViewDialog;
import org.jdal.ui.form.FormUtils;

/**
 * Generic Save Action for ViewDialog.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class ViewSaveAction<T> extends ViewAction<T> implements PersistentServiceAware<T> {

	private static final String DEFAULT_ICON = 	"/images/16x16/dialog-ok.png";
	private static final String DEFAULT_NAME = "Accept";
	private PersistentService<T, ?extends Serializable> service;

	public ViewSaveAction() {
		setIcon(FormUtils.getIcon(DEFAULT_ICON));
		setName(DEFAULT_NAME);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void actionPerformed(ActionEvent e) {
		beforeSave();
		afterSave(save());
	}

	/** 
	 * Save the view model, show a message to user if there are
	 * validation errors.
	 */
	@SuppressWarnings("unchecked")
	private boolean save() {
		View<T> view = getView();
		view.update();
		boolean valid = view.validateView();
		
		if (valid) {
			if (service != null)
				service.save(view.getModel());
			
			getDialog().setVisible(false);
			getDialog().dispose();
			
			// FIXME: move to Editor
			if (getDialog() instanceof ViewDialog) {
				((ViewDialog<?>) getDialog()).setValue(ViewDialog.OK);
			}
			
			if (getDialog() instanceof Editor) {
				((Editor<T>) getDialog()).save();
			}
		}
		else {
			if (onError()) {
				String errorMessage = view.getErrorMessage();
				JOptionPane.showMessageDialog(view.getPanel(),errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
		
		return valid;
	}

	/**
	 * Hook method to let subclases to do something on validation errors.
	 * @return true to show message error, false otherwise
	 */
	protected boolean onError() {
		return true;
	}

	/**
	 * Hook method to let subclases to do something before save 
	 * the model 
	 * @param valid true if validation success
	 */
	protected void afterSave(boolean valid) {
		
	}

	/**
	 * Hook method to let subclases to do something after save 
	 * the model
	 */
	protected void beforeSave() {
		
	}

	@Deprecated
	public PersistentService<T, ?extends Serializable> getService() {
		return service;
	}

	@Deprecated
	public void setService(PersistentService<T, ?extends Serializable> service) {
		this.service = (PersistentService<T, ? extends Serializable>) service;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setPersistentService(PersistentService<T, ? extends Serializable> persistentService) {
		this.service = persistentService;
	}
}
