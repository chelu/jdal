/*
 * Copyright 2008-2013 the original author or authors.
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

import javax.annotation.PostConstruct;

import org.jdal.beans.StaticMessageSource;
import org.jdal.vaadin.ui.Box;
import org.jdal.vaadin.ui.FormUtils;
import org.jdal.vaadin.ui.VaadinView;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Window;

/**
 * Window used to show Views.
 * 
 * @author Jose Luis Martin 
 */
public class ViewDialog extends Window {

	public static final int OK = 0;
	public static final int CANCEL= 1;

	private static final long serialVersionUID = 1L;
	private VaadinView<?> view;
	private ViewAction acceptAction = new ViewSaveAction();
	private ViewAction cancelAction = new CancelAction(); 
	private Button acceptButton;
	private Button cancelButton;
	private int windowWidth = 750;
	private int windowHeight = 750;
	private int value = CANCEL;
	private boolean showAcceptButton = true;
	private boolean showCancelButton = true;

	public ViewDialog() {

	}

	public ViewDialog(VaadinView<?> view) {
		setView(view);
	}

	@PostConstruct
	public void init() {
		acceptAction.setView(view);
		cancelAction.setView(view);
		
		acceptAction.setCaption(StaticMessageSource.getMessage("Accept"));
		cancelAction.setCaption(StaticMessageSource.getMessage("Cancel"));
		
		if (view != null) {
			HorizontalLayout wrapper = new HorizontalLayout();
			wrapper.addComponent(view.getPanel());

			BoxFormBuilder fb = new BoxFormBuilder();
			fb.row();
			fb.setElastic();
			fb.add(wrapper, SimpleBoxFormBuilder.SIZE_FULL);
			fb.row();
			fb.add(createButtonBox());

			this.setContent(fb.getForm());

			this.setHeight(view.getHeight(), Unit.PIXELS);
			this.setWidth(view.getWidth(), Unit.PIXELS);
			this.setCaption(view.getName());
		}
	}

	/**
	 * Create box with accept/cancel buttons
	 * @return new button box
	 */
	protected Component createButtonBox() {
		HorizontalLayout hl = new HorizontalLayout();
		hl.setSizeUndefined();
		Box.addHorizontalGlue(hl);
		
		if (showAcceptButton) {
			acceptButton = FormUtils.newButton(acceptAction);
			hl.addComponent(acceptButton);
			hl.addComponent(Box.createHorizontalStrut(5));
		}
		if (showCancelButton) {
			cancelButton = FormUtils.newButton(cancelAction);
			hl.addComponent(cancelButton);
		}
		Box.addHorizontalGlue(hl);

		return hl;
	}
	
	public static ViewDialog createAcceptDialog(VaadinView<?> view) {
		ViewDialog dlg = new ViewDialog(view);
		dlg.setAcceptAction(new CancelAction());
		dlg.setShowCancelButton(false);
		dlg.init();
		
		return dlg;
	}

	public VaadinView<?> getView() {
		return view;
	}

	public void setView(VaadinView<?> view) {
		this.view = view;
		this.acceptAction.setView(view);
		this.cancelAction.setView(view);
	}

	public ViewAction getAcceptAction() {
		return acceptAction;
	}

	public void setAcceptAction(ViewAction acceptAction) {
		this.acceptAction = acceptAction;
		this.acceptAction.setView(view);
	}

	public ViewAction getCancelAction() {
		return cancelAction;
	}

	public void setCancelAction(ViewAction cancelAction) {
		this.cancelAction = cancelAction;
		cancelAction.setView(view);
	}

	public boolean isAccepted() {
		return value == OK;
	}

	/**
	 * @return the value
	 */
	public int getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(int value) {
		this.value = value;
	}

	/**
	 * @return the windwoWidth
	 */
	public int getWindwoWidth() {
		return windowWidth;
	}

	/**
	 * @param windwoWidth the windwoWidth to set
	 */
	public void setWindwoWidth(int windwoWidth) {
		this.windowWidth = windwoWidth;
	}

	/**
	 * @return the windowHeight
	 */
	public int getWindowHeight() {
		return windowHeight;
	}

	/**
	 * @param windowHeight the windowHeight to set
	 */
	public void setWindowHeight(int windowHeight) {
		this.windowHeight = windowHeight;
	}

	/**
	 * @return the windowWidth
	 */
	public int getWindowWidth() {
		return windowWidth;
	}

	/**
	 * @param windowWidth the windowWidth to set
	 */
	public void setWindowWidth(int windowWidth) {
		this.windowWidth = windowWidth;
	}

	public Button getAcceptButton() {
		return acceptButton;
	}

	public void setAcceptButton(Button acceptButton) {
		this.acceptButton = acceptButton;
	}

	public boolean isShowAcceptButton() {
		return showAcceptButton;
	}

	public void setShowAcceptButton(boolean showAcceptButton) {
		this.showAcceptButton = showAcceptButton;
	}

	public boolean isShowCancelButton() {
		return showCancelButton;
	}

	public void setShowCancelButton(boolean showCancelButton) {
		this.showCancelButton = showCancelButton;
	}

}
