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
package org.jdal.vaadin.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.jdal.dao.Dao;
import org.jdal.dao.Page;

import com.vaadin.data.Container;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Container.Filterable;
import com.vaadin.data.Container.Sortable;

/**
 * Let a Vaadin container to be a persistent service.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class ContainerPersistentServiceAdapter<T, PK extends Serializable> 
	implements Dao<T, PK> {
	
	private Container container;
	
	public ContainerPersistentServiceAdapter() {
		
	}
	
	public ContainerPersistentServiceAdapter(Container container) {
		this.container = container;
	}
	

	/**
	 * {@inheritDoc}
	 */
	
	public <K> Page<K> getPage(Page<K> page) {
		if (container instanceof Sortable) {
			Sortable sortable = (Sortable) container;
			sortable.sort(new Object[] {page.getSortName()}, new boolean[] {page.getOrder() == Page.Order.ASC});
		}
		
		if (container instanceof Filterable) {
			Filterable filterable = (Filterable) container;
			filterable.addContainerFilter(getFilter(page.getFilter()));
		}
		
		Collection itemIds = container.getItemIds();
		
		List data = new ArrayList<T>(page.getPageSize());
		
		for (Object itemId : itemIds) {
			data.add(container.getItem(itemId));
		}
		
		page.setData(data);
		
		return page;
	}

	/**
	 * @param filter
	 * @return
	 */
	private Filter getFilter(Object filter) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Serializable> getKeys(Page<T> page) {
		return new ArrayList(container.getItemIds());
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
		container.addItem(entity);
		return entity;
	}

	/**
	 * {@inheritDoc}
	 */
	public void delete(T entity) {
		container.removeItem(entity);
	}

	/**
	 * {@inheritDoc}
	 */
	public void deleteById(PK id) {
		
	}

	/**
	 * {@inheritDoc}
	 */
	public List<T> getAll() {
		return new ArrayList(container.getItemIds());
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection<T> save(Collection<T> collection) {
		for (T t: collection)
			save(t);
		
		return collection;
	}

	/**
	 * {@inheritDoc}
	 */
	public void delete(Collection<T> collection) {
		for (T t : collection)
			delete(t);
	}

	/**
	 * {@inheritDoc}
	 */
	public void deleteById(Collection<PK> ids) {
	}

	/**
	 * {@inheritDoc}
	 */
	public T get(PK id) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	public <E> E get(PK id, Class<E> clazz) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	public <E> List<E> getAll(Class<E> clazz) {
		return (List<E>) getAll();
	}

	/**
	 * {@inheritDoc}
	 */
	public Class<T> getEntityClass() {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean exists(PK id) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<T> findByNamedQuery(String queryName,
			Map<String, Object> queryParams) {
		// TODO Auto-generated method stub
		return null;
	}

}
