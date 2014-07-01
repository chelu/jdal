/*
 * Copyright 2009-2014 the original author or authors.
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
package org.jdal.vaadin.ui.table;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;

import org.jdal.vaadin.data.ContainerPersistentServiceAdapter;
import org.jdal.vaadin.ui.FormUtils;
import org.jdal.vaadin.ui.VaadinView;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

/**
 * @author Jose Luis Martin
 *
 */
public class TableEditor<T> extends TableComponent<T> {
	
	private HorizontalLayout buttonBar = new HorizontalLayout();
	
	public TableEditor() {
		super();
	}

	public TableEditor(Class<T> entityClass) {
		super(entityClass);
	}
	
	@PostConstruct
	public void init() {
		BeanItemContainer<T> c = new BeanItemContainer<T>(getEntityClass());
		setContainer(c);
		getTable().setContainerDataSource(c);
		getTable().addItemClickListener(this);
		VerticalLayout vl = getVerticalLayout();
		vl.setSpacing(true);
		vl.addComponent(this.buttonBar);
		vl.addComponent(getTable());
		vl.setExpandRatio(getTable(), 1);
		this.buttonBar.setSpacing(true);
	}

	@Override
	public VaadinView<T> getEditorView() {
		VaadinView<T> view = super.getEditorView();
		if (view != null) 
			view.setPersistentService(
					new ContainerPersistentServiceAdapter<T, Serializable>(getContainer()));
		
		return view;
	}

	@Override
	public void setActions(List<TableButtonListener> actions) {
		super.setActions(actions);
		updateButtonBar();
	}

	private void updateButtonBar() {
		this.buttonBar.removeAllComponents();
		
		for (TableButtonListener a : getActions()) {
			a.setTable(this);
			buttonBar.addComponent(FormUtils.newButton(a, isNativeButtons()));
		}
	}			
		

}
