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
package org.jdal.vaadin.ui.bind;

import java.io.Serializable;
import java.util.Collection;

import org.jdal.dao.Dao;
import org.jdal.logic.CollectionPersistenceService;
import org.jdal.vaadin.ui.table.PageableTable;


/**
 * ControlAccessor for PageableTable
 * 
 * @author Jose Luis Martin
 * @since 2.1
 */
public class PageableTableAccessor extends VaadinControlAccessor {
	

	public PageableTableAccessor(Object control) {
		super(control);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getControlValue() {
		return getControl().getService().getAll();
		
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void setControlValue(Object value) {
		if (! (value instanceof Collection)) 
			throw new IllegalArgumentException("Value should implements Collection.");
			
		Collection<Object> collection = (Collection<Object>) value;
		PageableTable<Object> table = getControl();
		Dao<Object, ?extends Serializable> service = table.getService();
		
		if (service == null) {
			service = new CollectionPersistenceService<Object, Serializable>();
			table.setService(service);
		}
		
		service.delete(service.getAll());
		service.save(collection);
		table.refresh();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public PageableTable<Object> getControl() {
		return (PageableTable<Object>) super.getControl();
	}

}
