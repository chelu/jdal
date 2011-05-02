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
package info.joseluismartin.gui.report;

import info.joseluismartin.dao.filter.ReportFilter;
import info.joseluismartin.gui.AbstractView;
import info.joseluismartin.gui.form.BoxFormBuilder;
import info.joseluismartin.gui.form.FormUtils;
import info.joseluismartin.logic.PersistentManager;
import info.joseluismartin.reporting.ReportType;

import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTextField;

import org.springframework.richclient.list.ComboBoxListModel;




/**
 * @author Jose A. Corbacho
 *
 */
public class ReportFilterView extends AbstractView<ReportFilter> {

	private final static int COMBO_WIDTH = 20;
	
	private JTextField reportName = new JTextField();
	private JComboBox reportType = FormUtils.newCombo(COMBO_WIDTH);
	
	private PersistentManager<ReportType, Long> reportTypeService;
	
	public ReportFilterView() {
		this(new ReportFilter());
	}
	
	public ReportFilterView(ReportFilter reportFilter) {
		setModel(reportFilter);
	}

	public void init(){
		bind(reportName, "name");
		bind(reportType, "type");
		refresh();
	}

	@Override
	public void doRefresh() {
		List<ReportType> reportTypeList = reportTypeService.getAll();
		reportTypeList.add(0, null);
		reportType.setModel(new ComboBoxListModel(reportTypeList));
	}

	
	@Override
	protected JComponent buildPanel() {
		// Build Form
		BoxFormBuilder b = new BoxFormBuilder();
		
		b.add("Nombre: ", reportName);
		b.add("Tipo: ", reportType);

		JComponent c = b.getForm();
		
		return c;
	}


	/**
	 * GETTERS AND SETTERS
	 */

	public PersistentManager<ReportType, Long> getReportTypeService() {
		return reportTypeService;
	}

	public void setReportTypeService(PersistentManager<ReportType, Long> reportService) {
		this.reportTypeService = reportService;
	}
}
