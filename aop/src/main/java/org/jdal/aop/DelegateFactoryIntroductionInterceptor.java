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

import java.util.Map;
import java.util.WeakHashMap;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.IntroductionInterceptor;
import org.springframework.aop.ProxyMethodInvocation;
import org.springframework.aop.support.AopUtils;
import org.springframework.aop.support.IntroductionInfoSupport;

/**
 * DelegatingIntroductionInterceptor that use a Factory to create delegates.
 * <p>
 * Note: this class include code from {@link org.springframework.aop.support.DelegatePerTargetObjectIntroductionInterceptor}
 * </p>
 * @author Jose Luis Martin
 * @since 2.0
 */
@SuppressWarnings({"serial" })
public class DelegateFactoryIntroductionInterceptor extends IntroductionInfoSupport 
	implements IntroductionInterceptor {
	
	private DelegateFactory delegateFactory;
	private Class<?> interfaceType;

	/** 
	 * Hold weak references to keys as we don't want to interfere with garbage collection..
	 */
	private final Map<Object, Object> delegateMap = new WeakHashMap<Object, Object>();


	public DelegateFactoryIntroductionInterceptor(DelegateFactory delegateFactory, Class<?> interfaceType) {
		this.delegateFactory = delegateFactory;
		this.interfaceType = interfaceType;
		this.publishedInterfaces.add(interfaceType);
	}


	public Object invoke(MethodInvocation mi) throws Throwable {
		if (isMethodOnIntroducedInterface(mi)) {
			Object delegate = getIntroductionDelegateFor(mi.getThis());
			Object retVal = AopUtils.invokeJoinpointUsingReflection(delegate, mi.getMethod(), mi.getArguments());

			if (retVal == delegate && mi instanceof ProxyMethodInvocation) {
				retVal = ((ProxyMethodInvocation) mi).getProxy();
			}
			return retVal;
		}

		return doProceed(mi);
	}


	protected Object doProceed(MethodInvocation mi) throws Throwable {
		return mi.proceed();
	}

	private Object getIntroductionDelegateFor(Object targetObject) {
		synchronized (this.delegateMap) {
			if (this.delegateMap.containsKey(targetObject)) {
				return this.delegateMap.get(targetObject);
			}
			else {
				Object delegate = createNewDelegate(targetObject);
				this.delegateMap.put(targetObject, delegate);
				return delegate;
			}
		}
	}

	protected Object createNewDelegate(Object targetObject) {
		try {
			return delegateFactory.createNewDelegate(targetObject);
		}
		catch(Exception e) {
			throw new IllegalStateException("Cant create delegate instance of type ["  + this.interfaceType.getName() + 
					"] : " + e.getMessage());
		}
	}
	
	public interface DelegateFactory {

		Object createNewDelegate(Object target) throws Exception;
	}
}

