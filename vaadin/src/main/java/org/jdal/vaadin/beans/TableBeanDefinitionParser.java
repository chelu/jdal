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

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jdal.vaadin.ui.table.ConfigurableTable;
import org.jdal.vaadin.ui.table.PageableTable;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.parsing.BeanComponentDefinition;
import org.springframework.beans.factory.parsing.CompositeComponentDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.NamespaceHandler;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.core.Conventions;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 *
 */
public class TableBeanDefinitionParser implements BeanDefinitionParser {

	private static final String ENTITY = "entity";
	private static final String ID = "id";
	private static final String PAGEABLE_TABLE_SUFFIX = "PageableTable";
	private static final String TABLE_SUFFIX = "Table";
	private static final String TABLE = "table";
	private static final String SERVICE_SUFFIX = "Service";
	private static final String EDITOR_SUFFIX = "Editor";
	private static final String DATA_SOURCE = "service";
	private static final String PAGINATOR_VIEW = "paginator";
	private static final String PAGINATOR = "paginator";
	private static final String COLUMNS = "columns";
	private static final String ACTIONS = "actions";
	private static final String USE_ACTIONS = "use-actions";
	private static final String GUI_FACTORY = "guiFactory";
	private static final String EDITOR = "editorName";
	private static final String FILTER = "filter";
	private static final String FILTER_FORM = "filter-form";
	private static final String TABLE_SERVICE = "tableService";
	private static final String NAME = "name";
	private static final String MESSAGE_SOURCE = "messageSource";
	private static final String FIELD_FACTORY = "field-factory";
	private static final String SORT_PROPERTY = "sort-property";
	private static final String ORDER = "order";
	private static final String PAGE_SIZE = "page-size";
	private static final String SELECTABLE = "selectable";
	private static final String ENTITY_CLASS="entityClass";
	private static final String SCOPE = "scope";
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("rawtypes")
	public BeanDefinition parse(Element element, ParserContext parserContext) {
		// Defaults
		String entity = null;
		
		if (element.hasAttribute(ENTITY))
			entity = element.getAttribute(ENTITY);
		
		String name = StringUtils.uncapitalize(StringUtils.substringAfterLast(entity, "."));
		
		if (element.hasAttribute(ID))
			name = element.getAttribute(ID);
		
		parserContext.pushContainingComponent(
				new CompositeComponentDefinition(name, parserContext.extractSource(element)));
	
		// Bean names
		String pageableTableBeanName = name + PAGEABLE_TABLE_SUFFIX;
		String tableBeanName = name + TABLE_SUFFIX;
		String dataSource = name + SERVICE_SUFFIX;
		String paginator = PAGINATOR_VIEW;
		String editor = name + EDITOR_SUFFIX;
		String actions = DefaultsBeanDefinitionParser.DEFAULT_TABLE_ACTIONS;
		String guiFactory = DefaultsBeanDefinitionParser.DEFAULT_GUI_FACTORY;
		String scope = BeanDefinition.SCOPE_PROTOTYPE;
		
		if (element.hasAttribute(DATA_SOURCE))
			dataSource = element.getAttribute(DATA_SOURCE);
		
		if (element.hasAttribute(PAGINATOR))
			paginator = element.getAttribute(PAGINATOR);
		
		if (element.hasAttribute(ACTIONS))
			actions = element.getAttribute(ACTIONS);
		
		if (element.hasAttribute(GUI_FACTORY))
			guiFactory = element.getAttribute(GUI_FACTORY);
		
		if (element.hasAttribute(EDITOR))
			editor = element.getAttribute(EDITOR);
		
		if (element.hasAttribute(SCOPE))
			scope = element.getAttribute(SCOPE);

		// create PageableTable
		BeanDefinitionBuilder bdb = BeanDefinitionBuilder.genericBeanDefinition(PageableTable.class);
		bdb.setScope(scope);
		bdb.addPropertyReference(DATA_SOURCE, dataSource);
		bdb.addPropertyReference(PAGINATOR_VIEW, paginator);
		bdb.addPropertyValue(NAME, pageableTableBeanName);
		bdb.addPropertyReference(TABLE, tableBeanName);
		bdb.addPropertyReference(GUI_FACTORY, guiFactory);
		bdb.addPropertyValue(EDITOR, editor);
		bdb.addPropertyValue(ENTITY_CLASS, entity);
		
		if (element.hasAttribute(TABLE_SERVICE)) 
			bdb.addPropertyReference(TABLE_SERVICE, element.getAttribute(TABLE_SERVICE));
		
		if (element.hasAttribute(FILTER))
			bdb.addPropertyReference(FILTER, element.getAttribute(FILTER));

		if (element.hasAttribute(MESSAGE_SOURCE))
			bdb.addPropertyReference(MESSAGE_SOURCE, element.getAttribute(MESSAGE_SOURCE));
		
		if (!element.hasAttribute(USE_ACTIONS) || "true".equals(element.getAttribute(USE_ACTIONS)))
			bdb.addPropertyReference(ACTIONS, actions);
		
		if (element.hasAttribute(FILTER_FORM))
			bdb.addPropertyReference(Conventions.attributeNameToPropertyName(FILTER_FORM), 
					element.getAttribute(FILTER_FORM));
		
		if (element.hasAttribute(SORT_PROPERTY))
			bdb.addPropertyValue("sortName", element.getAttribute(SORT_PROPERTY));
		
		if (element.hasAttribute(ORDER))
			bdb.addPropertyValue(ORDER, element.getAttribute(ORDER));
		
		if (element.hasAttribute(PAGE_SIZE))
			bdb.addPropertyValue(Conventions.attributeNameToPropertyName(PAGE_SIZE), 
					element.getAttribute(PAGE_SIZE));
		
		
		
		parserContext.getDelegate().parseBeanDefinitionAttributes(element, pageableTableBeanName, 
				null, bdb.getBeanDefinition());
		
		BeanDefinitionHolder holder = new BeanDefinitionHolder(bdb.getBeanDefinition(), pageableTableBeanName);
		
		// Test Decorators like aop:scoped-proxy
		NodeList childNodes = element.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node n = childNodes.item(i);
			if (Node.ELEMENT_NODE != n.getNodeType() || COLUMNS.equals(n.getLocalName()))
				continue;
			
			NamespaceHandler handler = parserContext.getReaderContext()
					.getNamespaceHandlerResolver().resolve(n.getNamespaceURI());
			if (handler != null) {
				holder = handler.decorate(n, holder, parserContext);
			}
		}
		
