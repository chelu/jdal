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
package info.joseluismartin.logic;

import info.joseluismartin.dao.Page;
import info.joseluismartin.dao.Page.Order;
import info.joseluismartin.service.PersistentService;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * PersistentService spring-data Repository adapter. Don't implements getKeys and 
 * template methods get(id, class) and getAll(id, class). Allow to use JpaRepositories
 * with as PersistentServices. It will ignore filters in pages.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class SpringDataPersistentService<T, PK extends Serializable> implements PersistentService<T, PK> {

	JpaRepository<T, PK> repository;

	/**
	 * {@inheritDoc}
	 */
	public Page<T> getPage(Page<T> page) {
		PageablePage<T> pp = new PageablePage<T>(page);
		
		org.springframework.data.domain.Page<T> springPage = repository.findAll(pp);
		page.setCount((int) springPage.getTotalElements());
		page.setData(springPage.getContent());
		page.setPageableDataSource(this);

		return page;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Serializable> getKeys(Page<T> page) {
		throw new RuntimeException("Operation not supported");
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
		return repository.save(entity);
	}

	/**
	 * {@inheritDoc}
	 */
	public void delete(T entity) {
		repository.delete(entity);
		
	}

	/**
	 * {@inheritDoc}
	 */
	public void deleteById(PK id) {
		repository.delete(id);
		
	}

	/**
	 * {@inheritDoc}
	 */
	public List<T> getAll() {
		return repository.findAll();
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection<T> save(Collection<T> collection) {
		return repository.save(collection);
	}

	/**
	 * {@inheritDoc}
	 */
	public void delete(Collection<T> collection) {
		repository.delete(collection);
	}

	/**
	 * {@inheritDoc}
	 */
	public void deleteById(Collection<PK> ids) {
		for (PK id : ids)
			repository.delete(id);
	}

	/**
	 * {@inheritDoc}
	 */
	public T get(PK id) {
		return repository.findOne(id);
	}

	/**
	 * {@inheritDoc}
	 */
	public <E> E get(PK id, Class<E> clazz) {
		throw new RuntimeException("Operation not supported");
	}

	/**
	 * {@inheritDoc}
	 */
	public <E> List<E> getAll(Class<E> clazz) {
		throw new RuntimeException("Operation not supported");
	}

	/**
	 * @return the repository
	 */
	public JpaRepository<T, PK> getRepository() {
		return repository;
	}

	/**
	 * @param repository the repository to set
	 */
	public void setRepository(JpaRepository<T, PK> repository) {
		this.repository = repository;
	}
}

class PageablePage<T> implements Pageable {
	private Page<T> page;

	/**
	 * @param page 
	 */
	public PageablePage(Page<T> page) {
		this.page = page;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getPageNumber() {
		return page.getPage() - 1;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getPageSize() {
		return page.getPageSize();
	}

	/**
	 * {@inheritDoc}
	 */
	public int getOffset() {
		return page.getStartIndex();
	}

	/**
	 * {@inheritDoc}
	 */
	public Sort getSort() {
		Direction d = page.getOrder() == Order.ASC ?  Direction.ASC : Direction.DESC;
		return new Sort(d, page.getSortName());
	}
}