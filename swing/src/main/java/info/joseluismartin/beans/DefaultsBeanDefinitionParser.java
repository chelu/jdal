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

import info.joseluismartin.gui.ApplicationContextGuiFactory;
import info.joseluismartin.gui.PaginatorView;
import info.joseluismartin.gui.bind.ConfigurableControlAccessorFactory;
import info.joseluismartin.gui.bind.ControlAccessorBinderFactory;
import info.joseluismartin.gui.table.AddAction;
import info.joseluismartin.gui.table.ApplyFilterAction;
import info.joseluismartin.gui.table.ClearFilterAction;
import info.joseluismartin.gui.table.DeselectAllAction;
import info.joseluismartin.gui.table.HideShowFilterAction;
import info.joseluismartin.gui.table.RemoveAllAction;
import info.joseluismartin.gui.table.SelectAllAction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Action;

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
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * Register Swing defaults BeanDefinitions
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class DefaultsBeanDefinitionParser implements BeanDefinitionParser {

	public static final String PAGINATOR_VIEW_BEAN_NAME = "paginatorView";
	public static final String ACCESSOR_FACTORY_BEAN_NAME = "controlAccessorFactory";
	public static final String BINDER_FACTORY_BEAN_NAME = "binderFactory";
	public static final String CUSTOM_EDITOR_CONFIGURER_BEAN_NAME = "customEditorConfigurer";
	public static final String DEFAULT_TABLE_ACTIONS = "defaultTableActions";
	public static final String INIT_METHOD_NAME = "init";
	public static final String DEFAULT_GUI_FACTORY = "defaultGuiFactory";
	
	/**
	 * {@inheritDoc}
	 */
	public BeanDefinition parse(Element element, ParserContext parserContext) {
	
		Object source = parserContext.extractSource(element);
		CompositeComponentDefinition ccd = new CompositeComponentDefinition("jdal-swing", source);
		
		ccd.addNestedComponent(registerPropertyEditors(element, parserContext));
		ccd.addNestedComponent(registerAccessorFactory(element, parserContext));
		ccd.addNestedComponent(registerBinderFactory(element, parserContext));
		ccd.addNestedComponent(registerPaginatorView(element, parserContext));
		ccd.addNestedComponent(registerDefaultTableActions(element, parserContext));
		ccd.addNestedComponent(registerDefaultGuiFactory(element, parserContext));
		
		parserContext.getReaderContext().fireComponentRegistered(ccd);
		
		return null;
	}
	

	/**
	 * @param registry
	 * @param bcd
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
		BeanDefinitionBuilder bdb = BeanDefinitionBuilder.genericBeanDefinition(PaginatorView.class);
		bdb.addPropertyValue("pageSizes", "10,20,30,40,50,100,All");
		BeanComponentDefinition bcd = new BeanComponentDefinition(bdb.getBeanDefinition(), PAGINATOR_VIEW_BEAN_NAME);
		registerBeanComponentDefinition(element, parserContext, bcd);	
		return bcd;
	}

	/**
	 * @return
	 */
	private ComponentDefinition registerBinderFactory(Element element, ParserContext parserContext) {
		BeanDefinitionBuilder bdb = BeanDefinitionBuilder.genericBeanDefinition(
				ControlAccessorBinderFactory.class);
		BeanComponentDefinition bcd = new BeanComponentDefinition(bdb.getBeanDefinition(), BINDER_FACTORY_BEAN_NAME);
		registerBeanComponentDefinition(element, parserContext, bcd);	
		return bcd;
	}

	/**
	 * @return
	 */
	private ComponentDefinition registerAccessorFactory(Element element, ParserContext parserContext) {
		BeanDefinitionBuilder bdb = BeanDefinitionBuilder.genericBeanDefinition(
				ConfigurableControlAccessorFactory.class);
		BeanComponentDefinition bcd = new BeanComponentDefinition(bdb.getBeanDefinition(), ACCESSOR_FACTORY_BEAN_NAME);
		registerBeanComponentDefinition(element, parserContext, bcd);	
		return bcd;
	}

	/**
	 * @return
	 */
	private ComponentDefinition registerPropertyEditors(Element element, ParserContext parserContext) {
		BeanDefinitionBuilder bdb = BeanDefinitionBuilder.genericBeanDefinition(
				CustomEditorConfigurer.class);
		Map<String, String> map = new HashMap<String, String>();
		map.put("java.awt.Image", "info.joseluismartin.beans.ImagePropertyEditor");
		map.put("javax.swing.Icon", "info.joseluismartin.beans.IconPropertyEditor");
		bdb.addPropertyValue("customEditors", map);
		BeanComponentDefinition bcd = new BeanComponentDefinition(bdb.getBeanDefinition(), 
				CUSTOM_EDITOR_CONFIGURER_BEAN_NAME);
		registerBeanComponentDefinition(element, parserContext, bcd);
		return bcd;
	}
	
	/**
	 * @param element
	 * @param parserContext
	 * @return
	 */
	private ComponentDefinition registerDefaultTableActions(Element element, ParserContext parserContext) {
		ManagedList<Object> actions = new ManagedList<Object>(7);
		actions.add(createBeanDefinition(AddAction.class, parserContext));
		actions.add(createBeanDefinition(SelectAllAction.class, parserContext));
		actions.add(createBeanDefinition(DeselectAllAction.class, parserContext));
		actions.add(createBeanDefinition(RemoveAllAction.class, parserContext));
		actions.add(createBeanDefinition(HideShowFilterAction.class, parserContext));
		actions.add(createBeanDefinition(ApplyFilterAction.class, parserContext));
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
