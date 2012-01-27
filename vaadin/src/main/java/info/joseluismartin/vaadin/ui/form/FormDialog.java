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
package info.joseluismartin.vaadin.ui.form;

import info.joseluismartin.service.PersistentService;
import info.joseluismartin.vaadin.ui.FormUtils;
import info.joseluismartin.vaadin.ui.table.ButtonListener;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;

import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;

/**
 * Window that hold a form for use as Dialog
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 * @since 1.1
 */
public class FormDialog extends Window {

	@Autowired
	private PersistentService<Object, Serializable> persistentService;
	private Form form;
	private ButtonListener acceptButtonListener;
	private ButtonListener cancelButtonListener;
	@Autowired
	private MessageSource messageSource = new ResourceBundleMessageSource();
	
	public FormDialog() {
		super();
	}
	
	public FormDialog(Form form) {
		this.form  = form;
	}
	
	public FormDialog(Form form, String caption) {
		super(caption);
		this.form = form;
	}

	/**
	 * @param caption
	 * @param content
	 */
	public FormDialog(String caption, ComponentContainer content) {
		super(caption, content);
	}

	/**
	 * @param caption
	 */
	public FormDialog(String caption) {
		super(caption);
	}
	
	public void init() {
		acceptButtonListener = new AcceptButtonListener();
		cancelButtonListener = new CancelButtonListener();
		
		HorizontalLayout buttonLayout = new HorizontalLayout();
		Button acceptButton = FormUtils.newButton(acceptButtonListener);
		Button cancelButton = FormUtils.newButton(cancelButtonListener);
		buttonLayout.setSpacing(true);
		buttonLayout.addComponent(acceptButton);
		buttonLayout.addComponent(cancelButton);
		HorizontalLayout footer = new HorizontalLayout();
		footer.addComponent(buttonLayout);
		footer.setSizeFull();
		footer.setComponentAlignment(buttonLayout, Alignment.TOP_CENTER);
		form.setFooter(footer);
		form.setSizeFull();
		form.getLayout().setSizeFull();
		addComponent(form);
		getContent().setSizeUndefined();
		center();
	}

	
	/**
	 * @return the form
	 */
	public Form getForm() {
		return form;
	}

	/**
	 * @param form the form to set
	 */
	public void setForm(Form form) {
		this.form = form;
	}

	/**
	 * @return the persistentService
	 */
	public PersistentService<Object, Serializable> getPersistentService() {
		return persistentService;
	}

	/**
	 * @param persistentService the persistentService to set
	 */
	public void setPersistentService(PersistentService<Object, Serializable> persistentService) {
		this.persistentService = persistentService;
	}

	/**
	 * @return the acceptButtonListener
	 */
	public ButtonListener getAcceptButtonListener() {
		return acceptButtonListener;
	}

	/**
	 * @param acceptButtonListener the acceptButtonListener to set
	 */
	public void setAcceptButtonListener(ButtonListener acceptButtonListener) {
		this.acceptButtonListener = acceptButtonListener;
	}

	/**
	 * @return the cancelButtonListener
	 */
	public ButtonListener getCancelButtonListener() {
		return cancelButtonListener;
	}

	/**
	 * @param cancelButtonListener the cancelButtonListener to set
	 */
	public void setCancelButtonListener(ButtonListener cancelButtonListener) {
		this.cancelButtonListener = cancelButtonListener;
	}
	
	private class AcceptButtonListener extends ButtonListener {

		public AcceptButtonListener() {
			String caption = messageSource == null ? "Accept" : messageSource.getMessage("accept", null, null);
			setCaption(caption);
		}
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void buttonClick(ClickEvent event) {
			form.commit();
			BeanItem<?> item = (BeanItem<?>) form.getItemDataSource();
			persistentService.save(item.getBean());
			close();
		}
		
	}
	
	private class CancelButtonListener extends ButtonListener {

		public CancelButtonListener() {
			String caption = messageSource == null ? "Cancel" : messageSource.getMessage("cancel", null, null);
			setCaption(caption);
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void buttonClick(ClickEvent event) {
			form.discard();
			close();
		}
		
	}

	/**
	 * @return the messageSource
	 */
	public MessageSource getMessageSource() {
		return messageSource;
	}

	/**
	 * @param messageSource the messageSource to set
	 */
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
}
