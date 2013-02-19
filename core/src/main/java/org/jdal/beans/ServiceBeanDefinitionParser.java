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
package org.jdal.beans;


import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.config.BeanReference;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.parsing.BeanComponentDefinition;
import org.springframework.beans.factory.parsing.CompositeComponentDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.ClassUtils;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Parse jdal:service element and register a {@link org.jdal.dao.Dao} and 
 * {@link org.jdal.service.PersistentService} for a entity.
 * The beans names are build from entity name adding suffixes "Dao" and "Service" 
 * to uncapitalized entity SimpleClassName. For example, for a entity class
 * "com.example.Entity" will create entityDao and entityService beans.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class ServiceBeanDefinitionParser implements BeanDefinitionParser {
	
	private static final String DAO_SUFFIX = "Dao";
	private static final String SERVICE_SUFFIX = "Service";
	private static final String JPA_DAO_CLASS_NAME = "org.jdal.dao.jpa.JpaDao";
	private static final String ENTITY = "entity";
	private static final String DAO_CLASS = "daoClass";
	private static final String SERVICE_CLASS = "serviceClass";
	private static final String PERSISTENT_SERVICE_CLASS_NAME = "org.jdal.logic.PersistentManager";
	private static final String NAME = "name";
	private static final String BUILDER = "builder";
	private static final String CRITERIA = "criteria";
	private static final String CRITERIA_BUILDER_MAP = "criteriaBuilderMap";

	/**
	 * {@inheritDoc}
	 */
	public AbstractBeanDefinition parse(Element element, ParserContext parserContext) {
		
		// default dao and service classes
		String daoClassName = JPA_DAO_CLASS_NAME;
		String serviceClassName = PERSISTENT_SERVICE_CLASS_NAME;
		String name = null;
		
		if (element.hasAttribute(DAO_CLASS)) 
			daoClassName = element.getAttribute(DAO_CLASS);
		
		if (element.hasAttribute(SERVICE_CLASS))
			serviceClassName = element.getAttribute(SERVICE_CLASS);
		
		if (element.hasAttribute(NAME))
			name = element.getAttribute(NAME);
			
		if (element.hasAttribute(ENTITY)) {
			String className = element.getAttribute(ENTITY);
			if (name == null) {
				name = StringUtils.uncapitalize(
					StringUtils.substringAfterLast(className, PropertyUtils.PROPERTY_SEPARATOR));
			}
			parserContext.pushContainingComponent(
					new CompositeComponentDefinition(name, parserContext.extractSource(element)));
		
			// DAO
			BeanDefinitionBuilder daoBuilder  = BeanDefinitionBuilder.genericBeanDefinition(daoClassName);
			NodeList nl = element.getElementsByTagNameNS(element.getNamespaceURI(), CRITERIA);
			if (nl.getLength() > 0) {
				ManagedMap<String, BeanReference> builders = new ManagedMap<String, BeanReference>(nl.getLength());
				for (int i = 0; i < nl.getLength(); i++) {
					Element e = (Element) nl.item(i);
					builders.put(e.getAttribute(NAME), new RuntimeBeanReference(e.getAttribute(BUILDER)));
				}
				daoBuilder.addPropertyValue(CRITERIA_BUILDER_MAP, builders);
			}
			
			daoBuilder.addConstructorArgValue(ClassUtils.resolveClassName(className, null));
			daoBuilder.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
			String daoBeanName = name  + DAO_SUFFIX;
			registerBeanDefinition(parserContext, daoBuilder, daoBeanName); 
			
			// SERVICE
			BeanDefinitionBuilder serviceBuilder = BeanDefinitionBuilder.genericBeanDefinition(serviceClassName);
			serviceBuilder.addPropertyReference("dao", daoBeanName);
			String serviceBeanName = name + SERVICE_SUFFIX;
			registerBeanDefinition(parserContext, serviceBuilder, serviceBeanName); 
			
			parserContext.popAndRegisterContainingComponent();
					
		}
		
		return null;
	}

	private void registerBeanDefinition(ParserContext parserContext, BeanDefinitionBuilder builder,
			String beanName) {
		BeanComponentDefinition bcd = new BeanComponentDefinition(builder.getBeanDefinition(), beanName);
		parserContext.registerBeanComponent(bcd);
	}
}


