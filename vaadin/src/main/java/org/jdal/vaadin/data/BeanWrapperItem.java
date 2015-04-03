/*
 * Copyright 2009-2015 Jose Luis Martin
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
package org.jdal.vaadin.data;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;

import com.vaadin.data.Item;
import com.vaadin.data.Property;

/**
 * An adapter to use beans as {@link Item Items}.
 * 
 * @author Jose Luis Martin
 * @param <T> Bean type.
 * @since 2.1
 */
public class BeanWrapperItem implements Item {
	
	private List<String> properties = new ArrayList<String>();
	private BeanWrapper beanWrapper;
	
	public BeanWrapperItem (Object bean) {
		this(bean, null);
	}
	
	public BeanWrapperItem(Object bean, List<String> properties) {
		this.beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(bean);
		
		if (properties == null) {
			for (PropertyDescriptor pd : BeanUtils.getPropertyDescriptors(bean.getClass()))
				this.properties.add(pd.getName());
		}
		else {
			this.properties = properties;
		}
		
	}
	
	@Override
	@SuppressWarnings("rawtypes")
	public Property getItemProperty(Object id) {
		return new BeanWrapperProperty(this.beanWrapper, (String) id);
	}

	@Override
	public Collection<?> getItemPropertyIds() {
		return this.properties;
	}

	@Override
	@SuppressWarnings("rawtypes")
	public boolean addItemProperty(Object id, Property property)
			throws UnsupportedOperationException {
		this.properties.add((String) id);
		
		return true;
	}

	@Override
	public boolean removeItemProperty(Object id)
			throws UnsupportedOperationException {
		return this.properties.remove((String) id);
	}

}
