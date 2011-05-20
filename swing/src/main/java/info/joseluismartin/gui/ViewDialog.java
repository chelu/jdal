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
package info.joseluismartin.gui;

import info.joseluismartin.gui.action.DialogCancelAction;
import info.joseluismartin.gui.action.ViewAction;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;

/**
 * A JDialog for use as View Wrapper
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class ViewDialog extends JDialog implements View<Object>  {
	
	public static final int OK = 0;
	public static final int CANCEL= 1;
	
	private static final long serialVersionUID = 1L;
	private View<Object> view;
	private ViewAction acceptAction;
	private DialogCancelAction cancelAction;
	private int dialogWidth = 750;
	private int dialogHeight = 750;
	private int value = CANCEL;
	
	public void init() {
		add(view.getPanel(), BorderLayout.CENTER);
		if (view.getModel() != null)
			setTitle(view.getModel().toString());
		add(createButtonBox(), BorderLayout.SOUTH);
		setSize(dialogWidth, dialogHeight);
		pack();
		setLocationRelativeTo(null);
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
	 * @return the dialogWidth
	 */
	public int getDialogWidth() {
		return dialogWidth;
	}

	/**
	 * @param dialogWidth the dialogWidth to set
	 */
	public void setDialogWidth(int dialogWidth) {
		this.dialogWidth = dialogWidth;
	}

	/**
	 * @return the dialogHeight
	 */
	public int getDialogHeight() {
		return dialogHeight;
	}

	/**
	 * @param dialogHeight the dialogHeight to set
	 */
	public void setDialogHeight(int dialogHeight) {
		this.dialogHeight = dialogHeight;
	}

	public void clear() {
		view.clear();
		
	}

	public Object getModel() {
		return view.getModel();
	}

	public JComponent getPanel() {
		return view.getPanel();
	}

	public void refresh() {
		if (view.getModel() != null)
			setTitle(view.getModel().toString());
		
		view.refresh();
	}

	public void setModel(Object model) {
		view.setModel(model);
	}

	public void update() {
		view.update();
	}

	public boolean validateView() {
		return view.validateView();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isDirty() {
		// TODO Auto-generated method stub
		return false;
	}
}
