package org.jdal.vaadin.data;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;

import com.google.gwt.aria.client.Property;
import com.vaadin.data.util.AbstractProperty;

/**
 * {@link Property} implementation for beans delegating to a spring {@link BeanWrapper}.
 * 
 * @author Jose Luis Martin
 * @param <T> property type.
 * @since 2.1
 */
@SuppressWarnings("unchecked")
public class BeanWrapperProperty<T> extends AbstractProperty<T> {
	private String propertyName;
	private BeanWrapper beanWrapper;
	
	public BeanWrapperProperty(Object bean, String propertyName) {
		this.propertyName = propertyName;
		
		if (bean instanceof BeanWrapper) {
			this.beanWrapper = (BeanWrapper) bean;
		}
		else {
			this.beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(bean);
		}
	}
	
	@Override
	public T getValue() {
		return (T) this.beanWrapper.getPropertyValue(this.propertyName);
	}

	@Override
	public void setValue(T newValue)
			throws com.vaadin.data.Property.ReadOnlyException {
		this.beanWrapper.setPropertyValue(this.propertyName, newValue);
	}

	@Override
	public Class<? extends T> getType() {
		return (Class<? extends T>) this.beanWrapper.getPropertyDescriptor(this.propertyName).getPropertyType();
	}

}
