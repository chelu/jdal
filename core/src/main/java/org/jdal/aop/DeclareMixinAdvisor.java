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

import java.lang.reflect.Method;

import org.jdal.aop.DelegateFactoryIntroductionInterceptor.DelegateFactory;
import org.springframework.aop.aspectj.TypePatternClassFilter;
import org.springframework.aop.support.DefaultIntroductionAdvisor;

/**
 *  Implements AspectJ annotation-style behavior for the DeclareParents annotation.
 *  
 * @author Jose Luis Martin
 * @since 2.0
 */
@SuppressWarnings({"serial" })
public class DeclareMixinAdvisor extends DefaultIntroductionAdvisor {

private TypePatternClassFilter typePatterClassFilter = new TypePatternClassFilter();
	
	/**
	 * Create a new advisor for this DeclareMix method.
	 * @param method declare mix method
	 * @param aspectMetadata the aspect metadata
	 * @param typePattern type pattern the introduction is restricted to
	 */
	public DeclareMixinAdvisor(Method method, Object aspect, String typePattern) {
		super(createAspect(method, aspect), method.getReturnType());
		this.typePatterClassFilter.setTypePattern(typePattern);
	}


	@Override
	public boolean matches(Class<?> clazz) {
		return typePatterClassFilter.getTypePattern() != null ? typePatterClassFilter.matches(clazz) : true;
	}
	
	/**
	 * Create a DelegateFactory using the aspect and factory method
	 * @param method factory method
	 * @param aspect aspect instance
	 * @return a new delegate factory
	 */
	private static DelegateFactoryIntroductionInterceptor createAspect(final Method method, final Object aspect) {
		DelegateFactory delegateFactory = new DelegateFactory() {

			@Override
			public Object createNewDelegate(Object targetObject) throws Exception {
				return method.invoke(aspect, targetObject);
				
			}
		};
			
		return new DelegateFactoryIntroductionInterceptor(delegateFactory, method.getReturnType());
	}

}
