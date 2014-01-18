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
import org.springframework.aop.ProxyMethodInvocation;
import org.springframework.aop.support.AopUtils;
import org.springframework.aop.support.IntroductionInfoSupport;

/**
 * DelegatingIntroductionInterceptor that use a Factory to create delegates.
 * Based on {@link org.springframework.aop.support.DelegatingIntroductionInterceptor}
 * 
 * @author Jose Luis Martin
 * @since 2.0
 */
@SuppressWarnings({"serial" })
public class DelegateFactoryIntroductionInterceptor extends IntroductionInfoSupport 
	implements IntroductionInterceptor {
	
	private DelegateFactory delegateFactory;
	private Class<?> interfaceType;
	private Object delegate;

	public DelegateFactoryIntroductionInterceptor(DelegateFactory delegateFactory, Class<?> interfaceType) {
		this.delegateFactory = delegateFactory;
		this.publishedInterfaces.add(interfaceType);
	}

	/**
	 * Subclasses may need to override this if they want to  perform custom
	 * behaviour in around advice. However, subclasses should invoke this
	 * method, which handles introduced interfaces and forwarding to the target.
	 */
	public Object invoke(MethodInvocation mi) throws Throwable {
		if (this.delegate == null)
			this.delegate = createNewDelegate(mi.getThis());


		if (isMethodOnIntroducedInterface(mi)) {
			// Using the following method rather than direct reflection, we
			// get correct handling of InvocationTargetException
			// if the introduced method throws an exception.
			Object retVal = AopUtils.invokeJoinpointUsingReflection(this.delegate, mi.getMethod(), mi.getArguments());

			// Massage return value if possible: if the delegate returned itself,
			// we really want to return the proxy.
			if (retVal == this.delegate && mi instanceof ProxyMethodInvocation) {
				Object proxy = ((ProxyMethodInvocation) mi).getProxy();
				if (mi.getMethod().getReturnType().isInstance(proxy)) {
					retVal = proxy;
				}
			}
			return retVal;
		}

		return mi.proceed();
	}

	protected Object createNewDelegate(Object target) {
		try {
			return delegateFactory.createNewDelegate(target);
		} catch (Exception e) {
			throw new IllegalStateException("Can't create delegate instace of type: [" + 
					interfaceType.getName() + "] : " + e.getMessage());
		}
	}


	public interface DelegateFactory {

		Object createNewDelegate(Object target) throws Exception;
	}

}

