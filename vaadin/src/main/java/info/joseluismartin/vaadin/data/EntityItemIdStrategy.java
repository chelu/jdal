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

import com.vaadin.data.Item;
import com.vaadin.data.Container.ItemSetChangeEvent;

/**
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 *
 */
public class EntityItemIdStrategy implements ItemIdStrategy {

	ContainerDataSource<?> containerDataSource;
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
	public Collection<?> getItemIds() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public int indexOfId(Object itemId) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	public Object lastItemId() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setContainerDataSource(ContainerDataSource<?> containerDataSource) {
		// TODO Auto-generated method stub
		
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

}
