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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vaadin.data.Item;
import com.vaadin.data.Container.ItemSetChangeEvent;
import com.vaadin.data.util.BeanItem;

/**
 * Use Primary Keys as ItemId.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
@SuppressWarnings("rawtypes")
public class PkItemIdStrategy implements ItemIdStrategy {

	Map<Serializable, Integer> indexes = new HashMap<Serializable, Integer>();
	Map<Integer, Serializable> keys = new HashMap<Integer, Serializable>();
	ContainerDataSource containerDataSource;
	String identifierPropertyName = "id";

	/**
	 * {@inheritDoc}
	 */
	public Object firstItemId() {
		return getIdByIndex(0);
	}

	/**
	 * {@inheritDoc}
	 */
	public Object getIdByIndex(int index) {
		Item item = containerDataSource.getItemByIndex(index);
		return item != null ? getIdByItem(item) : null;
	}

	/**
	 * @param item
	 * @return
	 */
	private Serializable getIdByItem(Item item) {
		return (Serializable) item.getItemProperty(identifierPropertyName).getValue();
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection<?> getItemIds() {
		List<Serializable> ids = containerDataSource.getKeys();
		indexes.clear();
		for (int i = 0; i < ids.size(); i++) {
			indexes.put(ids.get(i), i);
		}
		return ids;
	}
		

	/**
	 * {@inheritDoc}
	 */
	public int indexOfId(Object itemId) {
		if (!indexes.containsKey(itemId)) {
			getItemIds();
		}
		
		return indexes.get(itemId);
	}

	/**
	 * {@inheritDoc}
	 */
	public Object lastItemId() {
		Item item= containerDataSource.getItemByIndex(containerDataSource.size() - 1);
		return item != null ? item.getItemProperty(identifierPropertyName): null;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setContainerDataSource(ContainerDataSource<?> containerDataSource) {
		this.containerDataSource = containerDataSource;
	}
	
	public Serializable getKey(Item item) {
		return item.getItemProperty(identifierPropertyName);
	}

	/**
	 * {@inheritDoc}
	 */
	public void itemLoaded(int index, Item item) {
		indexes.put(getIdByItem(item), index);
		
	}

	/**
	 * {@inheritDoc}
	 */
	public void containerItemSetChange(ItemSetChangeEvent event) {
		getItemIds();
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public Item getItem(Object itemId) {
		return new BeanItem<Object>(containerDataSource.getService().get((Serializable) itemId));
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean containsId(Object itemId) {
		return indexes.containsKey(itemId);
	}
}
