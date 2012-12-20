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

import info.joseluismartin.gui.ListTableModel;
import info.joseluismartin.gui.PageableTable;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.parsing.BeanComponentDefinition;
import org.springframework.beans.factory.parsing.CompositeComponentDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Parse swing:table elements. Create the ListTableModel, PageableTable and TablePanel 
 * BeanDefinitions.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class TableBeanDefinitionParser implements BeanDefinitionParser {
	
	private static final String ENTITY = "entity";
	private static final String ID = "id";
	private static final String TABLE_PANEL_SUFFIX = "Table";
	private static final String TABLE_PANEL_CLASS ="tablePanelClass";
	private static final String LIST_TABLE_MODEL_SUFFIX = "TableModel";
	private static final String PAGEABLE_TABLE_SUFFIX = "PageableTable";
	private static final String SERVICE_SUFFIX = "Service";
	private static final String EDITOR_SUFFIX = "Editor";
	private static final String DATA_SOURCE = "dataSource";
	private static final String PAGINATOR_VIEW = "paginatorView";
	private static final String PAGINATOR = "paginator";
	private static final String TABLE_MODEL = "tableModel";
	private static final String TABLE = "table";
	private static final String COLUMNS = "columns";
	private static final String ACTIONS = "actions";
	private static final String USE_ACTIONS = "useActions";
	private static final String GUI_FACTORY = "guiFactory";
	private static final String EDITOR = "editorName";
	private static final String PERSISTENT_SERVICE = "persistentService";
	private static final String FILTER = "filter";
	private static final String FILTER_VIEW = "filterView";
	private static final String TABLE_SERVICE = "tableService";
	private static final String NAME = "name";
	private static final String SHOW_MENU = "showMenu";
	private static final String MESSAGE_SOURCE = "messageSource";

	
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
		String tableModelBeanName = name + LIST_TABLE_MODEL_SUFFIX;
		String tablePanelBeanName = name + TABLE_PANEL_SUFFIX;
		String pageableTableBeanName = name + PAGEABLE_TABLE_SUFFIX;
		String dataSource = name + SERVICE_SUFFIX;
		String paginator = PAGINATOR_VIEW;
		String editor = name + EDITOR_SUFFIX;
		String actions = DefaultsBeanDefinitionParser.DEFAULT_TABLE_ACTIONS;
		String guiFactory = DefaultsBeanDefinitionParser.DEFAULT_GUI_FACTORY;
		
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
		
		
		// create ListTableModel
		BeanDefinitionBuilder bdb = BeanDefinitionBuilder.genericBeanDefinition(ListTableModel.class);
		bdb.setScope(BeanDefinition.SCOPE_PROTOTYPE);
		bdb.addPropertyValue("modelClass", entity);
		NodeList nl = element.getElementsByTagNameNS(element.getNamespaceURI(), COLUMNS);
	
		if (nl.getLength() > 0) {
			List columns = parserContext.getDelegate().parseListElement(
					(Element) nl.item(0), bdb.getRawBeanDefinition());
			bdb.addPropertyValue(COLUMNS, columns);
		}
		registerBeanDefinition(element, parserContext, tableModelBeanName, bdb);
		
		// create PageableTable
		bdb = BeanDefinitionBuilder.genericBeanDefinition(PageableTable.class);
		bdb.setScope(BeanDefinition.SCOPE_PROTOTYPE);
		bdb.addPropertyReference(DATA_SOURCE, dataSource);
		bdb.addPropertyReference(PAGINATOR_VIEW, paginator);
		bdb.addPropertyReference(TABLE_MODEL, tableModelBeanName);
		bdb.addPropertyValue(NAME, pageableTableBeanName);
		
		if (element.hasAttribute(TABLE_SERVICE)) 
			bdb.addPropertyReference(TABLE_SERVICE, element.getAttribute(TABLE_SERVICE));
		
		if (element.hasAttribute(FILTER))
			bdb.addPropertyReference(FILTER, element.getAttribute(FILTER));
		
		if (element.hasAttribute(SHOW_MENU))
			bdb.addPropertyValue(SHOW_MENU, element.getAttribute(SHOW_MENU));
		
		if (element.hasAttribute(MESSAGE_SOURCE))
			bdb.addPropertyReference(MESSAGE_SOURCE, element.getAttribute(MESSAGE_SOURCE));
			
		registerBeanDefinition(element, parserContext, pageableTableBeanName, bdb);
		
		// create TablePanel
		String tablePanelClassName = "info.joseluismartin.gui.table.TablePanel";
		if (element.hasAttribute(TABLE_PANEL_CLASS))
			tablePanelClassName = element.getAttribute(TABLE_PANEL_CLASS);
		
		bdb = BeanDefinitionBuilder.genericBeanDefinition(tablePanelClassName);
		bdb.setScope(BeanDefinition.SCOPE_PROTOTYPE);
		bdb.addPropertyReference(TABLE, pageableTableBeanName);
		bdb.addPropertyReference(GUI_FACTORY, guiFactory);
		bdb.addPropertyValue(EDITOR, editor);
		bdb.addPropertyReference(PERSISTENT_SERVICE, dataSource);
		
		if (element.hasAttribute(FILTER_VIEW))
			bdb.addPropertyReference(FILTER_VIEW, element.getAttribute(FILTER_VIEW));
		
		if (!element.hasAttribute(USE_ACTIONS) || "true".equals(element.getAttribute(USE_ACTIONS)))
			bdb.addPropertyReference(ACTIONS, actions);
			
		registerBeanDefinition(element, parserContext, tablePanelBeanName, bdb);
		
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
