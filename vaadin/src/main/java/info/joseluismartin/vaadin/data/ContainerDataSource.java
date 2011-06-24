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

import info.joseluismartin.dao.Page;
import info.joseluismartin.service.PersistentService;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanInstantiationException;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataAccessException;

import com.vaadin.data.Buffered;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Container.Indexed;
import com.vaadin.data.Container.ItemSetChangeNotifier;
import com.vaadin.data.Container.Sortable;
import com.vaadin.data.Item.PropertySetChangeListener;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.util.BeanItem;

/**
 * <p>
 * An adapter to use PageableDataSources as Vaadin Container.
 * Use a integer as itemId and load data by page from data source on
 * request.
 * </p>
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class ContainerDataSource<T> implements Container, Sortable, Indexed, 
	ItemSetChangeNotifier, PropertySetChangeListener, Buffered {

	private static final long serialVersionUID = 1L;
	private static final Log log = LogFactory.getLog(ContainerDataSource.class);

	private Page<T> page = new Page<T>();
	
	private PersistentService<T, Serializable> service; 
	private List<String> sortableProperties;
	private List<BeanItem<T> > items = new LinkedList<BeanItem<T>>();
	private Class<T> entityClass;
	private List<ItemSetChangeListener> listeners = new ArrayList<ItemSetChangeListener>();
	private ItemIdStrategy itemIdStrategy;
	
	private Set<Item> dirtyItems = new HashSet<Item>();
	private Set<Item> newItems = new HashSet<Item>();
	
	public ContainerDataSource() {
		this(null, null);
	}
	
	public ContainerDataSource(Class<T> entityClass) {
		this(entityClass, null);
	}
	
	public ContainerDataSource(Class<T> entityClass, PersistentService<T, Serializable> service) {
		this.service = service;
		this.entityClass = entityClass;
		setItemIdStrategy(new IndexedItemIdStrategy());
	}

	public void init() {
		loadPage();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Object nextItemId(Object itemId) {
		return isLastId(itemId) ? null : getIdByIndex(indexOfId(itemId) + 1);
	}

	/**
	 * {@inheritDoc}
	 */
	public Object prevItemId(Object itemId) {
		return isFirstId(itemId) ? null : getIdByIndex(indexOfId(itemId) - 1);
	}

	/**
	 * {@inheritDoc}
	 */
	public Object firstItemId() {
		return itemIdStrategy.firstItemId();
	}

	/**
	 * {@inheritDoc}
	 */
	public Object lastItemId() {
		return itemIdStrategy.lastItemId();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isFirstId(Object itemId) {
		return indexOfId(itemId) == 0;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isLastId(Object itemId) {
		return indexOfId(itemId) == page.getCount() - 1;
	}


	/**
	 * {@inheritDoc}
	 */
	public Object addItemAfter(Object previousItemId)
			throws UnsupportedOperationException {
		throw new UnsupportedOperationException("ContainerDataSourceAdapter don't support adding new Items after to container");
	}

	/**
	 * {@inheritDoc}
	 */
	public Item addItemAfter(Object previousItemId, Object newItemId)
			throws UnsupportedOperationException {
		throw new UnsupportedOperationException("ContainerDataSourceAdapter don't support adding new records after to container");
	}

	/**
	 * {@inheritDoc}
	 */
	public int indexOfId(Object itemId) {
		return itemIdStrategy.indexOfId(itemId);
	}

	/**
	 * {@inheritDoc}
	 */
	public Object getIdByIndex(int index) {
		return itemIdStrategy.getIdByIndex(index);
	}

	/**
	 * {@inheritDoc}
	 */
	public Object addItemAt(int index) throws UnsupportedOperationException {
		throw new UnsupportedOperationException("ContainerDataSourceAdapter don't support adding new records to container");
	}

	/**
	 * {@inheritDoc}
	 */
	public Item addItemAt(int index, Object newItemId)
			throws UnsupportedOperationException {
		throw new UnsupportedOperationException("ContainerDataSourceAdapter don't support adding new records to container");
	}

	/**
	 * {@inheritDoc}
	 */
	public void sort(Object[] propertyId, boolean[] ascending) {
		// only use the first property :I
		page.setSortName(propertyId[0].toString());
		page.setOrder(ascending[0] ? Page.Order.ASC : Page.Order.DESC);
		loadPage();
		fireItemSetChange();
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection<?> getSortableContainerPropertyIds() {
		if (sortableProperties != null)
			return sortableProperties;
		
		if (entityClass != null) {
			List<String> properties = new LinkedList<String>();
			PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(entityClass);
			for (PropertyDescriptor pd : pds)
				properties.add(pd.getName());
			
			return properties;
		}
		
		// if we have data will try introspection
		if (page.getData().size() > 0) {
			BeanItem<T> item = items.get(0);
			return item.getItemPropertyIds();
		}

		return new LinkedList<Object>();
	}

	/**
	 * {@inheritDoc}
	 */
	public Item getItem(Object itemId) {
		
		if (!containsId(itemId))
			return null;

		return getItemByIndex(indexOfId(itemId));
	}

	public int getPageContaining(int index) {
		return index/page.getPageSize() + 1;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Collection<?> getContainerPropertyIds() {
		// if we have data will try introspection
		if (page.getData().size() > 0) {
			BeanItem<T> item = items.get(0);
			return item.getItemPropertyIds();
		}
		
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection<?> getItemIds() {
		return itemIdStrategy.getItemIds();
	}

	/**
	 * {@inheritDoc}
	 */
	public Property getContainerProperty(Object itemId, Object propertyId) {
		Item item =  getItem(itemId);
		return item != null ? item.getItemProperty(propertyId) : null;
	}

	/**
	 * {@inheritDoc}
	 */
	public Class<?> getType(Object propertyId) {
		if (entityClass != null) {
			return BeanUtils.getPropertyDescriptor(entityClass, (String)propertyId)
				.getPropertyType();
		}
		
		// if we have data will try introspection
		if (page.getData().size() > 0) {
			BeanItem<T> item = items.get(0);
			return item.getBean().getClass();
		}
		
		return Object.class;
	}

	/**
	 * {@inheritDoc}
	 */
	public int size() {
		return page.getCount();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean containsId(Object itemId) {
		int index = indexOfId(itemId);
		return index >= 0 && index < page.getCount();
	}

	/**
	 * {@inheritDoc}
	 */
	public Item addItem(Object itemId) throws UnsupportedOperationException {
		throw new UnsupportedOperationException("ContainerDataSourceAdapter don't support adding new records to container");
	}

	/**
	 * {@inheritDoc}
	 */
	public Object addItem() throws UnsupportedOperationException {
		T bean = null;
		try {
			bean = BeanUtils.instantiate(entityClass);
		} catch (BeanInstantiationException be) {
			log.error(be);
			return null;
		}
		
		BeanItem<T> newItem = new BeanItem<T>(bean);
		newItem.addListener(this);
		newItems.add(newItem);
		
		return newItem;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean removeItem(Object itemId) {
		if (!containsId(itemId))
			return false;
		
		int index = (Integer) itemId;	
		
		if (isInPage(index)) {
			service.delete(page.getData().get(globalToPage(index)));
			loadPage();
		}
		else {
			Page<T> oneItem = new Page<T>(1, index);
			oneItem.setFilter(page.getFilter());
			service.getPage(oneItem);
			service.delete(oneItem.getData().get(0));
			page.setCount(page.getCount() - 1);
		}
		
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean addContainerProperty(Object propertyId, Class<?> type,
			Object defaultValue) throws UnsupportedOperationException {
		throw new UnsupportedOperationException("ContainerDataSourceAdapter don't support adding new properties to container");
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean removeContainerProperty(Object propertyId)
			throws UnsupportedOperationException {
		throw new UnsupportedOperationException("ContainerDataSourceAdapter don't support adding new properties to container");
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean removeAllItems() throws UnsupportedOperationException {
		try {
			Page<T> all =  page.clone();
			all.setPageSize(Integer.MAX_VALUE);
			service.delete(all.getData());
		} catch (DataAccessException dae) {
			return false;
		}
	
		return true;
	}

	private void loadPage() {
		service.getPage(page);
		int index = 0;
		items.clear();
		for (T t : page.getData()) {
			
			BeanItem<T> item = getDirtyOrCreate(t);
			item.addListener(this);
			items.add(item);
			itemIdStrategy.itemLoaded(pageToGlobal(index++), item);
		}
	}

	/**
	 * @param t
	 * @return
	 */
	private BeanItem<T> getDirtyOrCreate(T t) {
		BeanItem<T> item = new BeanItem<T>(t);
		// FIXME: check if the bean is in dirty list
		return item;
	}

	/**
	 * @param i
	 * @return
	 */
	private int pageToGlobal(int index) {
		return page.getStartIndex() + index;
	}
	

	/**
	 * Convert global index to page index.
	 * @param index global index
	 * @return the index in current page
	 */
	private int globalToPage(int index) {
		return index - page.getStartIndex();
	}

	private boolean isInPage(int index) {
		return globalToPage(index) >= 0 && globalToPage(index) < page.getPageSize();
	}
	
	public PersistentService<T, Serializable> getService() {
		return service;
	}

	public void setService(PersistentService<T, Serializable> service) {
		this.service = service;
	}
	

	public void setPage(Page<T> page) {
		this.page = page;
		loadPage();
	}

	public List<String> getSortableProperties() {
		return sortableProperties;
	}

	public void setSortableProperties(List<String> sortableProperties) {
		this.sortableProperties = sortableProperties;
	}

	public void addListener(ItemSetChangeListener listener) {
		if (!listeners.contains(listener))
			listeners.add(listener);
	}

	public void removeListener(ItemSetChangeListener listener) {
		listeners.remove(listener);
	}
	
	private void fireItemSetChange() {
		ItemSetChangeEvent isce = new ItemSetChangeEvent() {

			public Container getContainer() {
				return ContainerDataSource.this;
			}
		};
		// must be first 
		itemIdStrategy.containerItemSetChange(isce);
		
		for (ItemSetChangeListener listener : listeners) {
			listener.containerItemSetChange(isce);
		}
	}
	
	public void setPageSize(int pageSize) {
		page.setPageSize(pageSize);
		loadPage();
	}
	
	public int getPageSize() {
		return page.getPageSize();
	}

	/**
	 * @return
	 * @see info.joseluismartin.dao.Page#getFilter()
	 */
	public Object getFilter() {
		return page.getFilter();
	}

	/**
	 * @param filter
	 * @see info.joseluismartin.dao.Page#setFilter(java.lang.Object)
	 */
	public void setFilter(Object filter) {
		page.setFilter(filter);
		loadPage();
		fireItemSetChange();
	}

	/**
	 * 
	 */
	public List<Serializable> getKeys() {
		Page<T> p = new Page<T>(Integer.MAX_VALUE);
		p.setFilter(page.getFilter());
		p.setSortName(page.getSortName());
		p.setOrder(page.getOrder());
		
		return service.getKeys(p);
	}

	/**
	 * @return the itemIdStrategy
	 */
	public ItemIdStrategy getItemIdStrategy() {
		return itemIdStrategy;
	}

	/**
	 * @param itemIdStrategy the itemIdStrategy to set
	 */
	public void setItemIdStrategy(ItemIdStrategy itemIdStrategy) {
		this.itemIdStrategy = itemIdStrategy;
		itemIdStrategy.setContainerDataSource(this);
	}

	/**
	 * @param index
	 */
	public Item getItemByIndex(int index) {
		
		if (!isInPage(index)) {
			if (log.isDebugEnabled())
				log.debug("Page fault on index: " + index);
			page.setPage(getPageContaining(index));
			loadPage();
		}
		int pageIndex = globalToPage(index);
		
		return pageIndex < items.size() ? items.get(pageIndex) : null;		
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public void itemPropertySetChange(com.vaadin.data.Item.PropertySetChangeEvent event) {
		dirtyItems.add((BeanItem<T>) event.getItem());
		
	}
	
	/** 
	 * Save changes to Persistent Service
	 * @return true if all items was updated.
	 */
	@SuppressWarnings("unchecked")
	public boolean save() {
		
		// insert news 
		for (Item i : newItems) {
			try {
				BeanItem<T> bi = (BeanItem<T>) i;
				service.save(bi.getBean());
				newItems.remove(bi);
			}
			catch (DataAccessException dae) {
				log.error(dae);
			}
		}
		
		// update dirties
		for (Item i : dirtyItems) {
			try {
				BeanItem<T> bi = (BeanItem<T>) i;
				service.save(bi.getBean());
				dirtyItems.remove(bi);
			}
			catch (DataAccessException dae) {
				log.error(dae);
			}
		}
		
		return newItems.isEmpty() && dirtyItems.isEmpty();
	}

	/**
	 * {@inheritDoc}
	 */
	public void commit() throws SourceException, InvalidValueException {
		// TODO Auto-generated method stub
		
	}

	/**
	 * {@inheritDoc}
	 */
	public void discard() throws SourceException {
		// TODO Auto-generated method stub
		
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isModified() {
		return dirtyItems.size() > 0 || newItems.size() > 0;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isReadThrough() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isWriteThrough() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setReadThrough(boolean readThrough) throws SourceException {
		// TODO Auto-generated method stub
		
	}

	/**
	 * {@inheritDoc}
	 */
	public void setWriteThrough(boolean writeThrough) throws SourceException, InvalidValueException {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @return
	 */
	public Page<T> getPage() {
		return page;
	}
}
