/*
 * Copyright 2009-2012 the original author or authors.
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
package info.joseluismartin.remoting;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractGenericPointcutAdvisor;
import org.springframework.aop.support.StaticMethodMatcherPointcutAdvisor;

/**
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 *
 */
public class RemoteClientAdvisor extends AbstractGenericPointcutAdvisor {

	RemoteClientMethodPointCut pointCut = new RemoteClientMethodPointCut();
	
	public RemoteClientAdvisor(final RemoteReference remoteReference) {
		setAdvice(new MethodInterceptor() {
			public Object invoke(MethodInvocation invocation) throws Throwable {
				return remoteReference;
			}
		});
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Pointcut getPointcut() {
		return pointCut;
	}

}

class RemoteClientMethodPointCut extends StaticMethodMatcherPointcutAdvisor {

	/**
	 * {@inheritDoc}
	 */
	public boolean matches(Method method, Class<?> targetClass) {
		return ("getRemoteReference".equals(method.getName()));
	}
	
}

class RemoteClientInterceptor implements MethodInterceptor {

	private RemoteReference remoteReference;
	
	public RemoteClientInterceptor(RemoteReference remoteReference) {
		this.remoteReference = remoteReference;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Object invoke(MethodInvocation invocation) throws Throwable {
		return remoteReference;
	}
	
}