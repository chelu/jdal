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

import java.io.Serializable;

import org.springframework.aop.TargetSource;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.DependencyDescriptor;

/** 
 * Target source that use a {@link DependencyDescriptor} to lookup target.
 * 
 * @author Jose Luis Martin.
 * @since 2.0
 */
public class DependencyDescriptorTargetSource implements TargetSource, BeanFactoryAware, Serializable {
	
	private transient Object target;
	private DependencyDescriptor descriptor;
	private String beanName;
	private AutowireCapableBeanFactory beanFactory;
	
	
	public DependencyDescriptorTargetSource(DependencyDescriptor descriptor, AutowireCapableBeanFactory beanFactory,
			String beanName) {
		
		this.descriptor = descriptor;
		this.beanName = beanName;
		this.beanFactory = beanFactory;
	}

	@Override
	public Object getTarget() throws Exception {
		if (target == null)
			target = resolveDependency();
		
		return target;
	}

	/**
	 * @return
	 */
	protected Object resolveDependency() {
		return this.beanFactory.resolveDependency(descriptor, beanName);
	}

	public AutowireCapableBeanFactory getBeanFactory() {
		return this.beanFactory;
	}

	@Override
	public boolean isStatic() {
		return true;
	}

	public void setTarget(Object target) {
		this.target = target;
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = (AutowireCapableBeanFactory) this.beanFactory;
		
	}

	@Override
	public Class<?> getTargetClass() {
		return descriptor.getDependencyType();
	}

	@Override
	public void releaseTarget(Object target) throws Exception {
		// do nothing
	}

}
