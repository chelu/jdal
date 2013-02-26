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
package org.jdal.vaadin.beans;


import java.util.HashMap;
import java.util.Map;

import org.jdal.vaadin.ui.ApplicationContextGuiFactory;
import org.jdal.vaadin.ui.table.AddAction;
import org.jdal.vaadin.ui.table.ClearFilterAction;
import org.jdal.vaadin.ui.table.FindAction;
import org.jdal.vaadin.ui.table.RefreshAction;
import org.jdal.vaadin.ui.table.RemoveAction;
import org.jdal.vaadin.ui.table.VaadinPaginator;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.CustomEditorConfigurer;
import org.springframework.beans.factory.config.ListFactoryBean;
import org.springframework.beans.factory.parsing.BeanComponentDefinition;
import org.springframework.beans.factory.parsing.ComponentDefinition;
import org.springframework.beans.factory.parsing.CompositeComponentDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 *
 */
public class DefaultsBeanDefinitionParser implements BeanDefinitionParser {
	public static final String PAGINATOR_VIEW_BEAN_NAME = "paginator";
	public static final String CUSTOM_EDITOR_CONFIGURER_BEAN_NAME = "customEditorConfigurer";
	public static final String DEFAULT_TABLE_ACTIONS = "defaultTableActions";
	public static final String INIT_METHOD_NAME = "init";
	public static final String DEFAULT_GUI_FACTORY = "defaultGuiFactory";

	/**
	 * {@inheritDoc}
	 */
	public BeanDefinition parse(Element element, ParserContext parserContext) {
	
		Object source = parserContext.extractSource(element);
		CompositeComponentDefinition ccd = new CompositeComponentDefinition("jdal-vaadin", source);
		
		ccd.addNestedComponent(registerPropertyEditors(element, parserContext));
		ccd.addNestedComponent(registerPaginatorView(element, parserContext));
		ccd.addNestedComponent(registerDefaultTableActions(element, parserContext));
		ccd.addNestedComponent(registerDefaultGuiFactory(element, parserContext));
		
		parserContext.getReaderContext().fireComponentRegistered(ccd);
		
		return null;
	}
	

	/**
	 * Register a BeanComponentDefinition
	 * @param element
	 * @param parserContext
	 * @param BeanComponentDefinition
	 */
	private void registerBeanComponentDefinition(Element element, ParserContext parserContext, BeanComponentDefinition bcd) {
		parserContext.getDelegate().parseBeanDefinitionAttributes(element, bcd.getBeanName(), null, 
				(AbstractBeanDefinition) bcd.getBeanDefinition());
		BeanDefinitionRegistry registry = parserContext.getRegistry();
		registry.registerBeanDefinition(bcd.getBeanName(), bcd.getBeanDefinition());
	}

	/**
	 * @return
	 */
	private ComponentDefinition registerPaginatorView(Element element, ParserContext parserContext) {
		BeanDefinitionBuilder bdb = BeanDefinitionBuilder.genericBeanDefinition(VaadinPaginator.class);
		bdb.addPropertyValue("pageSizes", "10,20,30,40,50,100,All");
		bdb.setScope(BeanDefinition.SCOPE_PROTOTYPE);
		BeanComponentDefinition bcd = new BeanComponentDefinition(bdb.getBeanDefinition(), PAGINATOR_VIEW_BEAN_NAME);
		registerBeanComponentDefinition(element, parserContext, bcd);	
		return bcd;
	}


	/**
	 * Register s default CustomEditorConfigurer
	 * @return default CustomEditorComfigurer ComponentDefinition
	 */
	private ComponentDefinition registerPropertyEditors(Element element, ParserContext parserContext) {
		BeanDefinitionBuilder bdb = BeanDefinitionBuilder.genericBeanDefinition(
				CustomEditorConfigurer.class);
		Map<String, String> map = new HashMap<String, String>();
		map.put("java.awt.Image", "org.jdal.beans.ImagePropertyEditor");
		map.put("javax.swing.Icon", "org.jdal.beans.IconPropertyEditor");
		bdb.addPropertyValue("customEditors", map);
		BeanComponentDefinition bcd = new BeanComponentDefinition(bdb.getBeanDefinition(), 
				CUSTOM_EDITOR_CONFIGURER_BEAN_NAME);
		registerBeanComponentDefinition(element, parserContext, bcd);
		return bcd;
	}
	
	/**
	 * Register default TablePanel Actions
	 * @param element current element
	 * @param parserContext parserContext
	 * @return a new ComponentDefinition with default table action list.
	 */
	private ComponentDefinition registerDefaultTableActions(Element element, ParserContext parserContext) {
		ManagedList<Object> actions = new ManagedList<Object>(7);
		actions.add(createBeanDefinition(AddAction.class, parserContext));
		actions.add(createBeanDefinition(RefreshAction.class, parserContext));
		actions.add(createBeanDefinition(RemoveAction.class, parserContext));
		actions.add(createBeanDefinition(FindAction.class, parserContext));
		actions.add(createBeanDefinition(ClearFilterAction.class, parserContext));
		
		BeanDefinitionBuilder bdb = BeanDefinitionBuilder.genericBeanDefinition(ListFactoryBean.class);
		bdb.getRawBeanDefinition().setSource(parserContext.extractSource(element));
		bdb.addPropertyValue("sourceList", actions);
		bdb.setScope(BeanDefinition.SCOPE_PROTOTYPE);
		BeanComponentDefinition bcd = new BeanComponentDefinition(bdb.getBeanDefinition(), 
				DEFAULT_TABLE_ACTIONS);
		registerBeanComponentDefinition(element, parserContext, bcd);
		return bcd;
	}


	/**
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private BeanDefinitionHolder createBeanDefinition(Class clazz, ParserContext parserContext) {
		GenericBeanDefinition gbd = new GenericBeanDefinition();
		gbd.setBeanClass(clazz);
		gbd.setInitMethodName(INIT_METHOD_NAME);
		BeanDefinitionHolder holder = new BeanDefinitionHolder(gbd, 
				parserContext.getReaderContext().generateBeanName(gbd));
		
		return holder;
	}
	
	/**
	 * @param element
	 * @param parserContext
	 * @return
	 */
	private ComponentDefinition registerDefaultGuiFactory(Element element, ParserContext parserContext) {
		BeanDefinitionBuilder bdb = BeanDefinitionBuilder.genericBeanDefinition(
				ApplicationContextGuiFactory.class);
		BeanComponentDefinition bcd = new BeanComponentDefinition(bdb.getBeanDefinition(), 
				DEFAULT_GUI_FACTORY);		
		registerBeanComponentDefinition(element, parserContext, bcd);
		return bcd;
	}
	
}
