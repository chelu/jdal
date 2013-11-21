/*
 * Copyright 2009-2012 Jose Luis Martin.
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

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInvocation;
import org.jdal.beans.DefaultSerializableObject;
import org.jdal.beans.SerializableObject;
import org.springframework.aop.IntroductionInterceptor;
import org.springframework.aop.framework.AopContext;
import org.springframework.aop.support.IntroductionInfoSupport;

/**
 * Introducction interceptor for serializable objects.
 * 
 * @author Jose Luis Martin 
 */
public class SerializableIntroductionInterceptor extends IntroductionInfoSupport 
	implements IntroductionInterceptor {
	
	public SerializableIntroductionInterceptor() {
		this.publishedInterfaces.add(SerializableObject.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object invoke(MethodInvocation mi) throws Throwable {
		if (isMethodOnIntroducedInterface(mi)) {
			SerializableObject so = new DefaultSerializableObject(AopContext.currentProxy());
			return so.writeReplace();
		}
		
		return mi.proceed();
	}
}
