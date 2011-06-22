/*
 * Copyright 2009-2011 the original author or authors.
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
package info.joseluismartin.vaadin.ui.table;

import info.joseluismartin.dao.Page;
import info.joseluismartin.dao.PageChangedEvent;
import info.joseluismartin.dao.PaginatorListener;
import info.joseluismartin.service.PersistentService;
import info.joseluismartin.vaadin.ui.Box;
import info.joseluismartin.vaadin.ui.GuiFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.vaadin.data.Container;
import com.vaadin.data.Container.ItemSetChangeEvent;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.Action;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;


/**
 * <p>
 * Vaadin Table with paginator. Use a BeanItemContainer as 
 * table datasource.
 * </p>
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class PageableTable<T> extends CustomComponent implements PaginatorListener, 
	Container.ItemSetChangeListener {

	private static final long serialVersionUID = 1L;
	private static final Log log = LogFactory.getLog(PageableTable.class);
	
	private ConfigurableTable table;
	private VaadinPaginator<T> paginator;
	private PersistentService<T, Serializable>  service;
	private Page<T> page;
	private BeanItemContainer<T> container;
	private String editor;
	private GuiFactory guiFactory;
	private boolean autoResize = false;
	private boolean autoCreateEditor = true;
	private List<TableAction> actions = new ArrayList<TableAction>();
	private Form filterEditor;
	
	public void init() {
		// get initial page and wrap data in container
		paginator.addPaginatorListener(this);
		loadPage();
		// set external sorting, ie don't call Container.sort()
		table.setSorter(new PageSorter());
		table.setPageLength(page.getPageSize());
	
		// build Componenet
		VerticalLayout vbox = new VerticalLayout();
		vbox.setSizeUndefined();
		
		if (filterEditor != null) {
			vbox.addComponent(filterEditor);
			Box.addVerticalStruct(vbox, 5);
		}
		
		if (actions.size() > 0) {
			vbox.addComponent(createButtonBox());
		}
	
		vbox.addComponent(table);
		Box.addVerticalStruct(vbox, 5);
		vbox.addComponent(paginator.getComponent());
	
		this.setCompositionRoot(vbox);
		this.setSizeUndefined();
	}

	
	/**
	 * @return
	 */
	private Component createButtonBox() {
		HorizontalLayout hl = new HorizontalLayout();
		for (TableAction a : actions) {
			a.setTable(this);
			Button b = new Button(a.getCaption(), a);
			b.setIcon(a.getIcon());
			hl.addComponent(b);
		}
		
		return hl;
	}


	/**
	 * {@inheritDoc}
	 */
	public void pageChanged(PageChangedEvent event) {
		table.setPageLength(page.getPageSize());
		loadPage();
	}

	/**
	 * Load models from page and add to bean item container
	 */
	@SuppressWarnings("unchecked")
	private void loadPage() {
		page = service.getPage(paginator.getModel());
		if (page.getData() != null && page.getData().size() > 0) {
			if (container == null) {
				container = new BeanItemContainer(page.getData().get(0).getClass(), page.getData());
				table.setContainerDataSource(container);
			}
			else {
				container.removeAllItems();
				container.addAll(page.getData());
			}
		}
		else {
			if (container != null)
				container.removeAllItems();
		}
		
		paginator.refresh();
	}

	/**
	 * {@inheritDoc}
	 */
	public void containerItemSetChange(ItemSetChangeEvent event) {
		paginator.refresh();
	}
	
	public ConfigurableTable getTable() {		
		return table;
	}

	public void setTable(ConfigurableTable table) {
		this.table = table;
	}

	public VaadinPaginator<T> getPaginator() {
		return paginator;
	}

	public void setPaginator(VaadinPaginator<T> paginator) {
		this.paginator = paginator;
	}

	public PersistentService<T, Serializable> getService() {
		return service;
	}

	public void setService(PersistentService<T, Serializable> service) {
		this.service = service;
	}
	
	class PageSorter implements TableSorter, Serializable {
		
		public void sort(Object[] propertyId, boolean[] ascending) {
			Column c = table.getColumn(propertyId[0].toString());
			if (c != null && c.isSortable()) {
				page.setSortName(c.getSortPropertyName());
				page.setOrder(ascending[0] ? Page.Order.ASC : Page.Order.DESC);
				paginator.firstPage();
			}
		}
	}

	/**
	 * @return
	 * @see info.joseluismartin.dao.Page#getFilter()
	 */
	public Object getFilter() {
		return page.getFilter();
	}


	/**
	 * @param filter
	 * @see info.joseluismartin.dao.Page#setFilter(java.lang.Object)
	 */
	public void setFilter(Object filter) {
		page.setFilter(filter);
	}


	/**
	 * @return the editor
	 */
	public String getEditor() {
		return editor;
	}


	/**
	 * @param editor the editor to set
	 */
	public void setEditor(String editor) {
		this.editor = editor;
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
	 * @return the autoResize
	 */
	public boolean isAutoResize() {
		return autoResize;
	}


	/**
	 * @param autoResize the autoResize to set
	 */
	public void setAutoResize(boolean autoResize) {
		this.autoResize = autoResize;
	}


	/**
	 * @return the actions
	 */
	public List<TableAction> getActions() {
		return actions;
	}


	/**
	 * @param actions the actions to set
	 */
	public void setActions(List<TableAction> actions) {
		this.actions = actions;
	}


	/**
	 * @return the filterEditor
	 */
	public Form getFilterEditor() {
		return filterEditor;
	}


	/**
	 * @param filterEditor the filterEditor to set
	 */
	public void setFilterEditor(Form filterEditor) {
		this.filterEditor = filterEditor;
	}


	/**
	 * @return the autoCreateEditor
	 */
	public boolean isAutoCreateEditor() {
		return autoCreateEditor;
	}


	/**
	 * @param autoCreateEditor the autoCreateEditor to set
	 */
	public void setAutoCreateEditor(boolean autoCreateEditor) {
		this.autoCreateEditor = autoCreateEditor;
	}
}
