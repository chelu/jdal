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
