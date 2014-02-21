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
package org.jdal.vaadin.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jdal.dao.Dao;
import org.jdal.dao.Page;

import com.vaadin.data.Container.ItemSetChangeEvent;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;

/**
 * ItemIdStrategy that use entities as item id
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 * @since 2.0
 */
public class EntityItemIdStrategy implements ItemIdStrategy {

	private ContainerDataSource<?> containerDataSource;
	
	/**
	 * {@inheritDoc}
	 */
	public Object firstItemId() {
		return containerDataSource.getIdByIndex(0);
	}

	/**
	 * {@inheritDoc}
	 */
	public Object getIdByIndex(int index) {
		return containerDataSource.getItemByIndex(index);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public Collection<?> getItemIds() {
		Page<Object> p = (Page<Object>) containerDataSource.getPage().clone();
		p.setPageSize(Integer.MAX_VALUE);
		((Dao<Object, Serializable>) containerDataSource.getService()).getPage(p);
		return p.getData();
	}

	/**
	 * {@inheritDoc}
	 */
	public int indexOfId(Object itemId) {
		// FIXME: this will iterate over all entities. 
		for (int i = 0; i < containerDataSource.size(); i++) {
			if (itemId.equals(containerDataSource.getItemByIndex(i)))
				return i;
		}
		
		return -1;
	}

	/**
	 * {@inheritDoc}
	 */
	public Object lastItemId() {
		return getIdByIndex(containerDataSource.size() - 1);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setContainerDataSource(ContainerDataSource<?> containerDataSource) {
		this.containerDataSource = containerDataSource;
		
	}

	/**
	 * {@inheritDoc}
	 */
	public void itemLoaded(int index, Item item) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * {@inheritDoc}
	 */
	public void containerItemSetChange(ItemSetChangeEvent event) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * {@inheritDoc}
	 */
	public Item getItem(Object itemId) {
		return new BeanItem<Object>(itemId);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean containsId(Object itemId) {
		return true;
	}

	@Override
	public List<?> getItemIds(int startIndex, int numberOfItems) {
		List<Object> ids = new ArrayList<Object>(numberOfItems);
		
		for (int i = 0; i < numberOfItems; i++)
			ids.add(getIdByIndex((startIndex + i)));
		
		return ids;
		
	}

}
