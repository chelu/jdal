/*
 * Copyright 2009-2011 the original author or authors.
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
package info.joseluismartin.vaadin;

import info.joseluismartin.beans.AppCtx;
import info.joseluismartin.vaadin.ui.Box;

import org.springframework.context.ApplicationContext;

import com.vaadin.Application;
import com.vaadin.ui.Component;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class TestApp extends Application {
	
	ApplicationContext context = AppCtx.getInstance();
	@Override
	public void init() {
		Window mainWindow = new Window("Application");
		Component pageableTable = (Component) context.getBean("userPageableTable");
		Component dataSourceTable = (Component) context.getBean("dataSourceTable");	
		pageableTable.setWidth("100%");
		dataSourceTable.setWidth("100%");
		Panel panel = new Panel("Table with external paginator");
		panel.addComponent(pageableTable);
		Panel otherPanel = new Panel("Table with paginator in datasource");
		otherPanel.addComponent(dataSourceTable);
		VerticalLayout layout = new VerticalLayout();
		layout.addComponent(panel);
		Box.addVerticalStruct(layout, 10);
		layout.addComponent(otherPanel);
		mainWindow.setContent(layout);
		setMainWindow(mainWindow);
	}
}
