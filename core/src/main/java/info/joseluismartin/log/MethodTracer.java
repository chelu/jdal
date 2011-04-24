package info.joseluismartin.log;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;



/**
 * A Log method tracer to use as around advice to avoid "in code" debug
 * Enter/Exit method traces.
 * 
 * Example: 
 * <aop:config>
 *		<aop:aspect id="methodTracerAspect" ref="methodTracer">
 *			<aop:pointcut id="anyMethod" expression="execution(* a.b.c.*(..))"/>
 *			<aop:around pointcut-ref="anyMethod" method="traceMethod"/>
 *		</aop:aspect>
 * </aop:config> 
 * 
 * <bean id="methodTracer" class="es.matchmind.log.MethodTracer"/>
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */

public class MethodTracer {
	/** log */
	private static Log log = LogFactory.getLog(MethodTracer.class);

	/**
	 * Simple Around Advice for trace in/out method execution
  	 * 
	 * @param pjp the joint point
	 * @return returned object.
	 * @throws Throwable for the method
	 */
	public Object traceMethod(ProceedingJoinPoint  pjp) throws Throwable {
		log.debug("Enter method: " + pjp.toShortString());
		Object result = pjp.proceed();
		log.debug("Exit method: " + pjp.toShortString());
		return result;
	}
}
