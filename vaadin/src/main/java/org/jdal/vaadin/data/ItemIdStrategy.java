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

import java.util.Collection;

import com.vaadin.data.Item;
import com.vaadin.data.Container.ItemSetChangeListener;


/**
 * Strategy for generating the itemIds on ContainerDataSource
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public interface ItemIdStrategy extends ItemSetChangeListener {
	
	/**
	 * @return the first itemId
	 */
	Object firstItemId();

	/**
	 * @return the las itemId
	 */
	Object lastItemId();

	/**
	 * Gets the index of an itemId
	 * @param itemId the itemId
	 */
	int indexOfId(Object itemId);

	/**
	 * Get an id by index
	 * @param index the index
	 * @return Id
	 */
	Object getIdByIndex(int index);

	/**
	 * Gets a collection of all itemIds
	 * @return collection with all itemIds
	 */
	Collection<?> getItemIds();
	
	/**
	 * Set the containerDataSource
	 * @param containerDataSource 
	 */
	void setContainerDataSource(ContainerDataSource<?> containerDataSource);
	
	/**
	 * @param index
	 * @param item
	 */
	void itemLoaded(int index, Item item);
	
	public Item getItem(Object itemId);
	
	public boolean containsId(Object itemId);
	

}
