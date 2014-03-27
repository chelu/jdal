/*
 * Copyright 2009-2014 the original author or authors.
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
package org.jdal.vaadin.ui;

import org.jdal.vaadin.auth.AuthenticationEvent;
import org.jdal.vaadin.auth.AuthenticationListener;
import org.jdal.vaadin.auth.LoginView;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewProvider;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * Simple application UI.
 * 
 * @author Jose Luis Martin
 * @since 2.0
 */
public class SimpleApplicationUI extends UI implements AuthenticationListener {
	
	protected VerticalLayout root = new VerticalLayout();
	@Autowired(required=false)
	protected ButtonBar buttonBar;
	protected HorizontalLayout top = new HorizontalLayout();
	@Autowired(required=false)
	private LoginView loginView;
	private VerticalLayout loginLayout = new VerticalLayout();
	private Panel mainView = new Panel();
	@Autowired
	private ViewProvider viewProvider;

	@Override
	protected void init(VaadinRequest request) {
		addStyleNames();
		root.setSizeFull();
		loginView.addAuthenticationListener(this);
		Navigator navigator = new Navigator(this, mainView);
		navigator.addProvider(viewProvider);
		buildLogin();
		setContent(root);
		mainView.setStyleName(Reindeer.PANEL_LIGHT);
		mainView.setSizeFull();
		doInit(request);
	}
	
	protected void doInit(VaadinRequest request) {
		// do noting by default
	}
	
	/**
	 * build login window
	 */
	protected void buildLogin() {
		loginLayout.setSizeFull();
		loginLayout.setStyleName("jd-login-layout");
		loginLayout.addComponent(loginView.getPanel());
		loginLayout.setComponentAlignment(loginView.getPanel(), Alignment.MIDDLE_CENTER);
		root.addComponent(loginLayout);
	}
	
	/**
	 * Build application main window
	 */
	protected void buildMain() {
		root.removeAllComponents();
		top.setWidth(100, Unit.PERCENTAGE);
		top.setSpacing(false);
		top.setMargin(false);
		top.addComponent(buttonBar);
		root.addComponent(top);
		root.addComponent(mainView);
		root.setSpacing(false);
		root.setMargin(false);
		root.setExpandRatio(top, 0);
		root.setExpandRatio(mainView, 1);
	}
	
	protected void addStyleNames() {
		root.addStyleName("jd-app-root");
		top.addStyleName("jd-app-top");
	}

	@Override
	public void handleAuthentication(AuthenticationEvent event) {
		buildMain();
	}

	public ButtonBar getButtonBar() {
		return buttonBar;
	}

	public void setButtonBar(ButtonBar buttonBar) {
		this.buttonBar = buttonBar;
	}

	public LoginView getLoginView() {
		return loginView;
	}

	public void setLoginView(LoginView loginView) {
		this.loginView = loginView;
	}

	public Panel getMainView() {
		return mainView;
	}

	public void setMainView(Panel mainView) {
		this.mainView = mainView;
	}

	public ViewProvider getViewProvider() {
		return viewProvider;
	}

	public void setViewProvider(ViewProvider viewProvider) {
		this.viewProvider = viewProvider;
	}
}
