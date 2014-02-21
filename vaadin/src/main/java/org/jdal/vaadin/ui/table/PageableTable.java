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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdal.dao.Filter;
import org.jdal.dao.Page;
import org.jdal.dao.Page.Order;
import org.jdal.dao.PageChangedEvent;
import org.jdal.dao.PaginatorListener;
import org.jdal.service.PersistentService;
import org.jdal.ui.EditorEvent;
import org.jdal.ui.EditorListener;
import org.jdal.vaadin.ui.FormUtils;
import org.jdal.vaadin.ui.GuiFactory;
import org.jdal.vaadin.ui.VaadinView;
import org.jdal.vaadin.ui.form.FormDialog;
import org.jdal.vaadin.ui.form.ViewDialog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.MessageSource;

import com.vaadin.data.Container;
import com.vaadin.data.Container.ItemSetChangeEvent;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;

/**
 * Vaadin Table with paginator. 
 * <p>Use a BeanItemContainer as 
 * table datasource and request pages using a {@link PersistentService}. </p>
 * 
 * @author Jose Luis Martin
 * @see org.jdal.service.PersistentService
 */
@Configurable
public class PageableTable<T> extends CustomComponent implements PaginatorListener, 
	Container.ItemSetChangeListener, ItemClickListener, CloseListener {

	private static final long serialVersionUID = 1L;
	private static final Log log = LogFactory.getLog(PageableTable.class);
	
	/** the table */
	private ConfigurableTable table;
	/** the external paginator */
	private VaadinPaginator<T> paginator;
	/** persistentService */
	private transient PersistentService<T, Serializable>  service;
	/** page */
	private transient Page<T> page = new Page<T>();
	/** Filter */
	private Filter beanFilter;
	/** container to use when using external paginator */
	private BeanItemContainer<T> container;
	/** Form editor name */
	private String editor;
	/** Gui Factory used to get editor instances */
	@Autowired
	private transient GuiFactory guiFactory;
	/** if true, pagesLength change to pageSize */
	private boolean autoResize = true;
	/** if true, will create a editor when none configured */
	private boolean autoCreateEditor = true;
	/** TableAction List */
	private List<TableButtonListener> actions = new ArrayList<TableButtonListener>();
	/** Filter editor */
	private String filterEditor;
	/** Filter Form */
	private VaadinView<Filter> filterForm;
	/** the entity class */
	private Class<T> entityClass;
	/** Message Source */
	@Autowired
	private MessageSource messageSource;
	private String name;
	private VerticalLayout verticalLayout;
	
	public PageableTable() {
	}
	
	public PageableTable(Class<T> entityClass) {
		this.entityClass = entityClass;
	}
	
	@SuppressWarnings("unchecked")
	public void init() {
		// build Component
		verticalLayout = new VerticalLayout();
		verticalLayout.setSizeUndefined();
		verticalLayout.setSpacing(true);
		
		// filter 
		if (filterEditor != null && filterForm == null) {
			filterForm = (VaadinView<Filter>) guiFactory.getView(filterEditor);
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
		if (actions.size() > 0) {
			verticalLayout.addComponent(createButtonBox());
		}
		// table
		verticalLayout.addComponent(table);
		verticalLayout.setExpandRatio(table, 1.0f);
		
		// paginator
		if (paginator != null) {
			paginator.setModel(page);
			paginator.addPaginatorListener(this);
			page.setPageableDataSource(service);
			// get initial page and wrap data in container
			paginator.firstPage();
			// set external sorting, ie don't call Container.sort()
			table.setSorter(new PageSorter());
			Component p = paginator.getPanel();
			verticalLayout.addComponent(p);
			verticalLayout.setComponentAlignment(p, Alignment.MIDDLE_CENTER);
			table.setPageLength(page.getPageSize());
			if (beanFilter != null)
				page.setFilter(beanFilter);
		}
	
		table.addItemClickListener(this);
		this.setCompositionRoot(verticalLayout);
		this.setSizeUndefined();
		
	}
	
	public void setWidthFull() {
		this.setWidth("100%");
		verticalLayout.setWidth("100%");
		table.setWidth("100%");
	}

	/**
	 * Create a ButtonBox from TableAction List
	 * @return HorizontalLayout with Buttons
	 */
	private Component createButtonBox() {
		HorizontalLayout hl = new HorizontalLayout();
		hl.setSpacing(true);
		for (TableButtonListener a : actions) {
			a.setTable(this);
			Button b = FormUtils.newButton(a);
			hl.addComponent(b);
		}
		
		return hl;
	}

	/**
	 * {@inheritDoc}
	 */
	public void pageChanged(PageChangedEvent event) {
		if (log.isDebugEnabled()) 
			log.debug("handling page change event [" + event.toString() + "]");
		
		if (autoResize)
			table.setPageLength(page.getPageSize());
		
		loadPage();
	}

	/**
	 * Load models from page and add to internal bean item container
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void loadPage() {
		if (page.getData() != null && page.getData().size() > 0) {
			if (container == null) {
				Class beanClass = entityClass != null ? entityClass : page.getData().get(0).getClass();
				container = new BeanItemContainer(beanClass, page.getData());
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
	 * Get default form for edit or add models;
	 * @return form editor
	 */
	@SuppressWarnings("unchecked")
	public VaadinView<T> getEditorView() {
		// If there are a cofigured form editor return it.
		if (editor != null) {
			return (VaadinView<T>) guiFactory.getView(editor);
		}
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public Collection<T> getSelected() {
		Object selection = table.getValue();
		if (selection instanceof Collection)
			return (Collection<T>) selection;
		else {
			Set<T> set = new HashSet<T>();
			set.add((T) selection);
			return set;
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public void itemClick(ItemClickEvent event) {
		if (event.isDoubleClick()) {
			VaadinView<T> editor = getEditorView();
			if (editor != null) {
				BeanItem<T> bi = (BeanItem<T>) event.getItem();
				editor.setModel(bi.getBean());
				editor.refresh();
				
				editor.addEditorListener(new EditorListener() {
					
					public void modelChanged(EditorEvent e) {
						refresh();
					}
				});
				
				ViewDialog dlg =  this.guiFactory.newViewDialog(editor);
				dlg.init();
				dlg.center();
				this.getUI().addWindow(dlg);
			}
		}
	}
	
	/**
	 * By default, pageable table handle item clicks to edit items.
	 * This method disable it.
	 */
	public void disableEditorListener() {
		table.removeItemClickListener(this);
	}
	
	public void enableEditorListener() {
		table.addItemClickListener(this);
	}
	
	public void addItemClickListener(ItemClickListener listener) {
		table.addItemClickListener(listener);
	}
	
	public void removeItemClickListener(ItemClickListener listener) {
		table.removeItemClickListener(listener);
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
	 * @return the editor
	 * @deprecated use getEditorName instead
	 */
	public String getEditor() {
		return editor;
	}
	
	/**
	 * @param editor the editor to se
	 * @deprecated use getEditorName instead
	 */
	public void setEditor(String editor) {
		this.editor = editor;
	}

	/**
	 * @param editorName the editor to se
	 */
	public void setEditorName(String editorName) {
		this.editor = editorName;
	}
	
	/**
	 * @return the editor name
	 */
	public String getEditorName() {
		return editor;
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
	public List<TableButtonListener> getActions() {
		return actions;
	}


	/**
	 * @param actions the actions to set
	 */
	public void setActions(List<TableButtonListener> actions) {
		this.actions = actions;
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
	 * @return the entityClass
	 */
	public Class<T> getEntityClass() {
		return entityClass;
	}

	/**
	 * @param entityClass the entityClass to set
	 */
	public void setEntityClass(Class<T> entityClass) {
		this.entityClass = entityClass;
	}
	
	/**
	 * Sort using page requests
	 */
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
	 * @param selected
	 */
	@SuppressWarnings("unchecked")
	public void delete(Collection<?> selected) {
		service.delete((Collection<T>) selected);
	}

	/**
	 * 
	 */
	public void refresh() {
		paginator.setPage(page.getPage());
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
	 * @return the verticalLayout
	 */
	public VerticalLayout getVerticalLayout() {
		return verticalLayout;
	}

	/**
	 * @param verticalLayout the verticalLayout to set
	 */
	public void setVerticalLayout(VerticalLayout verticalLayout) {
		this.verticalLayout = verticalLayout;
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
	
	public boolean isSelectable() {
		return table.isSelectable();
	}

	public void setSelectable(boolean selectable) {
		table.setSelectable(selectable);
	}
}
