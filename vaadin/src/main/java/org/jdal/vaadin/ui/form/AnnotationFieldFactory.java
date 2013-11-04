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

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.ManyToOne;

import org.apache.commons.lang.ArrayUtils;
import org.jdal.service.PersistentServiceFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ReflectionUtils;

import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;

/**
 * FieldFactory that create fields based on Annotations.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class AnnotationFieldFactory extends ConfigurableFieldFactory {
	
	@Autowired
	private transient PersistentServiceFactory persistentServiceFactory;
	
	private Map<Class<?extends Annotation>, FieldBuilder> annotationMap = Collections.synchronizedMap(
			new HashMap<Class<? extends Annotation>, FieldBuilder>());

	
	public AnnotationFieldFactory() {
	}
	
	public void init() {
		configureDefaults();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Field createField(Item item, Object propertyId, Component uiContext) {
		if (item instanceof BeanItem<?>) {
			BeanItem<?> bi = (BeanItem<?>) item;
			String name = (String) propertyId;
			Class<?> clazz = bi.getBean().getClass();
			java.lang.reflect.Field field = ReflectionUtils.findField(clazz, name);
			Annotation[] fa = new Annotation[] {};
			if (field != null) {
				fa = field.getAnnotations();
			}
			java.lang.reflect.Method method = BeanUtils.getPropertyDescriptor(clazz, name).getReadMethod();
			Annotation[] ma = method.getAnnotations();
			Annotation[] annotations = (Annotation[]) ArrayUtils.addAll(fa, ma);
			Field f = null;
			for (Annotation a : annotations) {
				f = findField(a, clazz, name);
				if (f != null) {
					f.setCaption(createCaptionByPropertyId(propertyId));
					applyFieldProcessors(f, propertyId);
					return f;
				}
			}
		}
		// fall back to default
		return super.createField(item, propertyId, uiContext);
	}

	/**
	 * Find a field instance for Annotation
	 * @param a Annotation
	 * @param name 
	 * @param clazz 
	 * @return Field instance
	 */
	protected Field findField(Annotation a, Class<?> clazz, String name) {
		FieldBuilder builder = annotationMap.get(a.annotationType());
		
		return builder != null ? builder.build(clazz, name) : null;
	}
	
	/**
	 * 
	 */
	private void configureDefaults() {
		ComboBoxFieldBuilder comboBuilder = new ComboBoxFieldBuilder(persistentServiceFactory);
		annotationMap.put(ManyToOne.class, comboBuilder);
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

	/**
	 * @return the annotationMap
	 */
	public Map<Class<? extends Annotation>, FieldBuilder> getAnnotationMap() {
		return annotationMap;
	}

	/**
	 * @param annotationMap the annotationMap to set
	 */
	public void setAnnotationMap(Map<Class<? extends Annotation>, FieldBuilder> annotationMap) {
		this.annotationMap.clear();
		this.annotationMap.putAll(annotationMap);
	}

}
