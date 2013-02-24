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

import org.jdal.dao.Page;
import org.jdal.service.PersistentService;

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
	implements PersistentService<T, PK> {
	
	private Container container;

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
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public T initialize(T entity) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public T save(T entity) {
		return entity;
	}

	/**
	 * {@inheritDoc}
	 */
	public void delete(T entity) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * {@inheritDoc}
	 */
	public void deleteById(PK id) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * {@inheritDoc}
	 */
	public List<T> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection<T> save(Collection<T> collection) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public void delete(Collection<T> collection) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * {@inheritDoc}
	 */
	public void deleteById(Collection<PK> ids) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * {@inheritDoc}
	 */
	public T get(PK id) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public <E> E get(PK id, Class<E> clazz) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public <E> List<E> getAll(Class<E> clazz) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public Class<T> getEntityClass() {
		// TODO Auto-generated method stub
		return null;
	}

}
