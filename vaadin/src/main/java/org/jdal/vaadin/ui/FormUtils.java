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


import java.util.List;

import org.jdal.beans.StaticMessageSource;
import org.jdal.cmd.Command;
import org.jdal.vaadin.ui.table.ButtonListener;

import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Select;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * Form Utility Library
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public abstract class FormUtils {
	
	public static int OK = 0;
	public static int CANCEL = 1;

	/**
	 * Add a default commit/discard buttons to a form
	 * @param f the Form
	 */
	public static void addOKCancelButtons(Form f) {
		Button ok = newOKButton(f);
		Button cancel = newCancelButton(f);
		HorizontalLayout hl = new HorizontalLayout();
		hl.setSpacing(true);
		hl.addComponent(ok);
		hl.addComponent(cancel);
		HorizontalLayout  footer = new HorizontalLayout();
		footer.setWidth("100%");
		footer.addComponent(hl);
		footer.setComponentAlignment(hl, Alignment.TOP_CENTER);
		f.setFooter(footer);
	}

	/**
	 * Creates a new cancel button
	 * @param f form holding the button
	 * @return new cancel button
	 */
	private static Button newCancelButton(final Form f) {
		Button cancel = new Button(StaticMessageSource.getMessage("cancel"));
		cancel.addListener(new ClickListener() {
			
			public void buttonClick(ClickEvent event) {
				f.discard();
				// what? where is window.close() ?
				f.getWindow().getParent().removeWindow(f.getWindow());
			}
		});
		
		return cancel;
	}

	/**
	 * Creates a new OK Button
	 * @param f form holding the button
	 * @return new OK Button
	 */
	private static Button newOKButton(final Form f) {
		Button ok = new Button(StaticMessageSource.getMessage("ok"));
		ok.addListener(new ClickListener() {
			
			public void buttonClick(ClickEvent event) {
				f.commit();
				// what? where is window.close() ?
				f.getWindow().getParent().removeWindow(f.getWindow());
			}
		});
		
		return ok;
	}
	
	/**
	 * Closes a sub Window
	 * @param window Window to close
	 */
	public static void closeWindow(Window window) {
		if (window.getParent() != null) 
			window.getParent().removeWindow(window);
	}

	/**
	 * Build a new Button from Button Listener
	 * @param buttonListener
	 * @return a new Button
	 */
	public static Button newButton(ButtonListener buttonListener) {
		Button b = new Button(buttonListener.getCaption(), buttonListener);
		b.setIcon(buttonListener.getIcon());
		b.setDescription(buttonListener.getDescription());
		return b;
	}

	/**
	 * Show a YES/NO confirm dialog
	 * @param window Window to attach the dialog
	 * @param msg the msg
	 */
	public static void showConfirmDialog(Window window,  final Command command, String msg) {
	
		final Window dlg = new Window("Please Confirm");
		VerticalLayout vl = new VerticalLayout();
		vl.setSizeFull();
		vl.setSpacing(true);
		vl.setMargin(true);
		Label label  = new Label(msg, Label.CONTENT_XHTML);
		vl.addComponent(label);
		HorizontalLayout hl = new HorizontalLayout();
		hl.setSpacing(true);
		
		Button ok = new Button(StaticMessageSource.getMessage("yes"));
		ok.addListener(new ClickListener() {
			
			public void buttonClick(ClickEvent event) {
				command.execute(null);
				closeWindow(dlg);
				
			}
		});
		Button cancel = new Button(StaticMessageSource.getMessage("no"));
		cancel.addListener(new ClickListener() {
			
			public void buttonClick(ClickEvent event) {
				closeWindow(dlg);
			}
		});
		
		hl.setSpacing(true);
		hl.addComponent(ok);
		hl.addComponent(cancel);
		hl.setSizeFull();
		vl.addComponent(hl);
		vl.setComponentAlignment(hl, Alignment.TOP_CENTER);
	
		dlg.setContent(vl);
		dlg.setModal(true);
		vl.setSizeUndefined();
		window.center();
		window.addWindow(dlg);
	}
	
	/**
	 * Add a List of objects to a combo
	 * @param combo combo to add items
	 * @param items items to add
	 */
	public static void addItemList(AbstractSelect combo, List<?> items) {
		combo.setItemCaptionMode(Select.ITEM_CAPTION_MODE_ID);
		for (Object item : items)
			combo.addItem(item);
	}
	
	
}
