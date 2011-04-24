package info.joseluismartin.audit;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * An Abstract Auditor usiing  Spring AOP
 *  
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public abstract class AbstractAuditor {


	/**
	 * Do audit
	 * @param pjp the AspectJ proceedingJoinPoint
	 * @throws Throwable Exception
	 * @return audit object
	 */
	public abstract Object audit(ProceedingJoinPoint pjp) throws Throwable;
	
	/** 
	 * Get user from thread local var
	 * @return the user
	 */
	public String getUser() {
//		return SecurityContextHolder.getContext().getAuthentication().getName();
		return "";
	}
}
