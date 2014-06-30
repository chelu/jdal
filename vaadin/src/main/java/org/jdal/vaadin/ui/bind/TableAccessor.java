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

import java.util.ArrayList;
import java.util.Collection;

import com.vaadin.data.Container;
import com.vaadin.data.Container.Filterable;
import com.vaadin.ui.Table;

/**
 * ControlAccessor for Table
 * 
 * @author Jose Luis Martin
 * @since 2.0
 */
public class TableAccessor extends VaadinControlAccessor {
	

	public TableAccessor(Object control) {
		super(control);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<?> getControlValue() {
		Container container = getControl().getContainerDataSource();
		if (container instanceof Filterable) 
			((Filterable) container).removeAllContainerFilters();
		
		return new ArrayList<Object>(container.getItemIds());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setControlValue(Object value) {
		if (!(value instanceof Collection))
			throw new IllegalArgumentException("Value must implements Collection");
		
		Collection<?> collection = (Collection<?>) value;
		Container container = getControl().getContainerDataSource();
		container.removeAllItems();
		for (Object itemId : collection)
			container.addItem(itemId);
	}
	
	@Override
	public Table getControl() {
		return (Table) super.getControl();
	}

}
