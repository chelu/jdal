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
package org.jdal.vaadin.ui.form;

import org.jdal.service.PersistentService;
import org.jdal.ui.Editor;
import org.jdal.ui.View;
import org.jdal.vaadin.VaadinUtils;
import org.jdal.vaadin.ui.VaadinView;

import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification;
/**
 * Save Action for Views
 * 
 * @author Jose Luis Martin 
 * @since 2.0
 */
@SuppressWarnings("rawtypes")
public class ViewSaveAction extends ViewAction {
	
	private static final String DEFAULT_ICON="images/ok.png";
	
	private boolean showError = true;
	
	private PersistentService persistentService;
	
	public ViewSaveAction() {
		this(null, null);
	}
	
	public ViewSaveAction(VaadinView view) {
		this(view, null);
	}

	/**
	 * @param persistentService
	 * @param view
	 */
	public ViewSaveAction(VaadinView view, PersistentService persistentService) {
		this.persistentService = persistentService;
		setView(view);
		setIcon(new ThemeResource(DEFAULT_ICON));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		beforeSave();
		boolean valid = save();
		afterSave(valid);
		
		if (valid)
			VaadinUtils.closeWindow(getView().getPanel());
	}

	/** 
	 * Save the view model, show a message to user if there are
	 * validation errors.
	 */
	@SuppressWarnings("unchecked")
	private boolean save() {
		View<?> view = getView();
		view.update();
		boolean valid = view.validateView();
		
		if (valid) {
			if (view instanceof Editor) {
				((Editor) view).save();
			}
			else {
				// save using persistent service by default
				persistentService.save(view.getModel());
			}
		}
		else {
			if (onError()) {
				String errorMessage = view.getErrorMessage();
				Notification.show(errorMessage, Notification.Type.ERROR_MESSAGE);
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
	
	public PersistentService getPersistentService() {
		return persistentService;
	}

	public void setPersistentService(PersistentService persistentService) {
		this.persistentService = persistentService;
	}

	public boolean isShowError() {
		return showError;
	}

	public void setShowError(boolean showError) {
		this.showError = showError;
	}

}
