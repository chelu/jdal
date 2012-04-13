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
import info.joseluismartin.gui.table.TablePanel;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.parsing.BeanComponentDefinition;
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
	private static final String GUI_FACTORY = "guiFactory";
	private static final String EDITOR = "editorName";
	private static final String PERSISTENT_SERVICE = "persistentService";
	private static final String FILTER = "filter";
	private static final String FILTER_VIEW = "filterView";

	
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
		bdb.addPropertyReference(DATA_SOURCE, dataSource);
		bdb.addPropertyReference(PAGINATOR_VIEW, paginator);
		bdb.addPropertyReference(TABLE_MODEL, tableModelBeanName);
		registerBeanDefinition(element, parserContext, pageableTableBeanName, bdb);
		
		// create TablePanel
		bdb = BeanDefinitionBuilder.genericBeanDefinition(TablePanel.class);
		bdb.addPropertyReference(TABLE, pageableTableBeanName);
		bdb.addPropertyReference(ACTIONS, actions);
		bdb.addPropertyReference(GUI_FACTORY, guiFactory);
		bdb.addPropertyValue(EDITOR, editor);
		bdb.addPropertyReference(PERSISTENT_SERVICE, dataSource);
		
		if (element.hasAttribute(FILTER))
			bdb.addPropertyReference(FILTER_VIEW, element.getAttribute(FILTER));
		
		registerBeanDefinition(element, parserContext, tablePanelBeanName, bdb);
		
		
		return null;
	}

	/**
	 * Register BeanDefinition and apply default bean attributes.
	 * @param element
	 * @param parserContext
	 * @param tableModelBeanName
	 * @param bdb
	 */
	private void registerBeanDefinition(Element element, ParserContext parserContext, String tableModelBeanName,
			BeanDefinitionBuilder bdb) {
		AbstractBeanDefinition bd = bdb.getBeanDefinition();
		parserContext.getDelegate().parseBeanDefinitionAttributes(element, tableModelBeanName, null, bd);
		BeanComponentDefinition bcd = new BeanComponentDefinition(bdb.getBeanDefinition(), tableModelBeanName);
		parserContext.registerBeanComponent(bcd);
	}

}
