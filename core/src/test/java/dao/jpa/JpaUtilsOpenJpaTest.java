/*
 * Copyright 2009-2013 Jose Luis Martin.
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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("jpaDaos-openjpa.xml")
public class JpaUtilsOpenJpaTest extends JpaUtilsTest {
	
	@Autowired
	private PlatformTransactionManager txManager;
	
	/**
	 * Used only to consume an execption the first time that spring create a transaction.
	 * Seem related to open jpa weaving. Just a work around to run tests.
	 */
	@Test(expected=Exception.class)
	public void consumeOpenJpaException() {
		txManager.commit(txManager.getTransaction(new DefaultTransactionDefinition()));
	}

}
