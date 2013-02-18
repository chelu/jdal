/*
 * Copyright 2009-2012 the original author or authors.
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
package org.jdal.ui.bind;


import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.ManyToOne;
import javax.swing.JComboBox;
import javax.swing.JList;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdal.annotations.Reference;
import org.jdal.service.PersistentService;
import org.jdal.ui.list.ListComboBoxModel;
import org.jdal.ui.list.ListListModel;
import org.jdal.util.BeanUtils;
import org.springframework.util.ReflectionUtils;


/**
 * Initialize control by JPA Annotations.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class AnnotationControlInitializer implements ControlInitializer {
	
	private static final Log log = LogFactory.getLog(AnnotationControlInitializer.class);
	private PersistentService<Object, ?extends Serializable> persistentService;
	private boolean initializeEntities = false;
	private boolean firstNull = false;
	
	/**
	 * {@inheritDoc}
	 */
	public void initialize(Object control, String property, Class<?> clazz) {
		if (persistentService == null) {
			log.warn("Nothing to do without persistent service");
			return;
		}
		Class<?> propertyType = BeanUtils.getPropertyDescriptor(clazz, property).getPropertyType();
		Annotation[] annotations = getAnnotations(property, clazz);
		for (Annotation a : annotations) {
			if (ManyToOne.class.equals(a.annotationType())) {
				List<Object> entities = getEntityList(propertyType);
				if (control instanceof JComboBox) {
					((JComboBox) control).setModel(new ListComboBoxModel(entities));
				}
				else if (control instanceof JList) {
					((JList) control).setModel(new ListListModel(entities));
				}
				break;
			}
				
			if (Reference.class.equals(a.annotationType()) && control instanceof JComboBox) {
				Reference r = (Reference) a;
				Class type = void.class.equals(r.target()) ? propertyType : r.target();
				List entities = getEntityList(type);
				List values = StringUtils.isEmpty(r.property()) ?  entities : 
					getValueList(entities, r.property());
				
				((JComboBox) control).setModel(new ListComboBoxModel(values));
				break;
			}
			
		}
	}

	private List<Object> getEntityList(Class<?> propertyType) {
		List entities =  persistentService.getAll(propertyType);
		if (isInitializeEntities()) {
			for (Object entity : entities)
				persistentService.initialize(entity);
		}
		if (isFirstNull())
			entities.add(0, null);
		
		return entities;
	}

	/**
	 * @param entities
	 * @return
	 */
	private List getValueList(List entities, String propertyName) {
		List values = new ArrayList(entities.size());
		Iterator iter = entities.iterator();
	
		while (iter.hasNext()) {
			Object value = iter.next();
			if  (value == null)
				values.add(null);
			else {
				values.add(BeanUtils.getProperty(value, propertyName));
			}
		}
		
		return values;
	}

	/**
	 * Get field or method annotations
	 * @param property
	 * @param clazz
	 * @return
	 */
	private Annotation[] getAnnotations(String property, Class<?> clazz) {
		Field field = ReflectionUtils.findField(clazz, property);
		Annotation[] fa = new Annotation[] {};
		if (field != null) {
			fa = field.getAnnotations();
		}
		Method method = BeanUtils.getPropertyDescriptor(clazz, property).getReadMethod();
		if (method != null) {
			Annotation[] ma = method.getAnnotations();
			Annotation[] annotations = (Annotation[]) ArrayUtils.addAll(fa, ma);
			return annotations;
		}
		
		return fa;
	}
	
	
	/**
	 * @return the persistentService
	 */
	public PersistentService<Object, ? extends Serializable> getPersistentService() {
		return persistentService;
	}
	
	/**
	 * @param persistentService the persistentService to set
	 */
	public void setPersistentService(PersistentService<Object, ? extends Serializable> persistentService) {
		this.persistentService = persistentService;
	}

	/**
	 * @return the initializeEntities
	 */
	public boolean isInitializeEntities() {
		return initializeEntities;
	}

	/**
	 * @param initializeEntities the initializeEntities to set
	 */
	public void setInitializeEntities(boolean initializeEntities) {
		this.initializeEntities = initializeEntities;
	}

	/**
	 * @return the firstNull
	 */
	public boolean isFirstNull() {
		return firstNull;
	}

	/**
	 * @param firstNull the firstNull to set
	 */
	public void setFirstNull(boolean firstNull) {
		this.firstNull = firstNull;
	}

}
