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
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdal.beans.StaticMessageSource;
import org.jdal.cmd.Command;
import org.jdal.util.comparator.PropertyComparator;
import org.jdal.vaadin.ui.table.ButtonListener;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.context.i18n.LocaleContextHolder;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.ClassResource;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Resource;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.TwinColSelect;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;

/**
 * Form Utility Library
 * 
 * @author Jose Luis Martin
 */
public abstract class FormUtils {
	
	public static int OK = 0;
	public static int CANCEL = 1;
	public static final String THEME_PREFIX = "theme:";
	public static final String CLASSPATH_PREFIX = "classpath:";
	public static final String URL_PREFIX = "url:";
	
	private static final Log log = LogFactory.getLog(FormUtils.class);
	
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
	 * Build a new Button from Button Listener
	 * @param buttonListener
	 * @param native true for native buttons.
	 * @return a new Button
	 */
	public static Button newButton(ButtonListener buttonListener, boolean isNative) {
		if (isNative)
			return newNativeButton(buttonListener);
		
		return newButton(buttonListener);
		
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
		ok.addClickListener(new ClickListener() {
			
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
		combo.setItemCaptionMode(ItemCaptionMode.ID);
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
	
	public static void showDialog(Component component, String caption, String width, String height) {
		Dialog dlg = new Dialog(component);
		dlg.setWidth(width);
		dlg.setHeight(height);
		dlg.center();
		dlg.setCaption(caption);
		dlg.show();
		
	}
	
	public static void showDialog(Component component, String caption) {
		showDialog(component, caption, "80%", "80%");
	}

	/**
	 * Fill combo with a list of objects removing all items.
	 * 
	 * @param data list to fill with.
	 * @param clear true if clear all items before adding new ones.
	 */
	public static void fillCombo(ComboBox combo, List<?> data) {
		fillCombo(combo, data, true);
	}
	
	/**
	 * Fill combo with a list of objeces.
	 * @param data list to fill with.
	 * @param clear true if clear all items before adding new ones.
	 */
	public static void fillCombo(ComboBox combo, List<?> data, boolean clear) {
		Object selected = combo.getValue();
		
		if (clear) {
			combo.removeAllItems();
		}
		
		for (Object o : data) {
			combo.addItem(o);
		}
		
		if (data.contains(selected))
			combo.setValue(selected);
	}
	
	/**
	 * Gets resource from url using the following prefix
	 * <ul>
	 * <li> classpath: for searching the classpath.</li>
	 * <li> theme: for searching in current theme </li>
	 * <li> url: to use a url </li>
	 * </ul>
	 * @param url resource url
	 * @return the resource.
	 */
	public static Resource getResource(String url) {
		Resource resource;
		
		if (url.startsWith(CLASSPATH_PREFIX)) 
			resource = new ClassResource(StringUtils.substringAfter(url, CLASSPATH_PREFIX));
		else if (url.startsWith(THEME_PREFIX))
			resource = new ThemeResource(StringUtils.substringAfter(url, THEME_PREFIX));
		else if (url.startsWith(URL_PREFIX))
			resource = new ExternalResource(StringUtils.substringAfter(url, URL_PREFIX));
		else if (url.contains(":"))
			resource = new ExternalResource(url);
		else 
			resource = new ThemeResource(url);
		
		return resource;
	}
	
	/** 
	 * Create a new TextArea with current locale from {@link LocaleContextHolder}
	 * @return a new TextArea
	 */
	public static TextArea newTextArea() {
		TextArea area = new TextArea();
		area.setNullRepresentation("");
		area.setLocale(LocaleContextHolder.getLocale());
		
		return area;
	}
	
	 /**
     * Add a link on primary and dependent ComboBoxes by property name. 
     * When selection changes on primary use propertyName to get a Collection and fill dependent ComboBox with it
     * @param primary ComboBox when selection changes
     * @param dependent ComboBox that are filled with collection   
     * @param propertyName the property name for get the collection from primary selected item
     */
	public static void link(final ComboBox primary, final ComboBox dependent, 
			final String propertyName) {
		link(primary, dependent, propertyName, false);
	}

    /**
     * Add a link on primary and dependent ComboBoxes by property name. 
     * When selection changes on primary use propertyName to get a Collection and fill dependent ComboBox with it
     * @param primary ComboBox when selection changes
     * @param dependent ComboBox that are filled with collection   
     * @param propertyName the property name for get the collection from primary selected item
     * @param addNull if true, add a null as first combobox item
     */
    @SuppressWarnings("rawtypes")
    public static void link(final ComboBox primary, final ComboBox dependent,
    		final String propertyName, final boolean addNull) {

    	primary.addValueChangeListener(new ValueChangeListener() {

    		public void valueChange(ValueChangeEvent event) {
    			Object selected = event.getProperty().getValue();
    			if (selected != null) {
    				BeanWrapper wrapper = PropertyAccessorFactory.forBeanPropertyAccess(selected);
    				if (wrapper.isReadableProperty(propertyName)) {
    					Collection items = (Collection) wrapper.getPropertyValue(propertyName);
    					dependent.removeAllItems();
    					
    					if (addNull)
    						dependent.addItem(null);
    					
    					for (Object item : items)
    						dependent.addItem(item);
    				}
    				else {
    					log.error("Can't write on propety '" + propertyName + "' of class: '" + selected.getClass() + "'");
    				}
    			}

    		}
    	});
    }

    /**
     * Create a titled separator
     * @param title title
     * @return a {@link HorizontalLayout} with title and rule.
     */
    public static Component createTitledSeparator(String title) {
    	Label titleLabel = new Label(title);
    	titleLabel.setStyleName(Reindeer.LABEL_H2);
		Label rule = new Label("<hr />", ContentMode.HTML);
		titleLabel.setSizeUndefined();
		HorizontalLayout hl = new HorizontalLayout();
		hl.addComponent(titleLabel);
		hl.addComponent(rule);
		hl.setComponentAlignment(rule, Alignment.BOTTOM_CENTER);
		hl.setExpandRatio(rule, 1);
		hl.setWidth(100, Unit.PERCENTAGE);
		
		return hl;
    }
    
    /**
     * Crate a {@link TwinColSelect} 
     */
    public static TwinColSelect newTwinColSelect() {
    	TwinColSelect select = new TwinColSelect();
    	select.setLocale(LocaleContextHolder.getLocale());
    	
    	return select;
    }
}
