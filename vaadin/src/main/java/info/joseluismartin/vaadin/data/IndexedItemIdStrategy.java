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
package info.joseluismartin.vaadin.data;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

import com.vaadin.data.Item;
import com.vaadin.data.Container.ItemSetChangeEvent;

/**
 * Indexed ItemIdStragy use the index on the item in container as itemId.
 * The itemId of a item will change when filtering, sorting, adding or removing items.
 * If this is not a trouble, this is the faster ItemIdStrategy to use.
 *   
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class IndexedItemIdStrategy implements ItemIdStrategy {
	
	private ContainerDataSource<?> containerDataSource;


	/**
	 * {@inheritDoc}
	 */
	public Object firstItemId() {
		return containerDataSource.size() > 0 ? 0 : null;
	}

	/**
	 * Test for first itemId
	 * @param itemId the itemId
	 * @return true is itemId is the first
	 */
	public boolean isFirstId(Object itemId) {
		if (itemId instanceof Integer && containerDataSource.size() > 0)
			return ((Integer) itemId) == 0;
		
		return false;
	}

	/**
	 * Test for last itemId
	 * @param itemId the itemId
	 * @return true if the itemId is the last
	 */
	public boolean isLastId(Object itemId) {
		if (itemId instanceof Integer && containerDataSource.size() > 0)
			return ((Integer) itemId) == containerDataSource.size() - 1;
		
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public Object lastItemId() {
		return containerDataSource.size() > 0 ?  containerDataSource.size() -1 : null;
	}

	/**
	 * Return the next itemId
	 * @param itemId the itemId
	 * @return the next itemId
	 */
	public Object nextItemId(Object itemId) {
		if (itemId instanceof Integer) {
			return ((Integer) itemId) <= containerDataSource.size() ? ((Integer) itemId) + 1 : null;
		}
		return null;
	}

	/**
	 * Gets the previous itemId
	 * @param itemId 
	 */
	public Object prevItemId(Object itemId) {
		if (itemId instanceof Integer) {
			return ((Integer) itemId) > 0 ? ((Integer) itemId) - 1 : null;
		}
		return null;
	
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Collection<?> getItemIds() {
		LinkedList<Integer> ids = new LinkedList<Integer>();
		for (int i = 0; i < containerDataSource.size(); i++) {
			ids.add(i);
		}
		return Collections.unmodifiableCollection(ids);
	}

	
	/**
	 * {@inheritDoc}
	 */
	public int indexOfId(Object itemId) {
		return (Integer) itemId;
	}


	/**
	 * {@inheritDoc}
	 */
	public Object getIdByIndex(int index) {
		return index;
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
	}

	/**
	 * {@inheritDoc}
	 */
	public void containerItemSetChange(ItemSetChangeEvent event) {
	}

	/**
	 * {@inheritDoc}
	 */
	public Item getItem(Object itemId) {
		if (!containsId(itemId))
			return null;

		return containerDataSource.getItemByIndex(indexOfId(itemId));
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean containsId(Object itemId) {
		int index = indexOfId(itemId);
		return index >= 0 && index < containerDataSource.size();
	}
}
