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
package aop;

import java.util.List;

import junit.framework.Assert;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.DeclareMixin;
import org.jdal.aop.DeclareMixinAspectJAdvisorFactory;
import org.junit.Test;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.annotation.SingletonMetadataAwareAspectInstanceFactory;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.interceptor.ExposeInvocationInterceptor;

/**
 * Test DeclareMixin annotation
 */
public class DeclareMixinAspectJAdvisorFactoryTest {

	@Test
	public void testDeclareMixin() {
		NotTargetAware notTargetAware = new NotTargetAware();
		DeclareMixinAspectJAdvisorFactory factory = new DeclareMixinAspectJAdvisorFactory();
		
		TargetAware targetAware = (TargetAware) createProxy(notTargetAware,
				factory.getAdvisors(
						new SingletonMetadataAwareAspectInstanceFactory(new DeclareMixinMakeTargetAware(),"someBean")), 
						NotTargetAware.class);

		Assert.assertEquals(targetAware, targetAware.getTarget());
	}
	
	protected Object createProxy(Object target, List<Advisor> advisors, Class<?>... interfaces) {
		ProxyFactory pf = new ProxyFactory(target);
		if (interfaces.length > 1 || interfaces[0].isInterface()) {
			pf.setInterfaces(interfaces);
		}
		else {
			pf.setProxyTargetClass(true);
		}

		// Required everywhere we use AspectJ proxies
		pf.addAdvice(ExposeInvocationInterceptor.INSTANCE);

		for (Object a : advisors) {
			pf.addAdvisor((Advisor) a);
		}

		pf.setExposeProxy(true);
		return pf.getProxy();
	}

	@Aspect
	public static class DeclareMixinMakeTargetAware {
		
		@DeclareMixin("aop.NotTargetAware")
		public TargetAware createTargetAware(Object target) {
			return new DefaultTargetAware(target);
		}
	}
}
