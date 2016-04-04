/*
 * Copyright 2009-2015 the original author or authors.
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

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jdal.beans.MessageSourceWrapper;
import org.jdal.ui.EditorEvent;
import org.jdal.ui.EditorListener;
import org.jdal.vaadin.data.ItemUtils;
import org.jdal.vaadin.ui.ApplicationContextGuiFactory;
import org.jdal.vaadin.ui.FormUtils;
import org.jdal.vaadin.ui.GuiFactory;
import org.jdal.vaadin.ui.VaadinView;
import org.jdal.vaadin.ui.form.ViewDialog;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

/**
 * Table based editor.
 * 
 * @author Jose Luis Martin
 * @since 2.1
 */
public class TableComponent<T> extends CustomComponent implements ItemClickListener {

	/** the table */
	private Table table;
	/** container to use when using external paginator */
	private Container container;
	/** Form editor name */
	private String editor;
	/** View dialog name */
	private String dialog = ApplicationContextGuiFactory.VIEW_DIALOG;
	/** Gui Factory used to get editor instances */
	@Autowired
	private GuiFactory guiFactory;
	/** TableAction List */
	private List<TableButtonListener> actions = new ArrayList<TableButtonListener>();
	/** the entity class */
	private Class<T> entityClass;
	/** use native buttons */
	private boolean nativeButtons;
	/** Message Source */
	private MessageSourceWrapper messageSource = new MessageSourceWrapper();
	/** Container class to use */
	private Class<?extends Container> containerClass = BeanItemContainer.class;
	
	private VerticalLayout verticalLayout = new VerticalLayout();
	private List<EditorListener> editorListeners = new ArrayList<EditorListener>();

	public TableComponent() {
		this(null);
	}

	/**
	 * @param entityClass
	 */
	public TableComponent(Class<T> entityClass) {
		this.entityClass = entityClass;
		this.setCompositionRoot(this.verticalLayout);
	}
	
	/**
	 * Create the BeanContainer to use.
	 * @param beanClass bean type in container
	 * @param data intial data.
	 * @return a new BeanContainer
	 */
	protected Container createBeanContainer(Class<T> beanClass, List<T> data) {
		Constructor<?extends Container> ctor = 
				ClassUtils.getConstructorIfAvailable(this.containerClass, Class.class, Collection.class);
		
		if (ctor != null)
			return BeanUtils.instantiateClass(ctor, beanClass, data);
		
		return new BeanItemContainer<T>(beanClass, data);
	}
	
	/**
	 * Create a ButtonBox from TableAction List
	 * @return HorizontalLayout with Buttons
	 */
	protected Component createButtonBox() {
		HorizontalLayout hl = new HorizontalLayout();
		hl.setSpacing(true);
		for (TableButtonListener a : actions) {
			a.setTable(this);
			Button b = FormUtils.newButton(a, this.nativeButtons);
			hl.addComponent(b);
		}
		
		return hl;
	}

	/**
	 * Get default form for edit or add models;
	 * @return form editor
	 */
	@SuppressWarnings("unchecked")
	public VaadinView<T> getEditorView() {
		// If there are a cofigured form editor return it.
		if (editor != null) {
			VaadinView<T> view =  (VaadinView<T>) guiFactory.getView(editor);

			return view;
		}
		
		return null;
	}

