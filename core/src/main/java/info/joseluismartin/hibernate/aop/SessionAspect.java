package info.joseluismartin.hibernate.aop;

import info.joseluismartin.dao.hibernate.HibernateDao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * Spring AOP Aspect to apply session processors to current hibernate session.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */

public class SessionAspect  {

	private static final Log log = LogFactory.getLog(SessionAspect.class);

	private List<SessionProcessor> sessionProcessors;
	private SessionFactory sessionFactory;
	
	/**
	 * Before advice to prepare Session before call 
	 */
	@SuppressWarnings("unchecked")
	public void processSession(JoinPoint jp) {

		if (log.isDebugEnabled()) {
			String entityClassName = "";
			Object target = jp.getTarget();
			if (target instanceof HibernateDao) {
				entityClassName = ((HibernateDao) target).getEntityClass().getSimpleName();
			}		
			log.debug("Advising: " + jp.toShortString() + " of class: " + 
					jp.getTarget().getClass().getSimpleName() +"<" + entityClassName + ">" );
		}
		
		try {
			Session session = sessionFactory.getCurrentSession();
			for (SessionProcessor sp : sessionProcessors)
				sp.processSession(session);
		} catch (HibernateException he) {
			log.error(he);
		}
	}

	/**
	 * @return the sessionProcessors
	 */
	public List<SessionProcessor> getSessionProcessors() {
		return sessionProcessors;
	}

	/**
	 * @param sessionProcessors the sessionProcessors to set
	 */
	public void setSessionProcessors(List<SessionProcessor> sessionProcessors) {
		this.sessionProcessors = sessionProcessors;
	}

	/**
	 * @return the sessionFactory
	 */
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	/**
	 * @param sessionFactory the sessionFactory to set
	 */
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}
