package info.joseluismartin.audit;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * Auditor AOP Around advice to audit modifications on auditable models
 *  
 * @author Jose Luis Martin - (jolmarting@matchmind.es)
 */
public class Auditor extends AbstractAuditor {

	/** log */
	private static final Log LOG = LogFactory.getLog(Auditor.class);
	/** user holder thread local */ 
	
	/**
	 * Around advice to audit auditable models
	 * 
	 * @param pjp to pjp
	 * @return object result
	 * @throws Throwable Exception
	 */
	public Object audit(ProceedingJoinPoint pjp) throws Throwable {
		LOG.debug("Auditing on method: " + pjp.toShortString());
		Object result = pjp.proceed();
		if (pjp.getTarget() instanceof Auditable) {
			Auditable auditable = (Auditable) (pjp.getTarget());
			audit(auditable);
		} else {
			LOG.warn("Tried to audit a non-auditable object. Check your "
					+ "AOP configuration on applicationContext.xml");
		}
		return result;
	}

	/** 
	 * Audit an auditable model 
	 * 
	 * @param auditable auditable model
	 */
	protected void audit(Auditable auditable) {
		Date date = new Date();
		auditable.setModificationDate(date);
		auditable.setModificationUser("testUser");
	}
	
	
}
