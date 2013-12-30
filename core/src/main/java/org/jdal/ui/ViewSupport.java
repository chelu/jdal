/*
 * Copyright 2009-2011 original author or authors.
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
package org.jdal.ui;

import java.awt.Component;
import java.beans.PropertyDescriptor;
import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdal.annotations.AnnotationUtils;
import org.jdal.ui.bind.BinderFactory;
import org.jdal.ui.bind.BinderHolder;
import org.jdal.ui.bind.CompositeBinder;
import org.jdal.ui.bind.ControlAccessor;
import org.jdal.ui.bind.ControlAccessorFactory;
import org.jdal.ui.bind.ControlChangeListener;
import org.jdal.ui.bind.ControlError;
import org.jdal.ui.bind.ControlEvent;
import org.jdal.ui.bind.ControlInitializer;
import org.jdal.ui.bind.DirectFieldAccessor;
import org.jdal.ui.bind.Property;
import org.jdal.ui.bind.PropertyBinder;
import org.jdal.ui.validation.ErrorProcessor;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessor;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;

/**
 * Template class that simplifies {@link View} implementation.
 * 
 * <p> The central method is <code>buildPanel</code> that builds the <code>JComponent</code>
 * that hold the view controls. You may use custom binding of the view overwriting the methods 
 * <code>doUpdate</code> and <code>doRefresh</code>.
 * 
 * <p> For common binding code, you usually use autobinding facitility, that is, using 
 * the same name to the field control and model property name and calling to autobind() method.
 * When using autobinding, you may exclude model properties from binding using some
 * of <code>ignoreProperty</code> methods.
 * 
 * <p> Manual binding is also supported via <code>bind</code> methods. When binding a control, a
 * the View is added to control as <code>ChangeListener</code> is added to the control for setting dirty property on control
 * changes.
 * 
 * <p> Only <code>org.springframework.util.validation.Validator</code> validators are supported
 * The <code>validateView</code> method calls configured
 * <code>ErrorProcessors</code> to process errors found in validation.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 * @since 2.0
 * @see BinderFactory
 * @see ControlAccessorFactory
 * @see ErrorProcessor
 */
public abstract class ViewSupport<T> implements View<T>, ControlChangeListener, BinderHolder {
	
	public final static String DEFAULT_BINDER_FACTORY_NAME = "binderFactory";
	/** log */
	private static final Log log = LogFactory.getLog(ViewSupport.class);
	/** view name */
	private String name;
	/** binder factory to make property binders */
	private BinderFactory binderFactory;
	/** control accessor factory */
	private ControlAccessorFactory controlAccessorFactory;
	/** hold binders */
	private CompositeBinder<T> binder = new CompositeBinder<T>();
	/** if true, do an automatic binding using property names */
	private boolean autobinding = false;
	/** Set with property names to ingnore on binding commands */
	private Set<String> ignoredProperties = new HashSet<String>();
	/** data model */
	private T model;
	/** subviews list */
	@SuppressWarnings("rawtypes")
	private List<View> subViews = new ArrayList<View>();
	/** validator to check binding and model values */
	private Validator validator;
	/** message source for internationalization */
	@Autowired
	protected MessageSource messageSource;
	/** List of error handlers */
	private List<ErrorProcessor> errorProcessors = new ArrayList<ErrorProcessor>();
	/** Validation Errors */
	protected BindingResult errors;
	/** dirty state */
	private boolean dirty = false;
	/** initialize controls on autobind **/
	private boolean initializeControls = true;
	
	protected int width = 0;
	protected int height = 0;
	/** control initializer */
	private ControlInitializer controlInitializer;
	private List<ControlChangeListener> listeners = new ArrayList<ControlChangeListener>();
	
	/**
	 * Default ctor
	 */
	public ViewSupport() {
		this(null);
	}
	
	/**
	 * Create the view and set the model
	 * @param model model to set
	 */
	public ViewSupport(T model) {
		setModel(model);
	}
	
	/**
	 * add a binding for control and model property name
	 * @param component control
	 * @param propertyName the model property path to bind
	 * @param readOnly if true, binding only do refresh()
	 */
	public void bind(Object component, String propertyName, boolean readOnly) {
		checkFactories();
		binder.bind(component, propertyName, readOnly);
		listen(component);
		
	}
	
	protected void checkFactories() {
		
	}

	/**
	 * add a binding for control and model property name
	 * @param component control
	 * @param propertyName the model property path to bind
	 */
	public void bind(Object component, String propertyName) {
		bind(component, propertyName, false);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public abstract Object getPanel(); 
	
	
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
		createBindingResult();
		binder.setModel(model);
		
		// refresh subviews
		for (View<Object> v : subViews)
			v.setModel(model);
		
		onSetModel(model);
	}
	
