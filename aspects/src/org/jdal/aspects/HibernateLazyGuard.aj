/*
 * Copyright 2009-2011 Jose Luis Martin.
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.collection.AbstractPersistentCollection;
import org.hibernate.engine.PersistenceContext;
import org.hibernate.impl.SessionImpl;
import org.hibernate.persister.collection.CollectionPersister;

/**
 * Hibernate Guard for LazyInitializationException. Open read only session and 
 * load Uninitialized proxy o collection from database before access to it.
 * 
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
		return  this.isConnectedToSession();
	}

    /** pointcut to match PersistentCollection public methods calls */
	pointcut proxy(AbstractPersistentCollection c) : call (* PersistentCollection.*(..)) && target(c);

	
	/**
	 * Before Advice, if Collection is unitialized open a new Session (non-trasactional)
	 * to initialize Collection
	 * @param c AbstractPersistentCollection 
	 */
	before(AbstractPersistentCollection c) : proxy(c) && !within(HibernateLazyGuard) {
		if (log.isDebugEnabled())
			log.info(thisJoinPointStaticPart.toString());

		if (!c.isDetached()) {
			log.info("PersistentCollection will throw exception: " + c.getOwner().toString());
			Session session = sessionFactory.openSession();
			attachToSession(c, session);
			session.close();
		}
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
			log.debug("Initalizing PersistentCollection of role: " + ps.getRole());
		
		if (!ps.wasInitialized()) {
			SessionImpl source = (SessionImpl) session;
			PersistenceContext context = source.getPersistenceContext();
			CollectionPersister cp = source.getFactory().getCollectionPersister(ps.getRole());

			if (context.getCollectionEntry(ps) == null) {  // detached
				context.addUninitializedDetachedCollection(cp, ps);
			}

			ps.setCurrentSession(context.getSession());
			Hibernate.initialize(ps);
		}
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}
