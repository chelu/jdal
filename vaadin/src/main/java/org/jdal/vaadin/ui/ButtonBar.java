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
package org.jdal.vaadin.ui;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.jdal.vaadin.ui.table.ButtonListener;

import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

/**
 * A Component with button menu.
 * 
 * @author Jose Luis Martin
 * @since 2.0
 */
public class ButtonBar extends CustomComponent implements ClickListener {
	
	public static final int VERTICAL = 0;
	public static final int HORIZONTAL = 1;
	private int layoutType = HORIZONTAL;
	private AbstractOrderedLayout menu;
	private Component main;
	private List<ButtonListener> actions = new LinkedList<ButtonListener>();
	private List<Button> buttons = new LinkedList<Button>();
	private boolean useNativeButtons = true;
	
	public ButtonBar() {
		addStyleName("jd-button-bar");
	}
	
	@PostConstruct
	public void init() {
		if (layoutType == HORIZONTAL) {
			this.menu = new HorizontalLayout();
		}
		else { 
			this.menu = new VerticalLayout();
		}

		for (ButtonListener action : actions)
			addAction(action);

		setCompositionRoot(this.menu);

	}

	/**
	 * Add a new {@link Button} to bar from a {@link ButtonListener}
	 * @param action
	 */
	protected void addAction(ButtonListener action) {
		Button button = createButton(action);
		button.addClickListener(this);
		this.menu.addComponent(button);
		this.buttons.add(button);
	}

	/**
	 * Create a {@link Button} from a {@link ButtonListener}
	 * @param action button listener to use
	 * @return a new button.
	 */
	private Button createButton(ButtonListener action) {
		Button button =  this.useNativeButtons ?  FormUtils.newNativeButton(action) : FormUtils.newButton(action);
		button.addStyleName(createButtonStyleName(action.getCaption()));
		
		return button;
	}

	/**
	 * Create a css style name from caption.
	 * @param caption the caption
	 * @return the button style name.
	 */
	protected String createButtonStyleName(String caption) {
		if (caption != null)
			return "button-" + caption.replace(' ', '-');
		
		return null;
	}

	@Override
	public void buttonClick(ClickEvent event) {
		Button selected = event.getButton();
		selected.addStyleName("selected");
		
		for (Button b : buttons) {
			if (!b.equals(event.getButton()))
				b.removeStyleName("selected");
		}
	}
	
	/**
	 * Simulates a button click on first button with provided caption
	 * @param caption caption to search.
	 */
	public void click(String caption) {
		if (caption == null && buttons.size() > 0) {
			Button button = buttons.get(0);
			button.click();
			return;
		}
		
		Button button = getButtonByCaption(caption);
		if (button != null)
			button.click();
	}

	/**
	 * Find a button by caption
	 * @param caption caption to search
	 * @return the button, null if none
	 */
	private Button getButtonByCaption(String caption) {
		for (Button b : buttons) {
			if (caption.equals(b.getCaption()))
				return b;
		}
		
		return null;
	}

	public int getLayoutType() {
		return layoutType;
	}

	public void setLayoutType(int layoutType) {
		this.layoutType = layoutType;
	}

	public Component getMain() {
		return main;
	}

	public void setMain(Component main) {
		this.main = main;
	}

	public List<ButtonListener> getActions() {
		return actions;
	}

	public void setActions(List<ButtonListener> actions) {
		this.actions = actions;
	}

	public boolean isUseNativeButtons() {
		return useNativeButtons;
	}

	public void setUseNativeButtons(boolean useNativeButtons) {
		this.useNativeButtons = useNativeButtons;
	}
	
}
