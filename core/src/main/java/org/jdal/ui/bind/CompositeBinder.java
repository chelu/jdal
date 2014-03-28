package org.jdal.ui.bind;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdal.beans.PropertyUtils;
import org.jdal.ui.Binder;
import org.jdal.ui.ModelHolder;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessor;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

/**
 * Composite Binder methods to a collection of
 * PropertyBinders that bind on the same model 
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 * @param <T> model 
 */
@SuppressWarnings("unchecked")
public class CompositeBinder<T> implements Binder<T>, BinderHolder, ModelHolder<T>, Serializable {
	
	private static final Log log = LogFactory.getLog(CompositeBinder.class);
	private BinderFactory binderFactory;
	private Map<String, Binder<T>> binders = new HashMap<String, Binder<T>>();
	/** Default model to bind on for property binders */
	private T model;
	/** Binding result */
	private BindingResult bindingResult;
	private List<String> ignoredProperties = new ArrayList<String>();

	/**
	 * Create new CompositeBinder
	 */
	public CompositeBinder() {		
	}
	
	/**
	 * @param model model to bind on
	 */
	public CompositeBinder(T model) {
		this.model = model;
	}

	public void bind(Object component, String propertyName) {
		bind(component, propertyName, false);
	}
	

	public void bind(Object component, String propertyName, boolean readOnly) {
		bind(component, propertyName, this, readOnly);
	}


	public void bind(Object component, String propertyName, Object model, boolean readOnly) {
		PropertyBinder binder =  binderFactory.getBinder(component.getClass());
		if (binder != null) {
			binder.bind(component, propertyName, model, readOnly);
			addBinder(propertyName, binder);
		}
	}

	public void addBinder(String propertyName, PropertyBinder binder) {
		binders.put(propertyName, (Binder<T>) binder);
	}

	public void refresh() {
		for (Binder<?> b : binders.values())
			b.refresh();
	}

	public void addBinder(Binder<?> binder, String name) {
		binders.put(name, (Binder<T>) binder);
	}
	

	
	public void update() {
		bindingResult = null;
		
		for (Binder<?> b : binders.values())
			b.update();
		
	}
	
	public PropertyBinder getBinder(String propertyName) {
		PropertyBinder binder = (PropertyBinder) binders.get(propertyName);
		if (binder != null)
			return binder;
		
		if (PropertyUtils.isNested(propertyName)) {
			BinderHolder binderHolder = (BinderHolder) binders.get(PropertyUtils.getFirstPropertyName(propertyName));
			return binderHolder != null ?
					binderHolder.getBinder(PropertyUtils.getNestedPath(propertyName)) : null;
		}
		
		return null;
	}
	
	public Set<String> getPropertyNames() {
		return binders.keySet();
	}
	
	public Collection<Binder<T>> getPropertyBinders() {
		return binders.values();
	}

	/**
	 * @return the binderFactory
	 */
	public BinderFactory getBinderFactory() {
		return binderFactory;
	}

	/**
	 * @param binderFactory the binderFactory to set
	 */
	public void setBinderFactory(BinderFactory binderFactory) {
		this.binderFactory = binderFactory;
	}

	public T getModel() {
		return model;
	}
	
	public void setModel(T model) {
		this.model = model;
	}

	/**
	 * {@inheritDoc}
	 */
	public BindingResult getBindingResult() {
		if (getModel() == null)
			return null;

		if (bindingResult == null) {
			createBindingResult();
			for (Binder<?> b : binders.values()) {
				if (b.getBindingResult() != null && 
						bindingResult.getObjectName().equals(b.getBindingResult().getObjectName()))
					bindingResult.addAllErrors(b.getBindingResult());
			}
		}
		
		return bindingResult;
	}
	
	private void createBindingResult() {
		bindingResult = new BeanPropertyBindingResult(getModel(), getModel().getClass().getSimpleName());
	}
	
	public void autobind(Object view) {
		BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(getModel());
		PropertyAccessor  viewPropertyAccessor = new DirectFieldAccessor(view);
		// iterate on model properties
		for (PropertyDescriptor pd : bw.getPropertyDescriptors()) {
			String propertyName = pd.getName();
			if ( !ignoredProperties.contains(propertyName) && viewPropertyAccessor.isReadableProperty(propertyName)) {
				Object control = viewPropertyAccessor.getPropertyValue(propertyName);
				if (control != null) {
					if (log.isDebugEnabled()) 
						log.debug("Found control: " + control.getClass().getSimpleName() + 
								" for property: " + propertyName);
					bind(control, propertyName);
				}
			}
		}
	}

	/**
	 * @return the ignoredProperties
	 */
	public List<String> getIgnoredProperties() {
		return ignoredProperties;
	}

	/**
	 * @param ignoredProperties the ignoredProperties to set
	 */
	public void setIgnoredProperties(List<String> ignoredProperties) {
		this.ignoredProperties = ignoredProperties;
	}
	
	/**
	 * Add a property name  to ignore on binding.
	 * @param propertyName property name to ignore
	 */
	public void ignoreProperty(String propertyName) {
		ignoredProperties.add(propertyName);
	}

}
