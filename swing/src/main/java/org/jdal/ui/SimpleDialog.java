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
package org.jdal.ui;


import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;

import org.jdal.ui.form.FormUtils;

/**
 * Simple Dialog 
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class SimpleDialog extends JDialog implements ActionListener {
	
	private boolean accepted = false;
	private JButton acceptButton;
	private JButton cancelButton;

	/**
	 * 
	 */
	public SimpleDialog() {
		this (null, null, null);
	}
	
	public SimpleDialog(Window owner, Component component) {
		this(owner, component, null);
	}
	
	public SimpleDialog(Component component) {
		this(null, component, null);
	}
	
	public SimpleDialog(Component component, String title) {
		this(null, component, title);
		setSize(800, 600);
	}
	
	public SimpleDialog(Window owner, Component component, String title) {
		super(owner, title);
		setLayout(new BorderLayout());
		add(createButtonPanel(), BorderLayout.SOUTH);
		if (component != null)
			add(component, BorderLayout.CENTER);
	}
	
	protected JComponent createButtonPanel() {
		acceptButton = FormUtils.newOKButton();
		acceptButton.addActionListener(this);
		cancelButton = FormUtils.newCancelButton();
		cancelButton.addActionListener(this);
		JPanel panel = new JPanel();
		panel.add(acceptButton);
		panel.add(cancelButton);
		
		return panel;
	}

	/**
	 * {@inheritDoc}
	 */
	public void actionPerformed(ActionEvent e) {
		if (acceptButton.equals(e.getSource()))
			accepted = true;
		
		dispose();
	}

	public boolean isAccepted() {
		return accepted;
	}
	
}
