/*
 * Copyright 2008-2010 the original author or authors.
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
package org.jdal.audit;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * Auditor AOP Around advice to audit modifications on auditable models
 *  
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
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
