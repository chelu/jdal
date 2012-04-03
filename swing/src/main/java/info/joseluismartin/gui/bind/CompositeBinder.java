package info.joseluismartin.gui.bind;

import info.joseluismartin.beans.PropertyUtils;
import info.joseluismartin.gui.Binder;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.PropertyAccessor;
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
public class CompositeBinder<T> implements Binder<T>, BinderHolder {
	
	private BinderFactory binderFactory;
	private Map<String, Binder<T>> binders = new HashMap<String, Binder<T>>();
	/** Default model to bind on for property binders */
	private T model;
	/** Binding result */
	private BindingResult bindingResult;

	
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
		bind(component, propertyName, getModel(), readOnly);
	}


	public void bind(Object component, String propertyName, T model, boolean readOnly) {
		PropertyBinder binder =  binderFactory.getBinder(component.getClass());
		if (binder != null) {
			binder.bind(component, propertyName, model, readOnly);
			binders.put(propertyName, (Binder<T>) binder);
		}
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
		
		if (PropertyUtils.isNested(propertyName)) {
			BinderHolder binderHolder = (BinderHolder) binders.get(PropertyUtils.getFirstPropertyName(propertyName));
			return binderHolder != null ?
					binderHolder.getBinder(PropertyUtils.getNestedPath(propertyName)) : null;
		}
		
		return (PropertyBinder) binders.get(propertyName);
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
		for (Binder<T> b : binders.values()) {
			b.setModel(model);
		}
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
}
