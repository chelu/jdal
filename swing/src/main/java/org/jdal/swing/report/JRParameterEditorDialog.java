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
package org.jdal.swing.report;


import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import org.jdal.swing.SpringUtilities;

import net.sf.jasperreports.engine.JRParameter;


/**
 * @author Jose A. Corbacho
 * 
 */
public class JRParameterEditorDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5861091649245443018L;
	private Map<String, JRParameter> parameters;
	private Map<String, ReportParameterEditor> editors = new HashMap<String, ReportParameterEditor>();
	private Map<String, Object> returnValues;
	private boolean canceled;
	private EditorFactory editorFactory;

	public JRParameterEditorDialog() {
		this(null, true);
	}
	
	public JRParameterEditorDialog(Frame parent, boolean modal,
			Map<String, JRParameter> jrParameters) {
		super(parent, modal);
		this.parameters = jrParameters;
		initialize();
	}

	public JRParameterEditorDialog(Frame parent, boolean modal) {
		super(parent, modal);
	}

	public void initialize() {
		this.setTitle("Parámetros del informe");
		
		// Main Panel containing parameter panel and button panel.
		JPanel borderPanel = new JPanel();
		borderPanel.setBorder(BorderFactory.createTitledBorder(""));
		borderPanel.setMinimumSize(borderPanel.getPreferredSize());
		borderPanel.setLayout(new BorderLayout());

		// Button Panel
		JPanel buttonPanel = new JPanel();
		JButton acceptButton = new JButton(new AcceptAction());
		acceptButton.setText("Aceptar");
		JButton cancelButton = new JButton(new CancelAction());
		cancelButton.setText("Cancelar");
		acceptButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		cancelButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		buttonPanel.add(acceptButton);
		buttonPanel.add(cancelButton);

		// Parameter panel
		JPanel paramPanel = new JPanel(new SpringLayout());

		for (JRParameter param : parameters.values()) {
	
			ReportParameterEditor propertyEditor = editorFactory
					.getParameterEditor(param);
			
			propertyEditor.getEditor().addKeyListener(new KeyPressed());
			
			JLabel label = new JLabel(param.getName());
			label.setLabelFor(propertyEditor.getEditor());
			paramPanel.add(label);
			paramPanel.add(propertyEditor.getEditor());

			editors.put(param.getName(), propertyEditor);
		}

		borderPanel.add(paramPanel, BorderLayout.PAGE_START);
		borderPanel.add(buttonPanel, BorderLayout.PAGE_END);

		SpringUtilities.makeCompactGrid(paramPanel, parameters.size(), 2, // rows,
																			// cols
				6, 6, // initX, initY
				6, 6); // xPad, yPad

		add(borderPanel);
		setLocationRelativeTo(null);
		pack();

	}

	public Map<String, Object> getReturnValues() {
		return returnValues;
	}

	public void setCanceled(boolean canceled) {
		this.canceled = canceled;
	}

	public boolean isCanceled() {
		return canceled;
	}

	private void updateReturnValues() {
		if (returnValues == null)
			returnValues = new HashMap<String, Object>();
		for (String key : editors.keySet()) {
			try {
				ReportParameterEditor editor = editors.get(key);
				returnValues.put(key, editor.getValue());
			} catch (Exception e) {
				String msg = "El valor introducido no es correcto para el parámetro "
						+ parameters.get(key).getName();
				JOptionPane.showMessageDialog(this, msg, "Error en parámetro",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	@SuppressWarnings("serial")
	public class AcceptAction extends AbstractAction {

		/**
		 * Updates the values to be returned
		 */
		public void actionPerformed(ActionEvent arg0) {
			updateReturnValues();
			setVisible(false);
		}
	}

	@SuppressWarnings("serial")
	public class CancelAction extends AbstractAction {

		/**
		 * Updates the values to be returned
		 */
		public void actionPerformed(ActionEvent arg0) {
			setCanceled(true);
			setVisible(false);
			dispose();
		}
	}

	class KeyPressed implements KeyListener {
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER){
				new AcceptAction().actionPerformed(null);
			}
			else if (e.getKeyCode() == KeyEvent.VK_ESCAPE){
				new CancelAction().actionPerformed(null);
			}
		}

		public void keyReleased(KeyEvent e) {
		}

		public void keyTyped(KeyEvent e) {
		}
	}

	public static void main(String[] args) {
		JRParameterEditorDialog d = new JRParameterEditorDialog(null, true);
		d.setVisible(true);
	}

	/**
	 * @return the editorFactory
	 */
	public EditorFactory getEditorFactory() {
		return editorFactory;
	}

	/**
	 * @param editorFactory the editorFactory to set
	 */
	public void setEditorFactory(EditorFactory editorFactory) {
		this.editorFactory = editorFactory;
	}

	/**
	 * @return the parameters
	 */
	public Map<String, JRParameter> getParameters() {
		return parameters;
	}

	/**
	 * @param parameters the parameters to set
	 */
	public void setParameters(Map<String, JRParameter> parameters) {
		this.parameters = parameters;
	}
}
