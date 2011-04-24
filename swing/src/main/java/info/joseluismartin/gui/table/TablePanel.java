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
package info.joseluismartin.gui.table;

import info.joseluismartin.dao.Page;
import info.joseluismartin.dao.PageableDataSource;
import info.joseluismartin.gui.GuiFactory;
import info.joseluismartin.gui.PageableTable;
import info.joseluismartin.gui.View;
import info.joseluismartin.gui.form.FormUtils;
import info.joseluismartin.gui.report.ReportListView;
import info.joseluismartin.reporting.ReportDataProvider;
import info.joseluismartin.service.PersistentService;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.io.Serializable;
import java.util.List;

import javax.swing.Action;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

/**
 * A Panel with PageableTable, Filter and Button Box.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
@SuppressWarnings("unchecked")
public class TablePanel extends JPanel implements ReportDataProvider {

	private static final long serialVersionUID = 1L;
	
	
	private PersistentService<Object, Serializable> persistentService;
	private String editorName;
	private GuiFactory guiFactory;
	
	private String name;
	private PageableTable table;
	
	private View<Object> filterView;
	// TODO: reportListView must be View<Object>
	// Can be set to View<Object> if adding setReportProvider to View<Object>
	private ReportListView reportListView;
	
	private List<Action> actions;
	
	
	public TablePanel() {
	}
	
	
	public void init() {
		BorderLayout layout = new BorderLayout();
		layout.setVgap(10);
		layout.setHgap(10);
		setLayout(layout);

		// Header
		this.add(createFilterBox(), BorderLayout.NORTH);
		this.add(createTableBox(), BorderLayout.CENTER);
		
		if (filterView != null)
			table.setFilter(filterView.getModel());
		
		if (guiFactory != null)
			table.setGuiFactory(guiFactory);
		
		if (editorName != null)
			table.setEditorName(editorName);
		
		// Key Bindings
		getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F2"), "addAction");
		getActionMap().put("addAction", new AddAction());
	}
	
	
	private Component createReportListBox(){
		Box tableBox = Box.createHorizontalBox();
		tableBox.add(reportListView.getPanel());
		reportListView.setReportProvider(this);
		return tableBox;
	}
	
	/**
	 * @return
	 */
	private Component createTableBox() {
		Box tableBox = Box.createVerticalBox();
		tableBox.add(createControlBox());
		tableBox.add(Box.createVerticalStrut(5));
		table.setAlignmentX(Container.LEFT_ALIGNMENT);
		tableBox.add(table);
	
		return tableBox;
	}

	/**
	 * @return
	 */
	private Component createFilterBox() {
		Box header = Box.createVerticalBox();
		header.add(Box.createVerticalStrut(10));
		if (filterView != null) {
			filterView.refresh();
			header.add(filterView.getPanel());
		}
		
		header.setAlignmentX(Container.LEFT_ALIGNMENT);

		return header;
	}

	/**
	 * Create the control button Box from action list.
	 *  
	 * @return Box with buttons
	 */
	protected Box createControlBox() {
		Box controlBox = Box.createHorizontalBox();
		if (actions != null) {
			for (Action a : actions) {
				if (a instanceof TablePanelAction)
					((TablePanelAction) a).setTablePanel(this);

				JButton b = new JButton(a);
				controlBox.add(b);
				controlBox.add(Box.createHorizontalStrut(5));
			}
		}
		controlBox.add(Box.createHorizontalGlue());
		controlBox.setAlignmentX(Container.LEFT_ALIGNMENT);
		
		return controlBox;
	}
	
	public void refresh() {
		if (reportListView != null) 
			reportListView.refresh();
		if (filterView !=null)
			filterView.refresh();
		table.refresh();
	}
	
	
	public PageableTable getTable() {
		return table;
	}
	public void setTable(PageableTable table) {
		this.table = table;
	}


	public void selectAll() {
		PageableDataSource dataSource = table.getDataSource();
		Page page = new Page(Integer.MAX_VALUE);
		page.setFilter(filterView.getModel());
		table.getTableModel().check(dataSource.getKeys(page));
	}

	public View<Object> getFilterView() {
		return filterView;
	}

	public void setFilterView(View<Object> filterView) {
		this.filterView = filterView;
	}
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public PersistentService<Object, Serializable> getPersistentService() {
		return persistentService;
	}

	public void setPersistentService(PersistentService<Object, Serializable> ps) {
		this.persistentService = ps;
	}
	
	public JDialog getDialog() {
		JDialog dlg = guiFactory.getDialog(editorName);
		return dlg;
	}
	
	public JDialog getDialog(Object toEdit) {
		return (JDialog) guiFactory.getObject(editorName, new Object[] {toEdit});
	}

	public String getEditorName() {
		return editorName;
	}

	public void setEditorName(String editor) {
		this.editorName = editor;
	}
	
	
	
	public ReportListView getReportListView() {
		return reportListView;
	}

	public void setReportListView(ReportListView reportListView) {
		this.reportListView = reportListView;
	}

	public PersistentService<Object, Serializable> getDataSource() {
		return persistentService; 
	}

	public Object getFilter() {
		return table.getFilter();
	}

	public String getSortProperty() {
		return table.getSortPropertyName();
	}

	public Page.Order getSortOrder() {
		return table.getOrder();
	}


	/**
	 * @return the actions
	 */
	public List<Action> getActions() {
		return actions;
	}


	/**
	 * @param actions the actions to set
	 */
	public void setActions(List<Action> actions) {
		this.actions = actions;
	}


	/**
	 * @return the guiFactory
	 */
	public GuiFactory getGuiFactory() {
		return guiFactory;
	}


	/**
	 * @param guiFactory the guiFactory to set
	 */
	public void setGuiFactory(GuiFactory guiFactory) {
		this.guiFactory = guiFactory;
	}
}
