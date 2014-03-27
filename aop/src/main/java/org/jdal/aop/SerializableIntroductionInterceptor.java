/*
 * Copyright 2009-2014 Jose Luis Martin.
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
package org.jdal.aop;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.IntroductionInterceptor;
import org.springframework.aop.support.IntroductionInfoSupport;

/**
 * Introducction interceptor for serializable objects.
 * 
 * @author Jose Luis Martin
 * @since 2.0
 */
public class SerializableIntroductionInterceptor extends IntroductionInfoSupport 
	implements IntroductionInterceptor {
	
	private SerializableReference reference;
	
	public SerializableIntroductionInterceptor() {
		this(null);
		
	}

	public SerializableIntroductionInterceptor(SerializableReference reference) {
		this.reference = reference;
		this.publishedInterfaces.add(SerializableObject.class);;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object invoke(MethodInvocation mi) throws Throwable {
		if (isMethodOnIntroducedInterface(mi)) {
			reference.serialize();
			return reference;
		}
		
		return mi.proceed();
	}
}
