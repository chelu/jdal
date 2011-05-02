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
package info.joseluismartin.gui.bind;

import info.joseluismartin.gui.Binder;

import java.util.HashMap;
import java.util.Map;

/**
 * Composite Binder methods of a collection of
 * PropertyBinders that bind on the same model 
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 * @param <T> model 
 */
public class CompositeBinder<T> implements Binder<T> {
	
	private BinderFactory binderFactory;
	private Map<String, Binder<?>> binders = new HashMap<String, Binder<?>>();
	/** Default model to bind on for property binders */
	private T model;
	
	public CompositeBinder() {
		
	}
	
	/**
	 * @param T model
	 */
	public CompositeBinder(T model) {
		this.model = model;
	}

	public void bind(Object component, String propertyName) {
		bind(component, propertyName, this);
	}

	
	public void bind(Object component, String propertyName, Object object) {
		PropertyBinder binder = binderFactory.getBinder(component.getClass());
		if (binder != null) {
			binder.bind(component, propertyName, object);
			binders.put(propertyName, binder);
		}
	}

	public void refresh() {
		for (Binder<?> b : binders.values())
			b.refresh();
	}

	public void addBinder(Binder<?> binder, String name) {
		binders.put(name, binder);
	}
	

	
	public void update() {
		for (Binder<?> b : binders.values())
			b.update();
		
	}
	
	public Binder<?> getBinder(String propertyName) {
		return binders.get(propertyName);
	}

	/**
	 * @return the binderFactory
	 */
	public BinderFactory getBinderFactory() {
		return binderFactory;
	}

	/**
	 * @param binderFactory the binderFactory to set
	 */
	public void setBinderFactory(BinderFactory binderFactory) {
		this.binderFactory = binderFactory;
	}

	public T getModel() {
		return model;
	}
	
	public void setModel(T model) {
		this.model = model;
	}

}
