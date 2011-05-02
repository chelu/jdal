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
package info.joseluismartin.gui;

import info.joseluismartin.gui.action.DialogCancelAction;
import info.joseluismartin.gui.action.ViewAction;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * A JFrame for use as View Container
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class ViewFrame extends JFrame implements View<Object> {

	private static final long serialVersionUID = 1L;
	private View<Object> view;
	private ViewAction acceptAction;
	private DialogCancelAction cancelAction;
	private int windowWidth;
	private int windowHeight;


	public void init() {
		add(view.getPanel(), BorderLayout.CENTER);
		add(createButtonBox(), BorderLayout.SOUTH);
		setTitle(view.getModel().toString());
		setLocationRelativeTo(null);
		setSize(new Dimension(windowWidth, windowHeight));
		pack();
	}

	protected Component createButtonBox() {
		JButton acceptButton = new JButton(acceptAction);
		JButton cancelButton = new JButton(cancelAction);
		JPanel p = new JPanel();
		p.add(acceptButton);
		p.add(cancelButton);
		
		return p;
	}

	public View<Object> getView() {
		return view;
	}

	public void setView(View<Object> view) {
		this.view = view;
	}

	public ViewAction getAcceptAction() {
		return acceptAction;
	}

	public void setAcceptAction(ViewAction acceptAction) {
		this.acceptAction = acceptAction;
		this.acceptAction.setView(view);
		this.acceptAction.setDialog(this);
	}

	public DialogCancelAction getCancelAction() {
		return cancelAction;
	}

	public void setCancelAction(DialogCancelAction cancelAction) {
		this.cancelAction = cancelAction;
		cancelAction.setDialog(this);
	}

	public Object getModel() {
		return view.getModel();
	}

	public JComponent getPanel() {
		return view.getPanel();
	}

	public void refresh() {
		view.refresh();
		setTitle(view.getModel().toString());
	}

	public void setModel(Object model) {
		view.setModel(model);
	}

	public void update() {
		view.update();
	}

	
	public void clear() {
		view.clear();
	}

	public boolean validateView() {
		return view.validateView();
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
}
