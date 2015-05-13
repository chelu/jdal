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
package org.jdal.vaadin.ui.table;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdal.annotation.SerializableProxy;
import org.jdal.dao.Dao;
import org.jdal.dao.Filter;
import org.jdal.dao.Page;
import org.jdal.dao.Page.Order;
import org.jdal.dao.PageChangedEvent;
import org.jdal.dao.PaginatorListener;
import org.jdal.vaadin.ui.VaadinView;
import org.jdal.vaadin.ui.form.FormDialog;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.dao.DataAccessException;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import com.vaadin.data.Container;
import com.vaadin.data.Container.ItemSetChangeEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;

/**
 * Vaadin Table with paginator. Use a BeanItemContainer as 
 * table datasource and request pages using a {@link Dao}. </p>
 * 
 * @author Jose Luis Martin
 * @see org.jdal.dao.Dao
 * @param <T> bean type
 */
@Configurable
public class PageableTable<T> extends TableComponent<T> implements PaginatorListener, 
	Container.ItemSetChangeListener, ItemClickListener, CloseListener {

	private static final long serialVersionUID = 1L;
	private static final Log log = LogFactory.getLog(PageableTable.class);
	
	/** persistentService */
	@SerializableProxy
	private Dao<T, Serializable> service;
	/** external paginator */
	private VaadinPaginator<T> paginator;
	/** page */
	transient Page<T> page = new Page<T>();
	/** Filter */
	private Filter beanFilter;
	/** if true, pagesLength change to pageSize */
	private boolean autoResize = true;
	/** if true, will create a editor when none configured */
	private boolean autoCreateEditor = true;
	/** Filter editor */
	private String filterEditor;
	/** Filter Form */
	private VaadinView<Filter> filterForm;
	private String name;
	/** propagate service to editors */
	private boolean propagateService = true;
	
	public PageableTable() {
	}
	
	public PageableTable(Class<T> entityClass) {
		super(entityClass);
	}
	
	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {
		// build Component
		VerticalLayout verticalLayout = getVerticalLayout();
		verticalLayout.setSizeUndefined();
		verticalLayout.setSpacing(true);
		
		// filter 
		if (filterEditor != null && filterForm == null) {
			filterForm = (VaadinView<Filter>) getGuiFactory().getView(filterEditor);
		}

		if (filterForm != null) {
			if (beanFilter != null) {
				filterForm.setModel(beanFilter);
			}
			else {
				beanFilter = filterForm.getModel();
			}
			verticalLayout.addComponent((Component) filterForm.getPanel());
		}
		
		// action group
		if (getActions().size() > 0) {
			verticalLayout.addComponent(createButtonBox());
		}
		// table
		verticalLayout.addComponent(getTable());
		verticalLayout.setExpandRatio(getTable(), 1.0f);
		
		// paginator
		if (paginator != null) {
			paginator.setModel(page);
			paginator.addPaginatorListener(this);
			paginator.setNativeButtons(isNativeButtons());
			page.setPageableDataSource(getService());
			// set external sorting, ie don't call Container.sort()
			getTable().setSorter(new PageSorter());
			Component p = paginator.getPanel();
			verticalLayout.addComponent(p);
			verticalLayout.setComponentAlignment(p, Alignment.MIDDLE_CENTER);
			getTable().setPageLength(page.getPageSize());
			if (beanFilter != null)
				page.setFilter(beanFilter);
			
			// get initial page and wrap data in container
			paginator.firstPage();
		}
	
		getTable().addItemClickListener(this);
		this.setSizeUndefined();
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void pageChanged(PageChangedEvent event) {
		if (log.isDebugEnabled()) 
			log.debug("handling page change event [" + event.toString() + "]");
		
		if (autoResize)
			getTable().setPageLength(page.getPageSize());
		
		loadPage();
	}

	/**
	 * Load models from page and add to internal bean item container
	 */
	@SuppressWarnings("unchecked")
	protected void loadPage() {
		Container container = getContainer();
		Class<T> entityClass = getEntityClass();
		
		if (this.page.getData() != null && this.page.getData().size() > 0) {
			
			if (container == null) {
				Class<T> beanClass = (Class<T>) (entityClass != null ? entityClass : this.page.getData().get(0).getClass());
				container = createBeanContainer(beanClass, this.page.getData());
				getTable().setContainerDataSource(container);
				setContainer(container);
			}
			else {
				container.removeAllItems();
				addBeansFromPage(container);
			}
		}
		else {
			if (container != null)
				container.removeAllItems();
		}
		
		paginator.refresh();
	}

	/**
	 * @param container
	 */
	protected void addBeansFromPage(Container container) {
		ReflectionUtils.invokeMethod(ClassUtils.getMethod(container.getClass(), "addAll", Collection.class),
				container, page.getData());
	}
	
	/**
	 * Refresh table
	 */
	@Override
	public void refresh() {
		this.paginator.setPage(page.getPage());
		
		if (this.filterForm != null)
			this.filterForm.refresh();
	}
	
	@Override
	public VaadinView<T> getEditorView() {
		VaadinView<T> view = super.getEditorView();
		if (view != null && this.propagateService)
			view.setPersistentService(service);
		
		return view;
	}
	
	public void filter() {
		if (this.filterForm != null) {
			filterForm.update();
			
			if (!filterForm.validateView()) {
					Notification.show(filterForm.getErrorMessage(), Notification.Type.ERROR_MESSAGE);
			}
		}
		
		firstPage();
	}
	
	/**
	 * @param selected
	 */
	@Override
	public void delete(Collection<?> selected) {
		try {
			List<T> beans = new ArrayList<T>();
			for (Object id : selected) {
				beans.add(getBean(getContainer().getItem(id)));
			}
			
			this.service.delete(beans);
		}
		catch(DataAccessException dae) {
			Notification.show("Error", getMessage("PageableTable.deleteError"), Type.ERROR_MESSAGE);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void containerItemSetChange(ItemSetChangeEvent event) {
		this.paginator.refresh();
	}
	
	
	public ConfigurableTable getTable() {		
		return (ConfigurableTable) super.getTable();
	}

	public void setTable(ConfigurableTable table) {
		super.setTable(table);
	}

	public VaadinPaginator<T> getPaginator() {
		return paginator;
	}

	public void setPaginator(VaadinPaginator<T> paginator) {
		this.paginator = paginator;
	}

	/**
	 * @return the filter Object
	 */
	public Object getFilter() {
		return page.getFilter();
	}


	/**
	 * @param filter
	 */
	public void setFilter(Object filter) {
		page.setFilter(filter);
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

	/**
	 * Sort using page requests
	 */
	class PageSorter implements TableSorter, Serializable {
		
		public void sort(Object[] propertyId, boolean[] ascending) {
			Column c = getTable().getColumn(propertyId[0].toString());
			if (c != null && c.isSortable()) {
				page.setSortName(c.getSortPropertyName());
				page.setOrder(ascending[0] ? Page.Order.ASC : Page.Order.DESC);
				paginator.firstPage();
			}
		}
	}

	/**
	 * @return the beanFilter
	 */
	public Filter getBeanFilter() {
		return beanFilter;
	}

	/**
	 * @param beanFilter the beanFilter to set
	 */
	public void setBeanFilter(Filter beanFilter) {
		this.beanFilter = beanFilter;
	}

	/**
	 * @return the filterEditor
	 */
	public String getFilterEditor() {
		return filterEditor;
	}

	/**
	 * @param filterEditor the filterEditor to set
	 */
	public void setFilterEditor(String filterEditor) {
		this.filterEditor = filterEditor;
	}

	/**
	 * @return the filterForm
	 */
	public VaadinView<Filter> getFilterForm() {
		return filterForm;
	}

	/**
	 * @param filterForm the filterForm to set
	 */
	public void setFilterForm(VaadinView<Filter> filterForm) {
		this.filterForm = filterForm;
	}

	/**
	 * {@inheritDoc}
	 */
	public void windowClose(CloseEvent e) {
		if (((FormDialog) e.getWindow()).isDirty())
			loadPage();
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Go to first page
	 */
	public void firstPage() {
		if (paginator != null)
			paginator.firstPage();
		
	}

	/**
	 * @return
	 * @see org.jdal.dao.Page#getSortName()
	 */
	public String getSortName() {
		return page.getSortName();
	}

	/**
	 * @param sortName
	 * @see org.jdal.dao.Page#setSortName(java.lang.String)
	 */
	public void setSortName(String sortName) {
		page.setSortName(sortName);
	}

	/**
	 * @return
	 * @see org.jdal.dao.Page#getOrder()
	 */
	public Order getOrder() {
		return page.getOrder();
	}

	/**
	 * @param order
	 * @see org.jdal.dao.Page#setOrder(org.jdal.dao.Page.Order)
	 */
	public void setOrder(Order order) {
		page.setOrder(order);
	}

	/**
	 * @return
	 * @see org.jdal.dao.Page#getPageSize()
	 */
	public int getPageSize() {
		return page.getPageSize();
	}

	/**
	 * @param pageSize
	 * @see org.jdal.dao.Page#setPageSize(int)
	 */
	public void setPageSize(int pageSize) {
		page.setPageSize(pageSize);
	}

	public Dao<T, Serializable> getService() {
		return service;
	}

	public void setService(Dao<T, Serializable> service) {
		this.service = service;
		this.page.setPageableDataSource(service);
	}

	public boolean isPropagateService() {
		return propagateService;
	}

	public void setPropagateService(boolean propagateService) {
		this.propagateService = propagateService;
	}
}
