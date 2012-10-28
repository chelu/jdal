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
package dao.jpa;

import info.joseluismartin.dao.BeanFilter;
import info.joseluismartin.dao.Filter;
import info.joseluismartin.dao.Page;
import info.joseluismartin.dao.jpa.JpaDao;
import info.joseluismartin.dao.jpa.JpaUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;



/**
 * Test JpaDao
 * 
 * @author Jose Luis Martin
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"jpaDaos.xml"})
public class TestJpaDao extends TestCase {
	
	private static final Log log = LogFactory.getLog(TestJpaDao.class);
	
	@Resource
	private JpaDao<Book, Long> bookDao;
	@Resource JpaDao<Category, Long> categoryDao;
	
	@Test
	@Transactional
	public void testFilter() {
		BookFilter bf = new BookFilter("bookFilter");
		bf.setCategory(new Category("Java"));
		Page<Book> page = new Page<Book>(10);
		page.setFilter(bf);
		bookDao.getPage(page);
		List<Book> books = page.getData();
		assertEquals(10, books.size());
	}
	
	@Test
	@Transactional
	public void testCountQuery() {
		Filter f = new BeanFilter("joinFilter");
		Page<Book> p = new Page<Book>();
		p.setFilter(f);
		bookDao.getPage(p);
		assertEquals(22, p.getCount());
	}
	
	@Test
	@Transactional
	public void testCountCriteria() {
		EntityManager em = bookDao.getEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Book> criteria = cb.createQuery(Book.class);
		
		Root<Book> root = criteria.from(Book.class);
		Join<Book, Author> join = root.join("author");
		criteria.where(join.isNotNull());
		
		CriteriaQuery<Long> countCriteria = JpaUtils.countCriteria(em, criteria);
		Long result = em.createQuery(countCriteria).getSingleResult();
		log.debug("Count: " + result);
	}
	
	@Test
	@Transactional
	public void testCopy() {
		EntityManager em = bookDao.getEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Book> criteria = cb.createQuery(Book.class);
		
		// Fetch join
		Root<Book> root = criteria.from(Book.class);
		Path<String> path = root.join("author").<String>get("name");
		root.fetch("author");
		criteria.select(root);
		
		// SubQuery
		Subquery<String> sq = criteria.subquery(String.class);
		Root<Author> author = sq.from(Author.class);
		sq.select(author.<String>get("name"));
		sq.where(cb.equal(author.<String>get("name"), "Rod"));
		
		criteria.where(cb.in(path).value(sq));
		
		CriteriaQuery<Book> copy = cb.createQuery(Book.class);
		JpaUtils.copyCriteria(criteria, copy);
		
		List<Book> copyBooks = em.createQuery(copy).getResultList();
		List<Book> books = em.createQuery(criteria).getResultList();
		assertEquals(books, copyBooks);
	}
	
	@Test
	@Transactional
	public void testKeys() {
		Page<Book> page = new Page<Book>();
		List<Serializable> keys = bookDao.getKeys(page);
		assertEquals(10, keys.size());	
	}
	
	@Test
	@Transactional
	public void testNamedQuery() {
		BookFilter filter = new BookFilter("booksByAuthorName");
		filter.setAuthorName("Martin");
		Page<Book> page = new Page<Book>();
		page.setFilter(filter);
		page.setSortName("publishedDate");
		page.setOrderDesc();
		bookDao.getPage(page);
		assertEquals(4, page.getData().size());
	}
	
	@Test
	@Transactional
	public void testInitialize() {
		Category category = categoryDao.get(1L);
		categoryDao.initialize(category);
		Set<Book> books = category.getBooks();
	}

}
