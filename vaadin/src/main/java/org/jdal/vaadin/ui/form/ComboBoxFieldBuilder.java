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
package org.jdal.vaadin.ui.form;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.util.Iterator;

import org.jdal.service.PersistentService;
import org.jdal.service.PersistentServiceFactory;
import org.jdal.vaadin.data.ContainerDataSource;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Field;

/**
 * FieldBuilder that creates ComboBox and fill its with items from persistent service
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class ComboBoxFieldBuilder implements FieldBuilder, Serializable {
	
	@Autowired
	private transient PersistentServiceFactory persistentServiceFactory;
	
	/**
	 * Creates a new ComboBoxFieldFactory
	 */
	public ComboBoxFieldBuilder(){
	}

	/**
	 * Creates a new ComboBoxFieldBuilder
	 * @param persistentServiceFactory persistentServiceFactory for creating persistent serivices
	 */
	public ComboBoxFieldBuilder(PersistentServiceFactory persistentServiceFactory) {
		this.persistentServiceFactory = persistentServiceFactory;
	}

	/**
	 * {@inheritDoc}
	 */
	public Field<?> build(Class<?> clazz, String name) {
		ComboBox combo = new ComboBox();
		fillComboBox(combo, clazz, name);
		combo.setItemCaptionMode(ItemCaptionMode.ID);
		
		return combo;
	}	
		
	/**
	 * Fill the ComboBox with items from PersistentService
	 * @param combo ComboBox to fill
	 * @param clazz Class of Bean containing property name
	 * @param name property name
	 */
	protected void fillComboBox(ComboBox combo, Class<?> clazz, String name) {
		PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(clazz, name);
		PersistentService<?, Serializable> service = 
			persistentServiceFactory.createPersistentService(pd.getPropertyType());
		// fill combo
		Iterator<?>  iter = service.getAll().iterator();
		while(iter.hasNext())
			combo.addItem(iter.next());
	}

	/**
	 * Creates a ContainerDataSource for entity class
	 * @param clazz entity class
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected ContainerDataSource<?> buildContainerDataSource(Class<?> clazz) {
		ContainerDataSource<?> cds = new ContainerDataSource(clazz, 
				persistentServiceFactory.createPersistentService(clazz));
		cds.init();
		
		return cds;
	}

	/**
	 * @return the persistentServiceFactory
	 */
	public PersistentServiceFactory getPersistentServiceFactory() {
		return persistentServiceFactory;
	}

	/**
	 * @param persistentServiceFactory the persistentServiceFactory to set
	 */
	public void setPersistentServiceFactory(PersistentServiceFactory persistentServiceFactory) {
		this.persistentServiceFactory = persistentServiceFactory;
	}
}
