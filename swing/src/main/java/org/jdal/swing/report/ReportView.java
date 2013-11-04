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


import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.jdal.logic.PersistentManager;
import org.jdal.reporting.Report;
import org.jdal.reporting.ReportType;
import org.jdal.service.ReportService;
import org.jdal.swing.AbstractView;
import org.jdal.swing.form.BoxFormBuilder;
import org.jdal.swing.form.FormUtils;
import org.jdal.swing.list.ListComboBoxModel;

/**
 * @author Jose A. Corbacho
 *
 */
public class ReportView extends AbstractView<Report> {

	private JTextField name = new JTextField();
	private JComboBox comboType = FormUtils.newCombo(25);

	private ReportFileView fileView;
	private JTextArea description = new JTextArea();

	private PersistentManager<ReportType, Long> typeService;
	private ReportService reportService;
	
	/**
	 * Constructor 
	 * @param model the model for this view
	 */
	public ReportView(Report model){
		setModel(model);
	}
	
	/**
	 * 
	 */
	public ReportView(){
		this(new Report());
	}
	
	/**
	 * 
	 */
	public void init(){
		getPanel();
		refresh();

		bind(name, "name");
		bind(comboType, "type");
		bind(fileView.getFileName(), "fileName");
		bind(description, "description");
	}
	
	@Override
	protected void doRefresh() {
		if (getModel() == null)
			return;

		// File view 
		fileView.setModel(getModel());
		fileView.refresh();
		
		// Report Type
		comboType.setSelectedItem(getModel().getType());
	}
	
	@Override
	public void doUpdate() {
		if (getModel() == null)
			return;
		fileView.update();
	}
	
	@Override
	protected JComponent buildPanel() {
		Report model = getModel();
		// Build Form
		BoxFormBuilder mainBox = new BoxFormBuilder();
		Box box = Box.createHorizontalBox();
		box.add(new JLabel("Nombre: "));
		box.add(name);
		
		box.add(Box.createHorizontalStrut(5));
		
		// Report type
		comboType.setModel(new ListComboBoxModel(typeService.getAll()));
		comboType.setSelectedItem(getModel().getType());
		box.add(new JLabel("Tipo: "));
		box.add(comboType);
		mainBox.add(box);
		
		// Report file
		fileView.setModel(model);
		mainBox.add(fileView.getPanel());
		
		mainBox.row();
		mainBox.add(new JLabel("Descripción"));
		mainBox.row();
		description.setRows(5);
		
		mainBox.add(new JScrollPane(description));
		
		// Box attributes
		mainBox.getForm().setOpaque(true);
		
		JComponent mainPanel = mainBox.getForm();
		mainPanel.setBorder(FormUtils.createTitledBorder("Informe"));
		
		return mainPanel;
	}

	@Override
	public void onSetModel(Report model) {
		if (reportService != null)
			reportService.initialize(model, 2);
	}
	
	/**
	 * GETTERS AND SETTERS
	 */

	public ReportFileView getFileView() {
		return fileView;
	}

	public void setFileView(ReportFileView fileView) {
		this.fileView = fileView;
	}

	public PersistentManager<ReportType, Long> getTypeService() {
		return typeService;
	}

	public void setTypeService(PersistentManager<ReportType, Long> typeService) {
		this.typeService = typeService;
	}

	public ReportService getReportService() {
		return reportService;
	}

	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}
}