	/**
	 * 
	 */
	private void createBindingResult() {
		if (model != null)
			errors = new BeanPropertyBindingResult(getModel(), getModel().getClass().getSimpleName());
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
		clearErrors();
		// do custom update
		doUpdate();
		
		// update binder
		binder.update();
		
		if (errors != null && binder.getBindingResult() != null &&
				errors.getObjectName().equals(binder.getBindingResult().getObjectName()))
			errors.addAllErrors(binder.getBindingResult());
		
		// update subviews
		for (View<Object>  v : subViews) {
			v.update();
			if (errors != null && v.getBindingResult() != null &&
					errors.getObjectName().equals(v.getBindingResult().getObjectName()))
				errors.addAllErrors(v.getBindingResult());
		}
		
		// allow subclasses to do something after the update
		afterUpdate();
	}

	/**
	 * Clear validation erros
	 */
	private void clearErrors() {
		if (getModel() != null && errors != null && errors.hasErrors())
			createBindingResult();
		resetErrorProcessors();
		
	}

	/**
	 * Callback method on update()
	 */
	protected void doUpdate() {
		
	}
	
	/**
	 * Callback method on update() 
	 */
	
	protected void afterUpdate() {
		
	}
	
	/**
	 * Add a subview, the subview is refreshed, updated and hold the same model
	 * that this view, for adding views with other models, use bind()
	 * @param view
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void addView(View view) {
		subViews.add(view);
		view.setModel(model);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public final void refresh() {
		clearErrors();
		doRefresh();
		binder.refresh();

		// refresh subviews
		for (View<Object> v : subViews)
			v.refresh();
		
		setDirty(false);
	}
	
	/**
	 * Allow subclasses to do custom refresh
	 */
	protected void doRefresh() {
		
	}
	
	/**
	 * Allow subclasses to do something after refresh
	 */
	protected void afterRefresh() {
		
	}

