/*
 * Copyright 2009-2012 Jose Luis Martin.
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

import org.apache.commons.lang.ArrayUtils;
import org.jdal.annotation.SerializableProxy;
import org.jdal.dao.Dao;
import org.jdal.util.BeanUtils;
import org.springframework.util.ReflectionUtils;

/**
 * Support class for ControlInitializers.
 * 
 * @author Jose Luis Martin
 * @see ControlInitializer
 * @since 2.0
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class ControlInitializerSupport implements ControlInitializer {

	protected Dao<Object, ?extends Serializable> dao;
	private boolean initializeEntities = false;
	private boolean firstNull = false;

	protected List<Object> getEntityList(Class<?> propertyType) {
		List entities =  dao.getAll(propertyType);
		if (isInitializeEntities()) {
			for (Object entity : entities)
				dao.initialize(entity);
		}
		if (isFirstNull())
			entities.add(0, null);
		
		return entities;
	}

	/**
	 * @param entities
	 * @return
	 */
	protected List<Object> getValueList(List entities, String propertyName) {
		List<Object> values = new ArrayList<Object>(entities.size());
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
	protected Annotation[] getAnnotations(String property, Class<?> clazz) {
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
	 * @return the Dao
	 */
	public Dao<Object, ? extends Serializable> getPersistentService() {
		return dao;
	}

	/**
	 * @param Dao the Dao to set
	 */
	public void setPersistentService(Dao<Object, ? extends Serializable> Dao) {
		this.dao = Dao;
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
