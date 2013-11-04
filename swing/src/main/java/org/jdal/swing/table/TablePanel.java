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
package org.jdal.swing.table;


import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Window;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Action;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdal.dao.Page;
import org.jdal.reporting.ReportDataProvider;
import org.jdal.service.PersistentService;
import org.jdal.swing.GuiFactory;
import org.jdal.swing.PageableTable;
import org.jdal.swing.report.ReportListView;
import org.jdal.swing.View;
import org.jdal.ui.EditorListener;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.BeanCreationException;

/**
 * A Panel with PageableTable, Filter and Button Box to hold TablePanelActions.
 * Hold a Paginator to navigate across pages.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class TablePanel<T> extends JPanel implements ReportDataProvider {

	private static final long serialVersionUID = 1L;
	private static final Log log = LogFactory.getLog(TablePanel.class);
	/** GuiFactory to get model editor */
	private GuiFactory guiFactory;
	/** TablePanel name */
	private String name;
	/** Pageable table used to show records */
	private PageableTable<T> table;
	/** A filter view */
	private View<Object> filterView;
	/** ComboBox with reports available to execute on filtered data */
	private ReportListView reportListView;
	/** the TablePanelAction list */
	private List<Action> actions = new ArrayList<Action>();
	/** property values to configure new created editors */
	private PropertyValues propertyValues; 
	/** Action component holder */
	private Box controlBox;
	
	/** 
	 * Creates new TablePanel
	 */
	public TablePanel() {
		BorderLayout layout = new BorderLayout();
		layout.setVgap(10);
		layout.setHgap(10);
		setLayout(layout);
	}
	
	/**
	 * Initialize TablePanel after property set. Usally called by container.
	 */
	public void init() {
		// Header
		this.add(createFilterBox(), BorderLayout.NORTH);
		this.add(createTableBox(), BorderLayout.CENTER);
		
		if (filterView != null)
			table.setFilter(filterView.getModel());
		
		if (guiFactory != null)
			table.setGuiFactory(guiFactory);
		
		// Key Bindings
		getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F2"), "addAction");
		getActionMap().put("addAction", new AddAction());
	}
	
	
	/**
	 * Creates a new Component that holds the ReportListView
	 * @return ReportListView Component
	 */
	@SuppressWarnings("unused")
	private Component createReportListBox(){
		Box tableBox = Box.createHorizontalBox();
		tableBox.add(reportListView.getPanel());
		reportListView.setReportProvider(this);
		return tableBox;
	}
	
	/**
	 * Creates a new Component with PageableTable.
	 * @return PageableTableComponent.
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
	 * Creates a new Component with FilterView.
	 * @return new Component.
	 */
	private Component createFilterBox() {
		Box header = Box.createVerticalBox();
		
		if (filterView != null) {
			header.add(Box.createVerticalStrut(10));
			filterView.refresh();
			header.add(filterView.getPanel());
		}
		
		header.setAlignmentX(Container.LEFT_ALIGNMENT);

		return header;
	}

	/**
	 * Create the control button Box from action list.
	 * @return Box with buttons from actions
	 */
	protected Box createControlBox() {
		controlBox = Box.createHorizontalBox();
		populateControlBox();
		
		return controlBox;
	}

	public void populateControlBox() {
		if (actions != null) {
			for (Action a : actions) {
				if (a instanceof TablePanelAction)
					((TablePanelAction) a).setTablePanel((TablePanel<Object>) this);

				JButton b = new JButton(a);
				controlBox.add(b);
				controlBox.add(Box.createHorizontalStrut(5));
			}
		}
		controlBox.add(Box.createHorizontalGlue());
		controlBox.setAlignmentX(Container.LEFT_ALIGNMENT);
	}
	
	/**
	 * Refresh View
	 */
	public void refresh() {
		if (reportListView != null) 
			reportListView.refresh();
		if (filterView != null)
			filterView.refresh();
		table.refresh();
	}
	
	/**
	 * Selects records in all pages. Query only forkeys and check 
	 * them in PageableTable.
	 */
	public void selectAll() {
		table.selectAll();
	}
	
	public void unSelectAll() {
		table.unSelectAll();
	}
	
	// Getters and Setters

	/**
	 * @return the PagebleTable.
	 */
	public PageableTable<T> getTable() {
		return table;
	}
	
	/**
	 * @param table the PageableTable to set.
	 */
	public void setTable(PageableTable table) {
		this.table = table;
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

	public PersistentService<T, Serializable> getPersistentService() {
		return getDataSource();
	}

	public void setPersistentService(PersistentService<T, ?extends Serializable> ps) {
		table.setDataSource(ps);
	}
	
	public Window getDialog() {
		try {
			Window dlg = table.getEditor();
		
			if (dlg instanceof View && propertyValues != null) {
				View view = (View) dlg;
				new BeanWrapperImpl(view.getModel()).setPropertyValues(propertyValues);
				view.refresh();
			}
			
			return dlg;
		}
		catch (BeanCreationException bce) {
			if (log.isWarnEnabled())
				log.warn("Can't get editor [" + table.getEditorName() + "]");
			
			log.error(bce);
		}
		return null;
	}
	
	public Window getDialog(Object toEdit) {
		try {
			return table.getEditor(toEdit);
		}
		catch (BeanCreationException bce) {
			if (log.isWarnEnabled())
				log.warn("Can't get editor [" + table.getEditorName() + "]");
		}
		return null;
	}

	public String getEditorName() {
		return table.getEditorName();
	}

	public void setEditorName(String editorName) {
		table.setEditorName(editorName);
	}
	
	public ReportListView getReportListView() {
		return reportListView;
	}

	public void setReportListView(ReportListView reportListView) {
		this.reportListView = reportListView;
	}

	public PersistentService<T, Serializable> getDataSource() {
		return (PersistentService<T, Serializable>) table.getDataSource(); 
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
		if (controlBox != null) {
			controlBox.removeAll();
			populateControlBox();
		}
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

	/**
	 * @return List of selected models
	 */
	public List<T> getSelected() {
		List<Serializable> keys = table.getChecked();
		List<T> selected = new ArrayList();
		for (Serializable id : keys) {
			T t = getPersistentService().get(id);
			if (t != null)
				selected.add(getPersistentService().get(id));
		}

		return selected;
	}
	
	public List<T> getVisibleSelected() {
		return table.getVisibleSelected();
	}

	/**
	 * @return the propertyValues
	 */
	public PropertyValues getPropertyValues() {
		return propertyValues;
	}

	/**
	 * @param propertyValues the propertyValues to set
	 */
	public void setPropertyValues(PropertyValues propertyValues) {
		this.propertyValues = propertyValues;
	}

	/**
	 * @param listener
	 * @see org.jdal.swing.PageableTable#addEditorListener(org.jdal.ui.EditorListener)
	 */
	public void addEditorListener(EditorListener listener) {
		table.addEditorListener(listener);
	}

	/**
	 * @param listener
	 * @see org.jdal.swing.PageableTable#removeEditorListener(org.jdal.ui.EditorListener)
	 */
	public void removeEditorListener(EditorListener listener) {
		table.removeEditorListener(listener);
	}
}
