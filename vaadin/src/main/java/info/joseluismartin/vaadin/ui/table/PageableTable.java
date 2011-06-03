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

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.vaadin.data.Container;
import com.vaadin.data.Container.ItemSetChangeEvent;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.VerticalLayout;


/**
 * <p>
 * Vaadin Table with paginator. Use a BeanItemContainer as 
 * table datasource.
 * </p>
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
// FIXME: May be better extends ConfigurableTable, review it.
public class PageableTable<T> extends CustomComponent implements PaginatorListener, 
	Container.ItemSetChangeListener {

	private static final long serialVersionUID = 1L;
	private static final Log log = LogFactory.getLog(PageableTable.class);
	
	private ConfigurableTable table;
	private VaadinPaginator<T> paginator;
	private PersistentService<T, Serializable>  service;
	private Page<T> page;
	private BeanItemContainer<T> container;
	
	public void init() {
		// get initial page and wrap data in container
		paginator.addPaginatorListener(this);
		loadPage();
		// set external sorting, ie do't call Container.sort()
		table.setSorter(new PageSorter());
		table.setPageLength(page.getPageSize());
	
		// build Componenet
		VerticalLayout vbox = new VerticalLayout();
		vbox.setSizeUndefined();
		vbox.addComponent(table);
		Box.addVerticalStruct(vbox, 5);
		vbox.addComponent(paginator.getComponent());
	
		this.setCompositionRoot(vbox);
		this.setSizeUndefined();
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
	
	class PageSorter implements TableSorter {
		
		public void sort(Object[] propertyId, boolean[] ascending) {
			Column c = table.getColumn(propertyId[0].toString());
			if (c != null && c.isSortable()) {
				page.setSortName(c.getSortPropertyName());
				page.setOrder(ascending[0] ? Page.Order.ASC : Page.Order.DESC);
				paginator.firstPage();
			}
		}
	}
}
