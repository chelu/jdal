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
package org.jdal.logic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jdal.dao.Page;
import org.jdal.service.PersistentService;
import org.jdal.util.BeanUtils;
import org.jdal.util.comparator.PropertyComparator;

/**
 * Persistence Service using a Collection as entity store.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class CollectionPersistenceService<T, PK extends Serializable> implements PersistentService<T, PK> {

	private Collection<T> collection;
	private String propertyKey = "id";
	
	public CollectionPersistenceService() {
		collection = new ArrayList<T>();
	}
	
	/**
	 * @param collection
	 */
	public CollectionPersistenceService(Collection<T> collection) {
		this.collection = collection;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public <K> Page<K> getPage(Page<K> page) {
		if (collection.isEmpty()) {
			page.setCount(0);
			page.setData(new ArrayList<K>());
			return page;
		}
		
		List<K> list = (List<K>) new ArrayList<T>(collection);
		
		int startIndex = page.getStartIndex() < list.size() ? page.getStartIndex() : list.size() - 1;
		int toIndex = (page.getStartIndex() + page.getPageSize());
		toIndex =  toIndex < list.size() ?  toIndex : list.size();
	
		if (!StringUtils.isEmpty(page.getSortName())) {
			Collections.sort(list, new PropertyComparator(page.getSortName()));
			if (page.getOrder() == Page.Order.DESC)
				Collections.reverse(list);
		}
			
		page.setData(list.subList(startIndex, toIndex));
		page.setCount(list.size());
		
		return page;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Serializable> getKeys(Page<T> page) {
		List<Serializable> keys = new ArrayList<Serializable>();
		Page<T> keyPage = page.clone();
		getPage(keyPage);
		for (T model : keyPage.getData()) {
			Object value = BeanUtils.getProperty(model, propertyKey);
			if (value != null)
				keys.add((Serializable) value);
		}
		return keys;
	}

	/**
	 * {@inheritDoc}
	 */
	public T initialize(T entity, int depth) {
		return entity;
	}

	/**
	 * {@inheritDoc}
	 */
	public T initialize(T entity) {
		return entity;
	}

	/**
	 * {@inheritDoc}
	 */
	public T save(T entity) {
		if ( collection.contains(entity)) {
			collection.remove(entity);
		}
		collection.add(entity);
		
		return entity;
	}

	/**
	 * {@inheritDoc}
	 */
	public void delete(T entity) {
		collection.remove(entity);
		
	}

	/**
	 * {@inheritDoc}
	 */
	public void deleteById(PK id) {
		collection.remove(get(id));
		
	}

	/**
	 * {@inheritDoc}
	 */
	public List<T> getAll() {
		return new ArrayList<T>(collection);
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection<T> save(Collection<T> collection) {
		for (T model : collection) {
			save(model);
		}
		
		return collection;
	}

	/**
	 * {@inheritDoc}
	 */
	public void delete(Collection<T> collection) {
		for(T model : collection) 
			delete(model);
	}

	/**
	 * {@inheritDoc}
	 */
	public void deleteById(Collection<PK> ids) {
		for (PK id : ids)
			deleteById(id);
	}

	/**
	 * {@inheritDoc}
	 */
	public T get(PK id) {
		for (T model : collection) {
			Object value = BeanUtils.getProperty(model, propertyKey);
			if (value != null && value.equals(id))
				return model;
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public <E> E get(PK id, Class<E> clazz) {
		return (E) get(id);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public <E> List<E> getAll(Class<E> clazz) {
		return (List<E>) getAll();
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public Class<T> getEntityClass() {
		return (Class<T>) (collection.isEmpty() ? null : collection.iterator().next().getClass());
	}

	/**
	 * @return the collection
	 */
	public Collection<T> getCollection() {
		return collection;
	}

	/**
	 * @param collection the collection to set
	 */
	public void setCollection(Collection<T> collection) {
		this.collection = collection;
	}

	/**
	 * @return the propertyKey
	 */
	public String getPropertyKey() {
		return propertyKey;
	}

	/**
	 * @param propertyKey the propertyKey to set
	 */
	public void setPropertyKey(String propertyKey) {
		this.propertyKey = propertyKey;
	}
}
