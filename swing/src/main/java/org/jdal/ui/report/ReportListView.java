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
package org.jdal.ui.report;


import java.awt.event.ActionEvent;
import java.io.Serializable;
import java.util.EventObject;
import java.util.List;

import javax.sql.DataSource;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;

import org.jdal.reporting.Report;
import org.jdal.reporting.ReportDataProvider;
import org.jdal.reporting.ReportEventListener;
import org.jdal.reporting.ReportType;
import org.jdal.reporting.ReportingException;
import org.jdal.service.ReportService;
import org.jdal.ui.AbstractView;
import org.jdal.ui.form.FormUtils;
import org.jdal.ui.list.ListComboBoxModel;

/**
 * @author Jose A. Corbacho
 * 
 */
public class ReportListView extends AbstractView<Report> implements ReportEventListener{

	private JComboBox reportCombo = FormUtils.newCombo(20);
	private ReportService reportService;
	private Icon pdfIcon;
	private Icon excelIcon;
	private ReportType reportType;
	private ReportManager reportManager;

	private ReportDataProvider<Object, Serializable> reportProvider;

	// The dataSource to use in case the report uses java.sql.Connection
	private DataSource dataSource;

	public ReportListView(Report model) {
		this.setModel(model);
	}

	public ReportListView() {
		this(new Report());
	}

	/**
	 * 
	 */
	public void init() {
		getPanel();
		refresh();
	}

	@Override
	protected void doRefresh() {
		if (getModel() == null)
			return;
		List<Report> reportList = reportService.getReportsByType(reportType);
		// Keep current selection. When SwingWorker refreshes, it deselects current user's selection
		// This may lead to printing the wrong report
		Object selected = reportCombo.getSelectedItem() != null ? reportCombo.getSelectedItem() : getModel();
		reportCombo.removeAllItems();
		reportCombo.setModel(new ListComboBoxModel(reportList));
		reportCombo.setSelectedItem(selected);
	}

	@Override
	protected JComponent buildPanel() {
		Box box = Box.createHorizontalBox();
		List<Report> reportList = reportService.getReportsByType(reportType);
		
		reportCombo.setModel(new ListComboBoxModel(reportList));
		reportCombo.setSelectedItem(getModel());

		box.add(reportCombo);
		JButton viewPdfButton = new JButton(new ViewFileAction(pdfIcon, "pdf"));
		JButton viewExcelButton = new JButton(new ViewFileAction(excelIcon,
				"xls"));

		box.add(viewPdfButton);
		box.add(viewExcelButton);
		return box;
	}

	private void showFile(Report model, String reportOutputType) {
		// recordProvider has the list of ids of objects to be processed by the
		// report as well as the datasource used in it.
		// Delegates in ReportManager to build the JRDataSource and generate the
		// report

		if (model.getHasQuery()) {
			try {
				reportManager.showReport(model,
						dataSource, reportOutputType);
			} catch (ReportingException e) {
				JOptionPane.showMessageDialog(getPanel(), e.getMessage(),
						"Error mostrando informe", JOptionPane.ERROR_MESSAGE);
			}
		} else if (reportProvider != null) {
			try {
				reportManager.showReport(model,
						reportProvider.getDataSource(),
						reportProvider.getFilter(),
						reportProvider.getSortProperty(),
						reportProvider.getSortOrder(), reportOutputType);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("serial")
	private class ViewFileAction extends AbstractAction {

		private String reportOutputType;

		public ViewFileAction(Icon icon, String reportOutputType) {
			this.reportOutputType = reportOutputType;
			putValue(Action.SMALL_ICON, icon);
		}

		public void actionPerformed(ActionEvent e) {
			showFile((Report) reportCombo.getSelectedItem(), reportOutputType);
		}

	}

	/******************************
	 * GETTERS AND SETTERS
	 ******************************/

	/**
	 * 
	 * @return the persistence service
	 */
	public ReportService getReportService() {
		return reportService;
	}

	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}

	public Icon getPdfIcon() {
		return pdfIcon;
	}

	public void setPdfIcon(Icon pdfIcon) {
		this.pdfIcon = pdfIcon;
	}

	public Icon getExcelIcon() {
		return excelIcon;
	}

	public void setExcelIcon(Icon excelIcon) {
		this.excelIcon = excelIcon;
	}

	public ReportType getReportType() {
		return reportType;
	}

	public void setReportType(ReportType type) {
		this.reportType = type;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" }) 
	public void setReportProvider(ReportDataProvider reportProvider) {
		this.reportProvider = reportProvider;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void reportChanged(EventObject event) {
		doRefresh();
	}

	/**
	 * @return the reportManager
	 */
	public ReportManager getReportManager() {
		return reportManager;
	}

	/**
	 * @param reportManager the reportManager to set
	 */
	public void setReportManager(ReportManager reportManager) {
		this.reportManager = reportManager;
	}

}
