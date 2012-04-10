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
package info.joseluismartin.beans;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.parsing.BeanComponentDefinition;
import org.springframework.beans.factory.parsing.CompositeComponentDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * Parse jdal:service element and register Dao and PersistentService for an entity.
 * The beans names are build from entity name adding suffixes "Dao" and "Service" 
 * to uncapitalized entity simple class name. For example, for a entity 
 * "com.example.Entity" class will create entityDao and entityService beans.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class ServiceBeanDefinitionParser implements BeanDefinitionParser {
	
	private static String DAO_SUFFIX = "Dao";
	private static String SERVICE_SUFFIX = "Service";
	private static String JPA_DAO_CLASS_NAME = "info.joseluismartin.dao.jpa.JpaDao";
	private static String ENTITY = "entity";
	private static String DAO_CLASS = "daoClass";
	private static String SERVICE_CLASS = "serviceClass";
	private static String PERSISTENT_SERVICE_CLASS_NAME = "info.joseluismartin.logic.PersistentManager";

	/**
	 * {@inheritDoc}
	 */
	public AbstractBeanDefinition parse(Element element, ParserContext parserContext) {
		BeanDefinitionRegistry registry = parserContext.getRegistry();
		
		// default dao and service classes
		String daoClassName = JPA_DAO_CLASS_NAME;
		String serviceClassName = PERSISTENT_SERVICE_CLASS_NAME;
		
		if (element.hasAttribute(DAO_CLASS)) 
			daoClassName = element.getAttribute(DAO_CLASS);
		
		if (element.hasAttribute(SERVICE_CLASS))
			serviceClassName = element.getAttribute(SERVICE_CLASS);
		
		if (element.hasAttribute(ENTITY)) {
			String className = element.getAttribute(ENTITY);
			String name = StringUtils.uncapitalize(
					StringUtils.substringAfterLast(className, PropertyUtils.PROPERTY_SEPARATOR));
		
			GenericBeanDefinition dao =  new GenericBeanDefinition();
			dao.setBeanClassName(daoClassName);
			ConstructorArgumentValues cav = new ConstructorArgumentValues();
			cav.addGenericArgumentValue(className);
			dao.setConstructorArgumentValues(cav);
		
			String daoBeanName = name  + DAO_SUFFIX;
			registry.registerBeanDefinition(daoBeanName, dao);
			
			GenericBeanDefinition service =  new GenericBeanDefinition();
			service.setBeanClassName(serviceClassName);
			service.getPropertyValues().add("dao", new RuntimeBeanReference(daoBeanName));
			String serviceBeanName = name + SERVICE_SUFFIX;
			registry.registerBeanDefinition(serviceBeanName, service);
			
			Object source = parserContext.extractSource(element);
			CompositeComponentDefinition ccd = new CompositeComponentDefinition(name, source);
			ccd.addNestedComponent(new BeanComponentDefinition(dao, daoBeanName));
			ccd.addNestedComponent(new BeanComponentDefinition(service, serviceBeanName));
			
			parserContext.getReaderContext().fireComponentRegistered(ccd);
		}
		
		return null;
	}
}


