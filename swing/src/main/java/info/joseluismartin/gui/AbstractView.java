/*
 * Copyright 2002-2010 original author or authors.
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
package info.joseluismartin.gui;

import info.joseluismartin.gui.bind.BinderFactory;
import info.joseluismartin.gui.bind.CompositeBinder;
import info.joseluismartin.gui.validation.ErrorProcessor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;

/**
 * Default Abstract View with Composite support for refresh and update.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public abstract class AbstractView<T> implements View<T> {
	
	public final static String DEFAULT_BINDER_FACTORY_NAME = "binderFactory";
	/** log */
	private static final Log log = LogFactory.getLog(AbstractView.class);
	/** view name */
	private String name;
	/** binder factory to make property binders */
	private BinderFactory binderFactory;
	/** hold binders */
	private CompositeBinder<T> binder = new CompositeBinder<T>();
	/** data model */
	private T model;
	/** JComponent that hold controls */
	private JComponent panel;
	/** subviews list */
	private List<View<T>> subViews = new ArrayList<View<T>>();
	/** validator to check binding and model values */
	private Validator validator;
	/** message source for internationalization */
	private MessageSource messageSource;
	/** List of error handlers */
	private List<ErrorProcessor> errorProcessors = new ArrayList<ErrorProcessor>();

	protected int width = 0;
	protected int height = 0;
	
	/**
	 * Default ctor
	 */
	public AbstractView() {
		
	}
	
	/**
	 * Create the view and set the model
	 * @param model model to set
	 */
	public AbstractView(T model) {
		setModel(model);
	}
	
	/**
	 * add a binding for control and model property name
	 * @param comoponent control
	 * @param propertyName the model property path to bind
	 * @param readOnly if true, binding only do refresh()
	 */
	public void bind(Object component, String propertyName, boolean readOnly) {
		binder.bind(component, propertyName, readOnly);
		
	}
	
	/**
	 * add a binding for control and model property name
	 * @param comoponent control
	 * @param propertyName the model property path to bind
	 */
	public void bind(Object component, String propertyName) {
		bind(component, propertyName, false);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public JComponent getPanel() {
		if (panel == null) {
			panel = buildPanel();
			if (width != 0 && height != 0)
				panel.setSize(width, height);
		}
		return panel;
	}
	
	/**
	 * Build the JComponent that hold controls.
	 * @return a JCompoent
	 */
	protected abstract JComponent buildPanel();

	
	/**
	 * {@inheritDoc}
	 */
	public T getModel() {
		return model;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public final void setModel(T model) {
		this.model = model;
		binder.setModel(model);
		
		// refresh subviews
		for (View<T> v : subViews)
			v.setModel(model);
		
		onSetModel(model);
	}
	
	/**
	 * Callback method to handle model changes
	 * @param model the new model
	 */
	protected void onSetModel(T model) {
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	public final void update() {
		// do custom update
		doUpdate();
		
		binder.update();
		
		// update subviews
		for (View<T>  v : subViews) {
			v.update();
		}
	}

	/**
	 * Callback method on update()
	 */
	protected void doUpdate() {
		
	}
	
	/**
	 * Add a subview, the subview is refreshed, updated and hold the same model
	 * that this view, for adding views with other models, use bind()
	 * @param view
	 */
	public void addView(View<T> view) {
		subViews.add(view);
		view.setModel(model);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public final void refresh() {
		doRefresh();
		
		binder.refresh();
		
		// refresh subviews
		for (View<T> v : subViews)
			v.refresh();
	}
	
	/**
	 * Callback method for refresh()
	 */
	protected void doRefresh() {
		
	}

	/**
	 * Gets the binder factory
	 * @return the binder factory
	 */
	public BinderFactory getBinderFactory() {
		return binderFactory;
	}

	/**
	 * Sets the binder factory, propagate it to composite binder.
	 * @param binderFactory to set
	 */
	public void setBinderFactory(BinderFactory binderFactory) {
		this.binderFactory = binderFactory;
		binder.setBinderFactory(binderFactory);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean validateView() {
		if (validator == null)
			return true;
		
		resetErrorProcessors();
		Errors errors = new BeanPropertyBindingResult(getModel(), "");
		validator.validate(getModel(), errors);
		
		if (errors.hasErrors()) {
			String errorMessage = getErrorMessage(errors);
			JOptionPane.showMessageDialog(getPanel(),errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
			for (FieldError error : errors.getFieldErrors()) {
				for (ErrorProcessor ep : errorProcessors ) 
					ep.processError(binder.getBinder(error.getField()), error);
			}
			return false;
		}
		
		return true;
	}
	
	private void resetErrorProcessors() {
		for (ErrorProcessor ep : errorProcessors) {
			ep.reset();
		}
		
	}

	/**
	 * Build a error message with all errors.
	 * @param errors erros to use 
	 * @return String with error message
	 */
	@SuppressWarnings("unchecked")
	protected String getErrorMessage(Errors errors) {
		StringBuilder sb = new StringBuilder();
		if (errors.hasErrors()) {
			sb.append("\n");
			Iterator iter = errors.getAllErrors().iterator();
			while (iter.hasNext()) {
				ObjectError oe = (ObjectError) iter.next();
				sb.append("- ");
				sb.append(messageSource.getMessage(oe, null));
				sb.append("\n");
			}
		}
		sb.append("\n");
		return sb.toString();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public void clear() {
		T model = getModel();
		if (model != null) {
			try {
				setModel((T) model.getClass().newInstance());
				refresh();
			} catch (InstantiationException e) {
				log.error(e);
			} catch (IllegalAccessException e) {
				log.error(e);
			}
		}
	}

	/**
	 * @return the validator
	 */
	public Validator getValidator() {
		return validator;
	}

	/**
	 * @param validator the validator to set
	 */
	public void setValidator(Validator validator) {
		this.validator = validator;
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

	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @param width the width to set
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @param height the height to set
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the errorProcessors
	 */
	public List<ErrorProcessor> getErrorProcessors() {
		return errorProcessors;
	}

	/**
	 * @param errorProcessors the errorProcessors to set
	 */
	public void setErrorProcessors(List<ErrorProcessor> errorProcessors) {
		this.errorProcessors = errorProcessors;
	}
}