	/**
	 * Listen control for changes.
	 */
	public void listen(Object control) {
		checkFactories();
		ControlAccessor c = controlAccessorFactory.getControlAccessor(control);
		if (c != null) {
			c.addControlChangeListener(this);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void controlChange(ControlEvent e) {
		setDirty(true);
		fireControlChange(e);
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
		if (validator == null && !errors.hasErrors())
			return true;
		
		if (validator != null) 
			validator.validate(getModel(), errors);
		
		if (errors.hasErrors()) {
			for (FieldError error : errors.getFieldErrors()) {
				for (ErrorProcessor ep : errorProcessors ) {
					if (error instanceof ControlError) {
						ControlError ce = (ControlError) error;
						ep.processError(ce.getComponent(), error);
					}
					else {
						Binder<?> b = binder.getBinder(error.getField());
						if (b instanceof PropertyBinder) {
							ep.processError(((PropertyBinder) b).getComponent(), error);
						}
					}
				}
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
	 * @return String with error message
	 */
	public String getErrorMessage() {
		StringBuilder sb = new StringBuilder();
		if (errors.hasErrors()) {
			sb.append("\n");
			Iterator<ObjectError> iter = errors.getAllErrors().iterator();
			while (iter.hasNext()) {
				ObjectError oe = (ObjectError) iter.next();
				sb.append("- ");
				sb.append(getMessage(oe));
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
	 * {@inheritDoc}
	 */
	public void enableView(boolean enabled) {
		for (Binder<?> b : binder.getPropertyBinders()) {
			Object control = ((PropertyBinder) b).getComponent();
			
			if (control instanceof Component) 
				((Component) control).setEnabled(enabled);
			else if (control instanceof View<?>) 
				((View<?>) control).enableView(enabled);
			
			for (View<?> v : subViews) 
				v.enableView(enabled);
		}
	}
	
	/**
	 * Bind controls following the same name convention or annotated with Property annotation.
	 */
	public void autobind() {
		BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(getModel());
		PropertyAccessor  viewPropertyAccessor = new DirectFieldAccessor(this);
		
		// Parse Property annotations
		List<AnnotatedElement> elements = AnnotationUtils.findAnnotatedElements(Property.class, getClass());

		for (AnnotatedElement ae : elements) {
			Property p = ae.getAnnotation(Property.class);
			bindAndInitializeControl(p.value(), ae);
			this.ignoredProperties.add(p.value());
		}
		
		// Iterate on model properties
		for (PropertyDescriptor pd : bw.getPropertyDescriptors()) {
			String propertyName = pd.getName();
			if ( !ignoredProperties.contains(propertyName) && viewPropertyAccessor.isReadableProperty(propertyName)) {
				Object control = viewPropertyAccessor.getPropertyValue(propertyName);

				if (control != null) {
					if (log.isDebugEnabled()) 
						log.debug("Found control: " + control.getClass().getSimpleName() + 
									" for property: " + propertyName);
					// do bind
					bindAndInitializeControl(propertyName, control);
				}
			}
		}
	}

	/**
	 * Bind and initialize a control with property name.
	 * @param propertyName property name
	 * @param control control
	 */
	private void bindAndInitializeControl(String propertyName, Object control) {
		bind(control, propertyName);
		// initialize control
		if (isInitializeControls() && controlInitializer != null)
				controlInitializer.initialize(control, propertyName, getModel().getClass());
	}
	
	/** 
	 * I18n Support
	 * @param code message code
	 * @return message or code if none defined
	 */
	protected String getMessage(String code) {
		try {
			return messageSource == null ?
				code : messageSource.getMessage(code, null, Locale.getDefault());
		} 
		catch (NoSuchMessageException nsme) {
			log.error(nsme);
		}
		
		return code;
	}
	
	/** 
	 * I18n Support
	 * @param msr message source resolvable
	 * @return message or code if none defined
	 */
	protected String getMessage(MessageSourceResolvable msr) {
		return messageSource == null ?
				msr.getDefaultMessage() : messageSource.getMessage(msr, Locale.getDefault());
	}
	
	
	public void addControlChangeListener(ControlChangeListener l) {
		if (!listeners.contains(l))
			listeners.add(l);
		
	}
	
	
	public void removeControlChangeListener(ControlChangeListener l) {
		if (!listeners.contains(l))
			listeners.remove(l);
		
	}
	
	/**
	 * Notifiy Listeners that control value has changed	
	 */
	protected void fireControlChange(ControlEvent e) {
		for (ControlChangeListener l : listeners)
			l.controlChange(new ControlEvent(e));
	}
	
	
	/**
	 * Add a property name  to ignore on binding.
	 * @param propertyName property name to ignore
	 */
	public void ignoreProperty(String propertyName) {
		ignoredProperties.add(propertyName);
	}

	/**
	 * @return the ignoredProperties
	 */
	public Set<String> getIgnoredProperties() {
		return ignoredProperties;
	}

	/**
	 * @param ignoredProperties the ignoredProperties to set
	 */
	public void setIgnoredProperties(Set<String> ignoredProperties) {
		this.ignoredProperties = ignoredProperties;
	}

	/**
	 * Add a Collection of property names to ignore on binding
	 * @param c Collection of property names.
	 */
	public void ignoreProperties(Collection<? extends String> c) {
		ignoredProperties.addAll(c);
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

	/**
	 * @return the dirty
	 */
	public boolean isDirty() {
		boolean d = dirty;
		for (View<Object> v : subViews) {
			d = d || v.isDirty();
		}
		
		return d;
	}

	/**
	 * @param dirty the dirty to set
	 */
	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

	/**
	 * @return the controlAccessorFactory
	 */
	public ControlAccessorFactory getControlAccessorFactory() {
		return controlAccessorFactory;
	}

	/**
	 * @param controlAccessorFactory the controlAccessorFactory to set
	 */
	public void setControlAccessorFactory(ControlAccessorFactory controlAccessorFactory) {
		this.controlAccessorFactory = controlAccessorFactory;
	}

	/**
	 * @return the autobinding
	 */
	public boolean isAutobinding() {
		return autobinding;
	}

	/**
	 * @param autobinding the autobinding to set
	 */
	public void setAutobinding(boolean autobinding) {
		this.autobinding = autobinding;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public BindingResult getBindingResult() {
		if (errors == null && getModel() != null)
			createBindingResult();
		
		return errors;
	}

	/**
	 * {@inheritDoc}
	 */
	public PropertyBinder getBinder(String propertyName) {
		return binder.getBinder(propertyName);
	}

	/**
	 * @return the initializeControls
	 */
	public boolean isInitializeControls() {
		return initializeControls;
	}

	/**
	 * @param initializeControls the initializeControls to set
	 */
	public void setInitializeControls(boolean initializeControls) {
		this.initializeControls = initializeControls;
	}

	/**
	 * @return the controlInitializer
	 */
	public ControlInitializer getControlInitializer() {
		return controlInitializer;
	}

	/**
	 * @param controlInitializer the controlInitializer to set
	 */
	public void setControlInitializer(ControlInitializer controlInitializer) {
		this.controlInitializer = controlInitializer;
	}
}
