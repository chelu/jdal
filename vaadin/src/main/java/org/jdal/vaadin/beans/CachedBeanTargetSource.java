/*
 * Copyright 2009-2013 the original author or authors.
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
package org.jdal.vaadin.beans;

import org.springframework.aop.target.AbstractBeanFactoryBasedTargetSource;

/**
 * Simple bean factory based target source that caches the target to allow
 * reliable scoped proxies for prototypes.
 * 
 * @author Jose Luis Martin
 *
 */
public class CachedBeanTargetSource extends AbstractBeanFactoryBasedTargetSource {

	private transient Object target;
	
	public Object getTarget() throws Exception {
		if (target != null) 
			return target;
		
		target = getBeanFactory().getBean(getTargetBeanName());
		
		return target;
	}

}
