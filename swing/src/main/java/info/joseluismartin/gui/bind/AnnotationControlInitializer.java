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
package info.joseluismartin.gui.bind;

import groovy.swing.binding.JComboBoxMetaMethods;
import info.joseluismartin.gui.list.ListComboBoxModel;
import info.joseluismartin.service.PersistentService;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import javax.persistence.ManyToOne;
import javax.swing.JComboBox;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;


/**
 * Initialize control by JPA Annotations.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class AnnotationControlInitializer implements ControlInitializer {

	private static final Log log = LogFactory.getLog(AnnotationControlInitializer.class);
	private PersistentService<?extends Object, ?extends Serializable> persistentService;
	
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
			if (ManyToOne.class.equals(a.annotationType()) && control instanceof JComboBox) {
				// fill combo from persistent service
				List<?> entities = persistentService.getAll(propertyType);
				((JComboBox) control).setModel(new ListComboBoxModel(entities));
			}
		}
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
		Annotation[] ma = method.getAnnotations();
		Annotation[] annotations = (Annotation[]) ArrayUtils.addAll(fa, ma);
		return annotations;
	}
	
	
	/**
	 * @return the persistentService
	 */
	public PersistentService<? extends Object, ? extends Serializable> getPersistentService() {
		return persistentService;
	}
	
	/**
	 * @param persistentService the persistentService to set
	 */
	public void setPersistentService(PersistentService<? extends Object, ? extends Serializable> persistentService) {
		this.persistentService = persistentService;
	}

}
