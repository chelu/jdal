/*
 * Copyright 2009-2015 Jose Luis Martin
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

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdal.beans.PropertyUtils;
import org.springframework.beans.BeanUtils;

import com.vaadin.data.Container.Indexed;
import com.vaadin.data.Container.ItemSetChangeNotifier;
import com.vaadin.data.Container.PropertySetChangeNotifier;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.AbstractContainer;

/**
 * Bean container that uses a Integer as itemId and {@link BeanWrapperItem} as Item.
 * Hold data in a List.
 * 
 * @author Jose Luis Martin
 * @since 2.1
 */
public class ListBeanContainer extends AbstractContainer implements Indexed,
		ItemSetChangeNotifier, PropertySetChangeNotifier {
	
	/** Hold all beans in container */
	private transient List<Item> items = new ArrayList<Item>();
	/** Bean type */
	private Class<?> beanClass;
	/** Bean properties used by container */
	private List<String> properties = new ArrayList<String>();
	private Map<String, PropertyDescriptor> propertyDescriptors = 
			new HashMap<String, PropertyDescriptor>();

	public ListBeanContainer(Class<?> beanClass) {
		this(beanClass, null, null);
	}
	
	public ListBeanContainer(Class<?> beanClass, List<?> data) {
		this(beanClass, data, null);
	}
	
	public ListBeanContainer(Class<?> beanClass, List<?> data,  List<String> properties) {
		this.beanClass = beanClass;
		if (properties == null) {
			initDefaultProperties();
		}
		else {
			setProperties(properties);
		}
		
		if (data != null)
			init(data);
	}

	private void setProperties(List<String> properties) {
		this.properties.clear();
		this.propertyDescriptors.clear();
		
		for (String propertyName : properties) {
			addProperty(propertyName);
		}
	
		fireContainerPropertySetChange();
	}

	private void initDefaultProperties() {
		for (PropertyDescriptor pd : BeanUtils.getPropertyDescriptors(beanClass)) { 
			this.properties.add(pd.getName());
			this.propertyDescriptors.put(pd.getName(), pd);
		}
	}

	private void init(List<?> data) {
		for (Object bean : data) {
			this.items.add(createItem(bean));
		}
		
		fireItemSetChange();
	}

	@Override
	public Integer nextItemId(Object itemId) {
		Integer index = getAsIndex(itemId);
		return items.size() > index - 1 ? index + 1 : null;
	}

	/**
	 * Gets an itemId as Long
	 * @param itemId itemId to check
	 * @return long value
	 */
	private Integer getAsIndex(Object itemId) {
		if (itemId instanceof Integer)
			return (Integer) itemId;
		
		throw new IllegalArgumentException("Expected Integer, found + [" + 
				itemId != null ? itemId.getClass().getName() : "null"  + "]");
	}

	@Override
	public Integer prevItemId(Object itemId) {
		Integer index = getAsIndex(itemId);
		
		return index > 1 ? index - 1 : null;
	}

	@Override
	public Integer firstItemId() {
		return items.isEmpty() ? null : 0;
	}

	@Override
	public Integer lastItemId() {
		return items.isEmpty() ? null : items.size() - 1;
	}

	@Override
	public boolean isFirstId(Object itemId) {
		return getAsIndex(itemId) == 0;
	}

	@Override
	public boolean isLastId(Object itemId) {
		return getAsIndex(itemId) == items.size() - 1;
	}

	@Override
	public Object addItemAfter(Object previousItemId)
			throws UnsupportedOperationException {
		return null;
	}

	@Override
	public Item addItemAfter(Object previousItemId, Object newItemId)
			throws UnsupportedOperationException {

		return null;
	}

	@Override
	public Item getItem(Object itemId) {
		return items.get(getAsIndex(itemId));
	}

	@Override
	public Collection<?> getContainerPropertyIds() {
		return this.properties;
	}
	
	@Override
	public Collection<?> getItemIds() {
		return this.items;
	}
	
	@Override
	@SuppressWarnings("rawtypes")
	public Property getContainerProperty(Object itemId, Object propertyId) {
		int index = getAsIndex(itemId);
		
		if (index > this.items.size() - 1)
			return null;
		
		return this.items.get(index).getItemProperty(propertyId);
	}

	@Override
	public Class<?> getType(Object propertyId) {
		return getPropertyDescriptor(propertyId).getPropertyType();

	}

	private PropertyDescriptor getPropertyDescriptor(Object propertyId) {
		return propertyDescriptors.get(propertyId);
	}

	@Override
	public int size() {
		return this.items.size();
	}

	@Override
	public boolean containsId(Object itemId) {
		if (this.items.isEmpty())
			return false;
		
		int index = getAsIndex(itemId);
		
		return 0 >= index && index < this.items.size();
	}

	@Override
	public Item addItem(Object itemId) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object addItem() throws UnsupportedOperationException {
		if (this.items.add(createItem(newBean())))
			return this.items.size() - 1;
		
		return null;
	}

	protected BeanWrapperItem createItem(Object bean) {
		return new BeanWrapperItem(bean, this.properties);
	}

	private Object newBean() {
		return BeanUtils.instantiate(this.beanClass);
	}

	@Override
	public boolean removeItem(Object itemId)
			throws UnsupportedOperationException {
		
		boolean result = this.items.remove(getAsIndex(itemId));
		fireItemSetChange();
		
		return result;
	}

	@Override
	public boolean addContainerProperty(Object propertyId, Class<?> type,
			Object defaultValue) throws UnsupportedOperationException {
		
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeContainerProperty(Object propertyId)
			throws UnsupportedOperationException {
		this.properties.remove(propertyId);
		this.propertyDescriptors.remove(propertyId);
		
		return true;
	}

	@Override
	public boolean removeAllItems() throws UnsupportedOperationException {
		this.items.clear();
		fireItemSetChange();
		
		return true;
	}

	@Override
	public int indexOfId(Object itemId) {
		return getAsIndex(itemId);
	}

	@Override
	public Object getIdByIndex(int index) {
		return index;
	}

	@Override
	public List<?> getItemIds(int startIndex, int numberOfItems) {
		List<Integer> indexes = new ArrayList<Integer>();
		for (int i = startIndex; i < startIndex + numberOfItems;  i++)
			indexes.add(i);
		
		return indexes;
	}

	@Override
	public Object addItemAt(int index) throws UnsupportedOperationException {
		throw new UnsupportedOperationException(); 
	}

	@Override
	public Item addItemAt(int index, Object newItemId)
			throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}
	
	public void addAll(List<?> data) {
		init(data);
	}

	public void addProperty(String name) {
		this.properties.add(name);
		this.propertyDescriptors.put(name, 
				PropertyUtils.getPropertyDescriptor(this.beanClass, name));
	}

	@Override
	public void addPropertySetChangeListener(
			PropertySetChangeListener listener) {

		super.addPropertySetChangeListener(listener);
	}

	@Override
	public void addItemSetChangeListener(ItemSetChangeListener listener) {
		super.addItemSetChangeListener(listener);
	}

	@Override
	public void addListener(PropertySetChangeListener listener) {
		super.addListener(listener);
	}

	@Override
	public void addListener(ItemSetChangeListener listener) {
		super.addListener(listener);
	}

	@Override
	public void removePropertySetChangeListener(
			PropertySetChangeListener listener) {
		super.removePropertySetChangeListener(listener);
	}

	@Override
	public void removeListener(PropertySetChangeListener listener) {
		super.removeListener(listener);
	}

	@Override
	public void removeItemSetChangeListener(ItemSetChangeListener listener) {
		super.removeItemSetChangeListener(listener);
	}

	@Override
	public void removeListener(ItemSetChangeListener listener) {
		super.removeListener(listener);
	}

}
