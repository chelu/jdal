/*
 * Copyright 2008-2011 the original author or authors.
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
package org.jdal.mock;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;


/**
 * Replace singletons in configurableListableBeanFactory  
 * with mocks
 * 
 * @author Jose Luis Martin
 */
public class MockReplacer implements BeanFactoryPostProcessor {
	
	/** log */
	private Log log = LogFactory.getLog(MockReplacer.class);
	/** Map with bean names -> replaced beans instances */
	private Map<String, Object> replacedBeans = new HashMap<String, Object>();

	/**
	 * implements {@link BeanFactoryPostProcessor#postProcessBeanFactory(ConfigurableListableBeanFactory)}
	 * @param factory the BeanFactory to postprocess
	 * @throws BeansException if fail
	 */
	public void postProcessBeanFactory(ConfigurableListableBeanFactory factory)
			throws BeansException {

			for (String name : replacedBeans.keySet()) {
				Object bean = replacedBeans.get(name);
				log.debug("Replacing Bean "
						+ name + " with instance of class " 
						+ bean.getClass());
				factory.registerSingleton(name, bean);
			}
	}
	
	/** 
	 * Add a replaced mock 
	 * @param name name of singleton
	 * @param obj mock
	 */
	public final void add(String name, Object obj) {
		replacedBeans.put(name, obj);
	}

	/**
	 * get the map with replaced beans
	 * @return replacedBeans
	 */
	public Map<String, Object> getReplacedBeans() {
		return replacedBeans;
	}

	/** 
	 * Set the replacedBeans Map
	 * @param replacedBeans Map with replaced beans
	 */
	public void setReplacedBeans(Map<String, Object> replacedBeans) {
		this.replacedBeans = replacedBeans;
	}
		
}