		parserContext.registerBeanComponent(new BeanComponentDefinition(holder));
		
		// registerBeanDefinition(element, parserContext, pageableTableBeanName, bdb);
		
		// create ConfigurableTable
		bdb = BeanDefinitionBuilder.genericBeanDefinition(ConfigurableTable.class);
		bdb.setScope(scope);
	
		NodeList nl = element.getElementsByTagNameNS(element.getNamespaceURI(), COLUMNS);
		
		if (nl.getLength() > 0) {
			List columns = parserContext.getDelegate().parseListElement(
					(Element) nl.item(0), bdb.getRawBeanDefinition());
			bdb.addPropertyValue(COLUMNS, columns);
		}
		
		if (element.hasAttribute(FIELD_FACTORY))
			bdb.addPropertyReference(Conventions.attributeNameToPropertyName(FIELD_FACTORY), 
					element.getAttribute(FIELD_FACTORY));
		
		if (element.hasAttribute(SELECTABLE)) {
			bdb.addPropertyValue(SELECTABLE, element.getAttribute(SELECTABLE));
		}
		else {
			// set selectable by default
			bdb.addPropertyValue(SELECTABLE, true);
		}
		
		registerBeanDefinition(element, parserContext, tableBeanName, bdb);
		
		parserContext.popAndRegisterContainingComponent();
		
		return null;
	}

	/**
	 * Register BeanDefinition and apply default bean attributes.
	 * @param element
	 * @param parserContext
	 * @param beanName
	 * @param bdb
	 */
	private void registerBeanDefinition(Element element, ParserContext parserContext, String beanName,
			BeanDefinitionBuilder bdb) {
		AbstractBeanDefinition bd = bdb.getBeanDefinition();
		parserContext.getDelegate().parseBeanDefinitionAttributes(element, beanName, null, bd);
		BeanComponentDefinition bcd = new BeanComponentDefinition(bdb.getBeanDefinition(), beanName);
		parserContext.registerBeanComponent(bcd);
	}
}
