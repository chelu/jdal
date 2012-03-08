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
package info.joseluismartin.vaadin.ui.table;

import info.joseluismartin.service.PersistentService;
import info.joseluismartin.vaadin.ui.View;

import org.springframework.context.ApplicationContext;

import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class AdminPanel extends CustomComponent {
	
	private static final long serialVersionUID = 1L;
	private PageableTable table;
	private Component buttonPanel;
	private View<Object> editor;
	private String editorName;
	private ApplicationContext applicationContext;
	
	// Services
	private PersistentService service;
	
	protected Component buildPanel() {
		Layout vbox = new VerticalLayout();
		vbox.addComponent(buttonPanel);
		vbox.addComponent(table);
		
		return vbox;
	}
	
	public View<Object> getEditor(Object model) {
		View view = (View) applicationContext.getBean(editorName);
		view.setModel(model);
		
		return view;
	}
	
	
	
	/**
	 * @return the editorName
	 */
	public String getEditorName() {
		return editorName;
	}


	/**
	 * @param editorName the editorName to set
	 */
	public void setEditorName(String editorName) {
		this.editorName = editorName;
	}

	/**
	 * @return the table
	 */
	public PageableTable getTable() {
		return table;
	}

	/**
	 * @param table the table to set
	 */
	public void setTable(PageableTable table) {
		this.table = table;
	}

	/**
	 * @return the buttonPanel
	 */
	public Component getButtonPanel() {
		return buttonPanel;
	}

	/**
	 * @param buttonPanel the buttonPanel to set
	 */
	public void setButtonPanel(Component buttonPanel) {
		this.buttonPanel = buttonPanel;
	}

	/**
	 * @return the editor
	 */
	public View<Object> getEditor() {
		return editor;
	}

	/**
	 * @param editor the editor to set
	 */
	public void setEditor(View<Object> editor) {
		this.editor = editor;
	}
	
	/**
	 * @return the service
	 */
	public PersistentService getService() {
		return service;
	}

	/**
	 * @param service the service to set
	 */
	public void setService(PersistentService service) {
		this.service = service;
	}

	/**
	 * @return the context
	 */
	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	/**
	 * @param context the context to set
	 */
	public void setApplicationContext(ApplicationContext context) {
		this.applicationContext = context;
	}
}
