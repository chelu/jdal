/*
 * Copyright 2009-2013 the original author or authors.
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
package org.jdal.vaadin.auth;

import java.util.LinkedList;

import org.jdal.annotation.SerializableProxy;
import org.jdal.auth.AuthService;
import org.jdal.vaadin.ui.AbstractView;
import org.jdal.vaadin.ui.FormUtils;
import org.jdal.vaadin.ui.form.BoxFormBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.Resource;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.Reindeer;

/**
 * Default login window.
 * 
 * @author Jose Luis Martin
 * @since 2.0
 */
public class LoginView extends AbstractView<Credentials> implements ClickListener {
	
	private TextField username = FormUtils.newTextField();
	private PasswordField password = FormUtils.newPasswordField();
	private Button loginButton = new Button();
	private String applicationName = "";
	private Resource applicationIcon;
	@SerializableProxy
	@Autowired
	private AuthService authService;
	private Label errorLabel = new Label();
	
	private LinkedList<AuthenticationListener> authenticationListeners = new LinkedList<AuthenticationListener>();
	
	public LoginView() {
		super(new Credentials());
		setHeight(200);
		setWidth(500);
		errorLabel.setImmediate(true);
		errorLabel.setSizeUndefined();
		errorLabel.setStyleName("jd-login-error");
		autobind();
	}

	public LoginView(Credentials model) {
		super(model);
	}

	@Override
	protected Component buildPanel() {
		Label greeting = new Label(getMessage("loginView.greeting"));
		greeting.addStyleName("jd-login-greeting");
		greeting.addStyleName(Reindeer.LABEL_H2);
		Label applicationNameLabel = new Label(getMessage(applicationName));
		applicationNameLabel.addStyleName("jd-login-appname");
		applicationNameLabel.addStyleName(Reindeer.LABEL_H2);
		applicationNameLabel.setSizeUndefined();
		
		loginButton.addClickListener(this);
		loginButton.setCaption(getMessage("loginView.loginButtonCaption"));
		loginButton.addStyleName("jd-login-button");
		
		// add shortcut listener for enter key
		loginButton.addShortcutListener(new ShortcutListener("Sign In",
				KeyCode.ENTER, null) {
			@Override
			public void handleAction(Object sender, Object target) {
				loginButton.click();
			}
		});
		
		Image image = null;
		HorizontalLayout imageWrapper = null;
		
		if (applicationIcon != null) {
			image = new Image(null, applicationIcon);
			image.setSizeUndefined();
			image.setStyleName("jd-login-icon");
			imageWrapper = new HorizontalLayout();
			imageWrapper.setMargin(false);
			imageWrapper.addComponent(image);
			imageWrapper.setComponentAlignment(image, Alignment.MIDDLE_CENTER);
		}
		
		BoxFormBuilder fb = new BoxFormBuilder();
		fb.setDefaultWidth(BoxFormBuilder.SIZE_FULL);
		fb.row();
		fb.startBox();
		fb.setFixedHeight();
		fb.row(false);
		fb.add(greeting, Alignment.TOP_LEFT);
		fb.add(applicationNameLabel, Alignment.TOP_RIGHT);
		fb.endBox();
		// add application icon
		if (image != null) {
			fb.row(BoxFormBuilder.SIZE_FULL);
			fb.add(imageWrapper, BoxFormBuilder.SIZE_FULL, Alignment.MIDDLE_CENTER);
		}
		fb.row(true);
		fb.startBox();
		fb.row(50);
		fb.add(errorLabel, BoxFormBuilder.SIZE_FULL, Alignment.BOTTOM_CENTER);
		fb.endBox();
		fb.row();
		fb.startBox();
		fb.setFixedHeight();
		fb.row();
		fb.add(username, getMessage("loginView.username"), Alignment.BOTTOM_CENTER);
		fb.add(password, getMessage("loginView.password"), Alignment.BOTTOM_CENTER);
		fb.add(loginButton, 100, Alignment.BOTTOM_CENTER);
		fb.endBox();
		
		Component form =  fb.getForm();
		form.setWidth(this.getWidth(), Unit.PIXELS);
		form.setHeight(getHeight(), Unit.PIXELS);
		form.setStyleName("jd-login");
		
		return form;
	}
	
	@Override
	public void buttonClick(ClickEvent event) {
		login();
	}
	
	protected void login() {
		update();
		errorLabel.setValue("");
		Credentials model = getModel();
		
		if (authService.validate(model.getUsername(), model.getPassword())) {
			for (AuthenticationListener al : authenticationListeners)
				al.handleAuthentication(new AuthenticationEvent(getPanel(), getModel()));
			
			password.setValue("");
			username.setValue("");
		}
		else {
			showErrorMessage();
		}
	}

	protected void showErrorMessage() {
		errorLabel.setValue(getMessage("loginFailure"));
	}

	/**
	 * @param simpleApplicationUI
	 */
	public void addAuthenticationListener(AuthenticationListener listener) {
		if (!authenticationListeners.contains(listener))
			authenticationListeners.add(listener);
	}
	
	public void removeAuthenticationListener(AuthenticationListener listener) {
		authenticationListeners.remove(listener);
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public Resource getApplicationIcon() {
		return applicationIcon;
	}

	public void setApplicationIcon(Resource applicationIcon) {
		this.applicationIcon = applicationIcon;
	}

}
