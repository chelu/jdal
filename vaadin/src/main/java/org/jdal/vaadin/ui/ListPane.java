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
package org.jdal.vaadin.ui;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.event.MouseEvents.ClickEvent;
import com.vaadin.event.MouseEvents.ClickListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * ListPane
 * 
 * @author Jose Luis Martin
 * @since 1.0
 */
public class ListPane extends CustomComponent implements ClickListener {

	private List<ComponentHolder> components = new ArrayList<ComponentHolder>();
	private HorizontalLayout main = new HorizontalLayout();
	private VerticalLayout menu = new VerticalLayout();
	private Panel selected = new Panel();
	private MenuItem itemSelected;
	
	
	public void init() {
		
		menu.setWidth("150px");
		
		for (ComponentHolder holder : components) {
			MenuItem e = new MenuItem(holder);
			e.addClickListener(this);
			menu.addComponent(e);
			if (itemSelected == null)
				select(e);
		}
		
		main.addComponent(menu);
		selected.setSizeFull();
		main.addComponent(selected);
		main.setExpandRatio(selected, 1.0f);
		main.setSizeFull();
		main.setSpacing(true);
		this.setCompositionRoot(main);
	}

	/**
	 * @return the components
	 */
	public List<ComponentHolder> getComponents() {
		return components;
	}

	/**
	 * @param components the components to set
	 */
	public void setComponents(List<ComponentHolder> components) {
		this.components = components;
	}

	/**
	 * {@inheritDoc}
	 */
	public void click(ClickEvent event) {
		select((MenuItem) event.getSource());
	}

	private void select(MenuItem item) {
		if (item.equals(itemSelected))
			return; // nothing to do
		
		selected.setCaption(item.getCaption());
		Component c = item.getComponent();
		selected.setContent(c);
		if (c instanceof ListPaneAware) {
			((ListPaneAware) c).show();
		}
		item.setStyleName("menuItem-selected");
	
		if (itemSelected != null)
			itemSelected.setStyleName("menuItem");
		
		itemSelected = item;
	}
	
	public interface ListPaneAware {
		void show();
		void hide();
	}
}

class MenuItem extends Panel {

	public MenuItem(ComponentHolder holder) {
		HorizontalLayout layout = new HorizontalLayout();
		layout.setSpacing(true);
		setContent(layout);
		Embedded e = new Embedded("", holder.getIcon());
		layout.addComponent(e);
		Label l = new Label(holder.getName());
		l.setStyleName(Reindeer.LABEL_H2);
		layout.addComponent(l);
		layout.setComponentAlignment(l, Alignment.BOTTOM_LEFT);
		layout.setComponentAlignment(e, Alignment.BOTTOM_LEFT);
		setStyleName("menuItem");
		this.component = holder.getComponent();
	}


	private Component component;

	/**
	 * @return the component
	 */
	public Component getComponent() {
		return component;
	}

	/**
	 * @param component the component to set
	 */
	public void setComponent(Component component) {
		this.component = component;
	}

}

