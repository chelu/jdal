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
package org.jdal.ui.table;


import java.awt.Dimension;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import org.jdal.logic.CollectionPersistenceService;
import org.jdal.ui.AbstractView;
import org.jdal.ui.GuiFactory;
import org.jdal.ui.ListTableModel;
import org.jdal.ui.PageableTable;
import org.jdal.ui.SimpleDialog;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * View for showing Lists in a TablePanel
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class TableListView<T> extends AbstractView<List<T>> {

	private TablePanel<T> table;
	private CollectionPersistenceService<T, Serializable> dataSource = new CollectionPersistenceService<T, Serializable>();
	private String tableName;
	@Autowired
	private GuiFactory guiFactory;
	
	public TableListView() {
		this(new ArrayList<T>());
	}

	public TableListView(List<T> model) {
		super(model);
	}
	
	public TableListView(String tableName) {
		this();
		this.tableName = tableName;
	}
	
	
	@SuppressWarnings("unchecked")
	public void init() {
		if (guiFactory != null && tableName != null) {
			table = (TablePanel<T>) guiFactory.getObject(tableName);
			table.setPersistentService(dataSource);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected JComponent buildPanel() {
		if (table == null) 
			buildTable();
		
		return table;
	}


	private void buildTable() {
		table = new TablePanel<T>();
		PageableTable<T> pageableTable = new PageableTable<T>();
		ListTableModel ltm = new ListTableModel();
		ltm.setUsingIntrospection(true);
		pageableTable.setTableModel(ltm);
		pageableTable.init();
		table.setTable(pageableTable);
		table.setPersistentService(dataSource);
		table.init();
		
	}
	
	@Override
	public void onSetModel(List<T> model) {
		if (model != null && table != null) {
			dataSource.setCollection(model);
			table.getTable().getPaginator().firstPage();
		}
	}
	
	public static void main(String[] args) {
		TableListView<Object> v = new TableListView<Object>();
		SimpleDialog dlg = new SimpleDialog(v.getPanel());
		dlg.setSize(new Dimension(1024, 800));
		dlg.setVisible(true);
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