	@SuppressWarnings("unchecked")
	public Collection<T> getSelected() {
		Set<T> set = new HashSet<T>();
		Object selection = getTable().getValue();
		
		if (selection == null)
			return set;
		
		if (selection instanceof Collection) {
			return (Collection<T>) selection;
		}
		else {
			set.add((T) selection);
			return set;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void itemClick(ItemClickEvent event) {
		if (event.isDoubleClick()) {
			VaadinView<T> editor = getEditorView();
			if (editor != null) {
				Item item = event.getItem();
				editor.setModel(getBean(item));
				editor.refresh();
				
				editor.addEditorListener(new EditorListener() {
					
					public void modelChanged(EditorEvent e) {
						refresh();
						fireEditorEvent(e);
					}
				});
				
				ViewDialog dlg =  this.guiFactory.newViewDialog(editor, this.dialog);
				dlg.init();
				dlg.center();
				this.getUI().addWindow(dlg);
			}
		}
	}
	
	/**
	 * Get the wrapped bean from item calling to getMethod by  reflection.
	 * @param item item
	 * @return wrapped bean
	 */
	@SuppressWarnings("unchecked")
	protected T getBean(Item item) {
		return (T) ItemUtils.getBean(item);
	}

	public void refresh() {
		// do nothing by default
	}
	
	public void setWidthFull() {
		this.setWidth("100%");
		this.verticalLayout.setWidth("100%");
		this.table.setWidth("100%");
	}
	
	/**
	 * Create default table actinons
	 * @return list with add, refresh and remove actions.
	 */
	public List<TableButtonListener> createDefaultActions() {
		ArrayList<TableButtonListener> defaultActions = new ArrayList<TableButtonListener>();
		defaultActions.add(new AddAction());
		defaultActions.add(new RefreshAction());
		defaultActions.add(new RemoveAction());
		
		return defaultActions;
	}

	/**
	 * By default, pageable table handle item clicks to edit items.
	 * This method disable it.
	 */
	public void disableEditorListener() {
		getTable().removeItemClickListener(this);
	}

	public void enableEditorListener() {
		getTable().addItemClickListener(this);
	}

	public void addItemClickListener(ItemClickListener listener) {
		getTable().addItemClickListener(listener);
	}

	public void removeItemClickListener(ItemClickListener listener) {
		getTable().removeItemClickListener(listener);
	}
	
	public void addEditorListener(EditorListener l) {
		if (!this.editorListeners.contains(l))
			this.editorListeners.add(l);
	}
	
	public void removeEditorListener(EditorListener l) {
		this.editorListeners.remove(l);
	}
	
	protected void fireEditorEvent(EditorEvent e) {
		for (EditorListener el : this.editorListeners)
			el.modelChanged(e);
	}
	
	public Table getTable() {
		return table;
	}

	public void setTable(Table table) {
		this.table = table;
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
	 * @return the editor name
	 */
	public String getEditorName() {
		return editor;
	}
	
	/**
	 * @param editorName the editor to se
	 */
	public void setEditorName(String editorName) {
		this.editor = editorName;
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
		
		for (TableButtonListener action : this.actions) {
			action.setTable(this);
		}
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
	 * @param selected
	 */
	public void delete(Collection<?> selected) {
		for (Object item : selected)
			container.removeItem(item);
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

	public boolean isSelectable() {
		return this.isSelectable();
	}

	public void setSelectable(boolean selectable) {
		this.table.setSelectable(selectable);
	}

	public boolean isNativeButtons() {
		return nativeButtons;
	}

	public void setNativeButtons(boolean nativeButtons) {
		this.nativeButtons = nativeButtons;
	}
	
	public Container getContainer() {
		return container;
	}

	public void setContainer(Container container) {
		this.container = container;
	}

	public MessageSource getMessageSource() {
		return messageSource.getMessageSource();
	}

	@Autowired
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource.setMessageSource(messageSource);
	}
	
	public String getMessage(String code) {
		return this.messageSource.getMessage(code);
	}
	
	/**
	 * @return the containerClass
	 */
	public Class<? extends Container> getContainerClass() {
		return containerClass;
	}

	/**
	 * @param containerClass the containerClass to set
	 */
	public void setContainerClass(Class<? extends Container> containerClass) {
		this.containerClass = containerClass;
	}
	
	/**
	 * @return the dialog
	 */
	public String getDialog() {
		return dialog;
	}

	/**
	 * @param dialog the dialog to set
	 */
	public void setDialog(String dialog) {
		this.dialog = dialog;
	}

}
