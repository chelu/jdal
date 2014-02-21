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
package org.jdal.service;

import java.io.Serializable;

import org.jdal.dao.Dao;
import org.jdal.dao.DaoFactory;
import org.jdal.logic.PersistentManager;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.interceptor.MatchAlwaysTransactionAttributeSource;
import org.springframework.transaction.interceptor.TransactionInterceptor;

/**
 * Persistent Service Factory. Creates PersistentServices on the fly 
 * for entity classes.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class PersistentServiceFactory {
	
	@Autowired
	private DaoFactory daoFactory;
	@Autowired
	private PlatformTransactionManager transactionManager;
	
	/**
	 * Creates a PersistentService with default transaction attributes
	 * @param clazz entity class
	 */
	public <T> PersistentManager<T, Serializable> createPersistentService(Class<T> clazz) {	
		Dao<T, Serializable> dao = daoFactory.createDao(clazz);
		PersistentManager<T, Serializable> manager = new PersistentManager<T, Serializable>();
		manager.setDao(dao);
		
		return makeTransactionalProxy(manager);
	}
	
	/**
	 * Creates a default transactional proxy for service with default transacction attributes
	 * @param <T>
	 * @param service
	 * @return a Tx Proxy for service with default tx attributes
	 */
	@SuppressWarnings("unchecked")
	public <T>  PersistentManager<T, Serializable> makeTransactionalProxy(PersistentManager<T, Serializable> service) {
		ProxyFactory factory = new ProxyFactory(service);
		factory.setInterfaces(new Class[] {Dao.class});
		TransactionInterceptor interceptor = new TransactionInterceptor(transactionManager, 
				new MatchAlwaysTransactionAttributeSource()); 
		factory.addAdvice(interceptor);
		factory.setTarget(service);
		return (PersistentManager<T, Serializable>) factory.getProxy();
	}
}
