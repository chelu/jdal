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


import java.util.HashMap;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JToggleButton;
import javax.swing.text.JTextComponent;

import org.jdal.swing.ApplicationContextGuiFactory;
import org.jdal.swing.PaginatorView;
import org.jdal.swing.Selector;
import org.jdal.swing.bind.AnnotationControlInitializer;
import org.jdal.swing.bind.ComboAccessor;
import org.jdal.swing.bind.LabelAccessor;
import org.jdal.swing.bind.ListAccessor;
import org.jdal.swing.bind.SelectorAccessor;
import org.jdal.swing.bind.TablePanelAccessor;
import org.jdal.swing.bind.TextComponentAccessor;
import org.jdal.swing.bind.ToggleButtonAccessor;
import org.jdal.swing.table.AddAction;
import org.jdal.swing.table.ApplyFilterAction;
import org.jdal.swing.table.ClearFilterAction;
import org.jdal.swing.table.CollectionTableCellRenderer;
import org.jdal.swing.table.DeselectAllAction;
import org.jdal.swing.table.HideShowFilterAction;
import org.jdal.swing.table.RemoveAllAction;
import org.jdal.swing.table.SelectAllAction;
import org.jdal.swing.table.TablePanel;
import org.jdal.ui.View;
import org.jdal.ui.bind.ConfigurableBinderFactory;
import org.jdal.ui.bind.ConfigurableControlAccessorFactory;
import org.jdal.ui.bind.ControlAccessor;
import org.jdal.ui.bind.ViewAccessor;
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
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
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
	public static final String CONTROL_INITIALIZER = "controlInitializer";
	public static final String PERSISTENT_SERVICE = "persistentService";
	public static final String COLLECTION_TABLE_CELL_RENDERER_BEAN_NAME = "collectionTableCellRenderer";

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
		ccd.addNestedComponent(registerCollectionTableCellRenderer(element, parserContext));
//		ccd.addNestedComponent(registerControlInitializer(element, parserContext));
		
		parserContext.getReaderContext().fireComponentRegistered(ccd);
		
		return null;
	}
	

	/**
	 * Register CollectionTableCellRenderer
	 * @param element
	 * @param parserContext
	 * @return
	 */
	private ComponentDefinition registerCollectionTableCellRenderer(Element element, ParserContext parserContext) {
		BeanDefinitionBuilder bdb = BeanDefinitionBuilder.genericBeanDefinition(CollectionTableCellRenderer.class);
		bdb.setScope(BeanDefinition.SCOPE_PROTOTYPE);
		BeanComponentDefinition bcd = new BeanComponentDefinition(bdb.getBeanDefinition(), COLLECTION_TABLE_CELL_RENDERER_BEAN_NAME);
		registerBeanComponentDefinition(element, parserContext, bcd);	
		return bcd;
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
		bdb.setScope(BeanDefinition.SCOPE_PROTOTYPE);
		BeanComponentDefinition bcd = new BeanComponentDefinition(bdb.getBeanDefinition(), PAGINATOR_VIEW_BEAN_NAME);
		registerBeanComponentDefinition(element, parserContext, bcd);	
		return bcd;
	}

	/**
	 * @return
	 */
	private ComponentDefinition registerBinderFactory(Element element, ParserContext parserContext) {
		BeanDefinitionBuilder bdb = BeanDefinitionBuilder.genericBeanDefinition(
				ConfigurableBinderFactory.class);
		bdb.addPropertyReference(ACCESSOR_FACTORY_BEAN_NAME, ACCESSOR_FACTORY_BEAN_NAME);
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
		
		Map<Class<?>, Class<?extends ControlAccessor>> accessors = 
				new ManagedMap<Class<?>,Class<?extends ControlAccessor>>();
		accessors.put(JTextComponent.class, TextComponentAccessor.class);
		accessors.put(JList.class, ListAccessor.class);
		accessors.put(Selector.class, SelectorAccessor.class);
		accessors.put(JToggleButton.class, ToggleButtonAccessor.class);
		accessors.put(JComboBox.class, ComboAccessor.class);
		accessors.put(View.class, ViewAccessor.class);
		accessors.put(JLabel.class, LabelAccessor.class);
		accessors.put(TablePanel.class, TablePanelAccessor.class);
		bdb.addPropertyValue("accessors", accessors);
		
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
	
	/**
	 * @param element
	 * @param parserContext
	 * @return
	 */
	@SuppressWarnings("unused")
	private ComponentDefinition registerControlInitializer(Element element, ParserContext parserContext) {
		BeanDefinitionBuilder bdb = BeanDefinitionBuilder.genericBeanDefinition(
				AnnotationControlInitializer.class);
		
		bdb.addPropertyReference(PERSISTENT_SERVICE, PERSISTENT_SERVICE);
		BeanComponentDefinition bcd = new BeanComponentDefinition(bdb.getBeanDefinition(), 
				CONTROL_INITIALIZER);
		
		registerBeanComponentDefinition(element, parserContext, bcd);
		return bcd;
	}
	

}
