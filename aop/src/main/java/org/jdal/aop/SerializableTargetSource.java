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

import org.springframework.aop.target.AbstractBeanFactoryBasedTargetSource;
import org.springframework.beans.factory.BeanFactory;

/**
 * SimpleBeanTargetSource that use a transient target to avoid serialization issues 
 * in a container with prototypes beans.
 * 
 * @author Jose Luis Martin
 * @since 2.0
 */
public class SerializableTargetSource extends AbstractBeanFactoryBasedTargetSource  {
	
	private transient Object target;
	private boolean cacheTargetObject;

	public SerializableTargetSource(BeanFactory beanFactory, String targetBeanName, 
			boolean cacheTargetObject ) {
		setTargetBeanName(targetBeanName);
		setBeanFactory(beanFactory);
		this.cacheTargetObject = cacheTargetObject;
	}
	
	@Override
	public Object getTarget() throws Exception {
		if (cacheTargetObject) {
			if (target == null)
				target = getBeanFactory().getBean(getTargetBeanName());
		
			return target;
		}
		
		return getBeanFactory().getBean(getTargetBeanName());
	}

}
