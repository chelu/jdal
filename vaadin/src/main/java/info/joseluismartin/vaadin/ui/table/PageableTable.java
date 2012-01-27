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

import info.joseluismartin.dao.Filter;
import info.joseluismartin.dao.Page;
import info.joseluismartin.dao.PageChangedEvent;
import info.joseluismartin.dao.PaginatorListener;
import info.joseluismartin.service.PersistentService;
import info.joseluismartin.vaadin.ui.FormUtils;
import info.joseluismartin.vaadin.ui.GuiFactory;
import info.joseluismartin.vaadin.ui.form.FormDialog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.vaadin.data.Container;
import com.vaadin.data.Container.ItemSetChangeEvent;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Form;
import com.vaadin.ui.FormFieldFactory;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

import org.springframework.context.MessageSource;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * <p>
 * Vaadin Table with paginator. Use a BeanItemContainer as 
 * table datasource.
 * </p>
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class PageableTable<T> extends CustomComponent implements PaginatorListener, 
	Container.ItemSetChangeListener, ItemClickListener {

	private static final long serialVersionUID = 1L;
	private static final Log log = LogFactory.getLog(PageableTable.class);
	
	/** the table */
	private ConfigurableTable table;
	/** the external paginator */
	private VaadinPaginator<T> paginator;
	/** persistentService */
	private PersistentService<T, Serializable>  service;
	/** page */
	private Page<T> page;
	/** Filter */
	private Filter beanFilter;
	/** container to use when using external paginator */
	private BeanItemContainer<T> container;
	/** Form editor name */
	private String editor;
	/** Gui Factory used to get editor instances */
	private GuiFactory guiFactory;
	/** if true, pagesLength change to pageSize */
	private boolean autoResize = true;
	/** if true, will create a editor when none configured */
	private boolean autoCreateEditor = true;
	/** TableAction List */
	private List<TableButtonListener> actions = new ArrayList<TableButtonListener>();
	/** Filter editor */
	private String filterEditor;
	/** Filter Form */
	private Form filterForm;
	/** FormFieldFactory used when creating editor forms */
	private FormFieldFactory formFieldFactory;
	/** the entity class */
	private Class<T> entityClass;
	/** Message Source */
	@Autowired
	private MessageSource messageSource;
	
	public PageableTable() {
	}
	
	public PageableTable(Class<T> entityClass) {
		this.entityClass = entityClass;
	}
	
	public void init() {
		// build Component
		VerticalLayout vbox = new VerticalLayout();
		vbox.setSizeUndefined();
		vbox.setSpacing(true);
		// filter 
		if (filterEditor != null && filterForm == null) {
			filterForm = (Form) guiFactory.getComponent(filterEditor);
		}
		if (filterForm != null) {
			filterForm.setItemDataSource(new BeanItem<Filter>(beanFilter), filterForm.getVisibleItemProperties());
			vbox.addComponent(filterForm);
		}
		// action group
		if (actions.size() > 0) {
			vbox.addComponent(createButtonBox());
		}
		// table
		vbox.addComponent(table);
		
		// paginator
		if (paginator != null) {
			// get initial page and wrap data in container
			paginator.addPaginatorListener(this);
			page = paginator.getModel();
			loadPage();
			// set external sorting, ie don't call Container.sort()
			table.setSorter(new PageSorter());
			vbox.addComponent(paginator.getComponent());
			table.setPageLength(page.getPageSize());
			if (beanFilter != null)
				page.setFilter(beanFilter);
		}
	
		table.addListener((ItemClickListener) this);
		this.setCompositionRoot(vbox);
		this.setSizeUndefined();
		
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
		if (autoResize)
			table.setPageLength(page.getPageSize());
		
		// table.setCurrentPageFirstItemIndex(page.getStartIndex());
		loadPage();
		
	}

	/**
	 * Load models from page and add to internal bean item container
	 */
	@SuppressWarnings("unchecked")
	private void loadPage() {
		page = service.getPage(paginator.getModel());
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
	public Form getEditorForm() {
		// If there are a cofigured form editor return it.
		if (editor != null) {
			return (Form) guiFactory.getComponent(editor);
		}
		
		// else create a default one
		Form f = new Form();
		f.setSizeFull();
		f.setImmediate(true);
		f.getLayout().setMargin(true);
		f.getLayout().setSizeFull();
		
		if (formFieldFactory != null)
			f.setFormFieldFactory(formFieldFactory);
		
		FormUtils.addOKCancelButtons(f);
		
		return f;
	}
	
	@SuppressWarnings("unchecked")
	public Collection<T> getSelected() {
		Object selection = table.getValue();
		if (selection instanceof Collection)
			return (Collection) selection;
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
			BeanItem<T> bi = (BeanItem<T>) event.getItem();
			String message = messageSource.getMessage("edit", null, null);
			FormDialog dlg = new FormDialog(message + " " + bi.getBean().getClass().getSimpleName());
			dlg.setMessageSource(messageSource);
			dlg.setPersistentService((PersistentService<Object, Serializable>) service);
			Form form = getEditorForm();
			form.setItemDataSource(bi, form.getVisibleItemProperties());
			dlg.setForm(form);
			dlg.init();
			getWindow().addWindow(dlg);
		}
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
	 * @return the formFieldFactory
	 */
	public FormFieldFactory getFormFieldFactory() {
		return formFieldFactory;
	}

	/**
	 * @param formFieldFactory the formFieldFactory to set
	 */
	public void setFormFieldFactory(FormFieldFactory formFieldFactory) {
		this.formFieldFactory = formFieldFactory;
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
		loadPage();
	}

	/**
	 * @return the filterForm
	 */
	public Form getFilterForm() {
		return filterForm;
	}

	/**
	 * @param filterForm the filterForm to set
	 */
	public void setFilterForm(Form filterForm) {
		this.filterForm = filterForm;
	}
}
