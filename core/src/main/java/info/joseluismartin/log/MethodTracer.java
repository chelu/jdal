/*
 * Copyright 2008-2011 the original author or authors.
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
 * <bean id="methodTracer" class="info.joseluismartin.log.MethodTracer"/>
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
