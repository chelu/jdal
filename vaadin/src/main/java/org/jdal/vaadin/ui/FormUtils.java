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


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.jdal.beans.StaticMessageSource;
import org.jdal.cmd.Command;
import org.jdal.util.comparator.PropertyComparator;
import org.jdal.vaadin.ui.table.ButtonListener;
import org.springframework.context.i18n.LocaleContextHolder;

import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.Select;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * Form Utility Library
 * 
 * @author Jose Luis Martin
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
		cancel.addClickListener(new ClickListener() {
			
			public void buttonClick(ClickEvent event) {
				f.discard();
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
		ok.addClickListener(new ClickListener() {
			
			public void buttonClick(ClickEvent event) {
				f.commit();
			}
		});
		
		return ok;
	}
	
	/**
	 * Closes a sub Window
	 * @param window Window to close
	 */
	public static void closeWindow(Window window) {
			window.getUI().removeWindow(window);
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
	public static void showConfirmDialog(UI ui,  final Command command, String msg) {
	
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
		cancel.addClickListener(new ClickListener() {
			
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
		
		ui.addWindow(dlg);
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
	
	public static ComboBox newComboBox(List<?> elements, String sortProperty, String caption) {
		Collections.sort(elements, new PropertyComparator(sortProperty));
		ComboBox combo = new ComboBox(caption);
		addItemList(combo, elements);
		
		return combo;
	}

	/**
	 * Create a new {@link TextField}
	 * @return new TextField with null representation set to empty string
	 */
	public static TextField newTextField() {
		TextField tf = new TextField();
		tf.setNullRepresentation("");
		
		return tf;
	}
	
	/**
	 * Create a new {@link PasswordField}
	 * @return new PasswordField with null representation set to emptry string.
	 */
	public static PasswordField newPasswordField() {
		PasswordField pf = new PasswordField();
		pf.setNullRepresentation("");
		
		return pf;
	}

	/**
	 * Create a new {@link NativeButton} from an {@link ButtonListener}
	 * @param action button listener
	 * @return a new native button.
	 */
	public static NativeButton newNativeButton(ButtonListener action) {
		NativeButton b = new NativeButton(action.getCaption(), action);
		b.setIcon(action.getIcon());
		b.setDescription(action.getDescription());
		
		return b;
	}
	
	/**
	 * Create a new DateField with format for current locale and {@link DateFormat#SHORT} style
	 * @return a new DateField
	 */
	public static DateField newDateField() {
		return newDateField(DateFormat.SHORT);
	}

	/**
	 * Create a new DateField with format for current locale and given style.
	 * @param style DateFormat style
	 * @return a new DateField
	 */
	public static DateField newDateField(int style) {
		DateField df = new DateField();
		Locale locale = LocaleContextHolder.getLocale();
		df.setLocale(locale);
		DateFormat dateFormat = DateFormat.getDateInstance(style);
		
		if (dateFormat instanceof SimpleDateFormat) {
			SimpleDateFormat sdf = (SimpleDateFormat) dateFormat;
			df.setDateFormat(sdf.toPattern());
		}
		
		return df;
	}

}
