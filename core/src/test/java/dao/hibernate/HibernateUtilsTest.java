/*
 * Copyright 2009-2014 Jose Luis Martin.
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
package dao.hibernate;

import model.Book;

import org.hibernate.SessionFactory;
import org.jdal.hibernate.HibernateUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Test {@link HibernateUtils} utility class
 * @author Jose Luis Martin
 * @since 2.1
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"hibernate-dao.xml"})
@DirtiesContext(classMode =ClassMode.AFTER_CLASS)
public class HibernateUtilsTest {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Transactional
	@Test
	public void testExists() {
		Book book = new Book();
		book.setId(7L);
		boolean value = HibernateUtils.exists(book, this.sessionFactory.openSession()); 
		Assert.assertTrue(value);
		book.setId(1L);
		value = HibernateUtils.exists(book, sessionFactory.openSession());
		Assert.assertFalse(value);
	}
}
