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
package org.jdal.vaadin.ui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.jdal.service.PersistentService;
import org.jdal.ui.EditorEvent;
import org.jdal.ui.EditorListener;
import org.jdal.ui.ViewSupport;
import org.jdal.ui.bind.ConfigurableControlAccessorFactory;
import org.jdal.ui.bind.ControlAccessorBinderFactory;
import org.jdal.vaadin.ui.bind.VaadinBindingUtils;
import org.springframework.context.i18n.LocaleContextHolder;

import com.vaadin.ui.Component;

/**
 * <p>
 * Base class for Vaadin Views. Subclases must implements buildPanel()  method
 * to create the Vaadin component.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public abstract class AbstractView<T> extends ViewSupport<T> implements VaadinView<T> {
	
	private Component component;
	private List<EditorListener> editorListeners = new ArrayList<EditorListener>();
	private PersistentService<T, ?extends Serializable> persistentService;
	

	public AbstractView() {
		super();
	}

	/**
	 * @param model
	 */
	public AbstractView(T model) {
		super(model);
		setWidth(800);
		setHeight(400);
	}

	/**
	 * {@inheritDoc}
	 */
	public Component getPanel() {
		if (component == null) 
			component = buildPanel();
		
		return component;
	}

	/**
	 * Build the view component
	 * @return view component
	 */
	protected abstract Component buildPanel();

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void checkFactories() {
		if (getControlAccessorFactory() == null) {
			ConfigurableControlAccessorFactory accessorFactory = ConfigurableControlAccessorFactory.getDefaultFactory();
			VaadinBindingUtils.registerControlAccessors(accessorFactory);
			setControlAccessorFactory(accessorFactory);
		}
		
		if (getBinderFactory() == null) {
			setBinderFactory(new ControlAccessorBinderFactory(getControlAccessorFactory()));
		}
	}

	public void addEditorListener(EditorListener listener) {
		if (!editorListeners.contains(listener))
			editorListeners.add(listener);	
	}

	public void removeEditorListener(EditorListener listener) {
		editorListeners.remove(listener);
	}

	public void save() {
		this.persistentService.save(getModel());
		fireEditorEvent();
	}
	
	protected void fireEditorEvent() {
		for (EditorListener el : editorListeners) {
			el.modelChanged(new EditorEvent(this, getModel()));
		}
	}

	public void cancel() {
		// do nothing
	}

	public void setPersistentService(
			PersistentService<T, ? extends Serializable> persistentService) {
		this.persistentService = persistentService;
	}
	
	public PersistentService<T, ? extends Serializable> getPersistentService() {
		return this.persistentService;
	}

	@Override
	protected String getMessage(String code) {
		return getMessage(code, LocaleContextHolder.getLocale());
	}

}
