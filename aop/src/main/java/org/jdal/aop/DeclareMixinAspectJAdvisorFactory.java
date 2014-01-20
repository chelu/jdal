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

import org.aspectj.lang.annotation.DeclareMixin;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.annotation.MetadataAwareAspectInstanceFactory;
import org.springframework.aop.aspectj.annotation.ReflectiveAspectJAdvisorFactory;
import org.springframework.core.annotation.AnnotationUtils;

/**
 * Add support for AspectJ {@link org.aspectj.lang.annotation.DeclareMixin} annotation.
 *  
 * @author Jose Luis Martin
 * @since 2.0
 */
public class DeclareMixinAspectJAdvisorFactory extends ReflectiveAspectJAdvisorFactory {

	@Override
	public Advisor getAdvisor(Method candidateAdviceMethod,
			MetadataAwareAspectInstanceFactory aif,
			int declarationOrderInAspect, String aspectName) {
		
		Advisor advisor =  super.getAdvisor(candidateAdviceMethod, aif, declarationOrderInAspect,
				aspectName);
		
		// test declare mixin annotation
		if (advisor == null) {
		
			DeclareMixin declareMixin = AnnotationUtils.findAnnotation(candidateAdviceMethod, DeclareMixin.class);

			if (declareMixin != null) {
				advisor = new  DeclareMixinAdvisor(candidateAdviceMethod, aif.getAspectInstance(), declareMixin.value());
			}
		}
		
		return advisor;
	}
	
}
