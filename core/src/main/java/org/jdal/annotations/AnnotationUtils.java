/*
 * Copyright 2009-2013 Jose Luis Martin.
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
package org.jdal.annotations;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdal.util.BeanUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;
import org.springframework.util.ReflectionUtils.MethodCallback;


/**
 * Utility class for dealing with annotations
 * 
 * @author Jose Luis Martin.
 * @since 2.0
 */
public abstract class AnnotationUtils {
	
	private static final Log log = LogFactory.getLog(AnnotationUtils.class);

	/**
	 * Find annotated elements on types
	 * @param ann annotation to search
	 * @param clazz class to search on.
	 * @return List with annotated elements
	 */
	public static List<AnnotatedElement> findAnnotatedElements(final Class<? extends Annotation> annotationType, Class<?> clazz) {
		final ArrayList<AnnotatedElement> elements = new ArrayList<AnnotatedElement>();
		// Lookup fields
		ReflectionUtils.doWithFields(clazz, new FieldCallback() {
			
			@Override
			public void doWith(Field field) throws IllegalArgumentException,
					IllegalAccessException {
				if (field.getAnnotation(annotationType) != null) {
					elements.add(field);
				}
				
			}
		});
		// Lookup methods
		ReflectionUtils.doWithMethods(clazz, new MethodCallback() {
			
			@Override
			public void doWith(Method method) throws IllegalArgumentException,
					IllegalAccessException {
				
				if (org.springframework.core.annotation.AnnotationUtils.getAnnotation(method, annotationType) != null)
					elements.add(method);
			}
		});
		
		return elements;
	}
	
	/**
	 * Tray to set a value on AnnotatedElement.
	 * @param element the annotated element.
	 * @param value value to set.
	 */
	public static void setValue(AnnotatedElement element, Object target, Object value) {
		if (element instanceof Field) {
			Field field = (Field) element;
			ReflectionUtils.makeAccessible(field);
			ReflectionUtils.setField(field, target, value);
		}
		else if (element instanceof Method) {
			Method method = (Method) element;
			try {
				method.invoke(target, new Object[] {value});
			} catch (Exception e) {
				log.error("Cannot set value on method [" + method.toString() + "]");
			}
		}
	}
	
	public static Object getValue(AnnotatedElement element, Object target) {
		if (element instanceof Field) {
			ReflectionUtils.makeAccessible((Field) element); 
			return ReflectionUtils.getField((Field) element, target);
		}
		else if (element instanceof Method) {
			Method method = (Method) element;
			String name = method.getName();
			if (name.startsWith("set")) {
				String property = StringUtils.uncapitalize(name.substring(3));
				return BeanUtils.getProperty(target, property);
			}
		}
		
		return null;
	}
 }
