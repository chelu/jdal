/*
 * Copyright 2009-2011 original author or authors.
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
package org.jdal.aspects;

import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.FlushMode;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.collection.AbstractPersistentCollection;
import org.hibernate.engine.PersistenceContext;
import org.hibernate.impl.SessionImpl;
import org.hibernate.persister.collection.CollectionPersister;
import org.springframework.orm.hibernate3.SessionFactoryUtils;

/**
 * Hibernate Guard for LazyInitializationException. Open read only session and 
 * load Uninitialized proxy o collection from database before access to it.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
privileged public aspect HibernateLazyGuard {

	/** hibernate session factory used to create sessions */
	private SessionFactory sessionFactory;
	/** common logging log */
	private static final Log log = LogFactory.getLog(HibernateLazyGuard.class);
	
	/**
	 * Test if PersistentCollection is connected to session
	 * @return true if not connected (detached)
	 */
	public boolean  AbstractPersistentCollection.isDetached() {
		return  !(isConnectedToSession() && session.isConnected());
	}

    /** match Collection public methods calls */
	pointcut collection() : call (public * Collection.*(..)) && !within(HibernateLazyGuard);
	
	/** match join points on this advice */
	pointcut me() : cflow(adviceexecution()) && within(HibernateLazyGuard);
	
	/** match access to intialized flag on APC, only if call is in flow of woven collection method */
	pointcut initialized(AbstractPersistentCollection apc) : 
		get(boolean org.hibernate.collection.AbstractPersistentCollection.initialized) && 
		this(apc);
	
	/**
	 * Before Advice, if Collection is unitialized open a new Session
	 * to initialize Collection
	 * @param c AbstractPersistentCollection 
	 */
	boolean around(AbstractPersistentCollection apc) : initialized(apc) && !cflow(me())   {
		if (log.isDebugEnabled())
			log.debug(thisJoinPointStaticPart.toString());

		if (!SessionFactoryUtils.hasTransactionalSession(sessionFactory) &&
				!apc.wasInitialized() && apc.isDetached()) {
			log.warn("PersistentCollection will throw exception: " + apc.getRole());
			Session session = sessionFactory.openSession();
			session.setFlushMode(FlushMode.MANUAL);
			Transaction tx = null;
			try {
				tx = session.beginTransaction();
				attachToSession(apc, session);
				Hibernate.initialize(apc);  // will throw lazy if attach was failed
				tx.commit();
			} 
			catch (RuntimeException e) {
				tx.rollback();	
				throw e;
			}
			finally {
				session.close();
			}
		}
		// return true
		return proceed(apc);
	}
	

	/**
	 * If PersistentCollection is uninitialized, attach it to new session and 
	 * initialize it. 
	 * @param ps persistent collection
	 * @param session hibernate sesion to use for initialization
	 */
	@SuppressWarnings("unchecked")
	public void attachToSession(AbstractPersistentCollection ps, Session session) {
		if (log.isDebugEnabled())
			log.debug("Attatching PersistentCollection of role: " + ps.getRole());

		if (!ps.wasInitialized()) {
			SessionImpl source = (SessionImpl) session;
			PersistenceContext context = source.getPersistenceContext();
			CollectionPersister cp = source.getFactory().getCollectionPersister(ps.getRole());

			if (!context.containsCollection(ps)) {  // detached
				context.addUninitializedDetachedCollection(cp, ps);
			}

			ps.setCurrentSession(context.getSession());
		}
	}
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}
