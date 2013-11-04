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
package org.jdal.text;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Formatter;
import org.springframework.format.Parser;
import org.springframework.format.Printer;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.number.NumberFormatAnnotationFormatterFactory;

/**
 * Utility class for getting Printers an Parsers for Format annotated properties.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public abstract class FormatUtils {
	
	private static final Log log = LogFactory.getLog(FormatUtils.class);
	private  static AnnotationFormatterFactory<NumberFormat> numberFormatFactory =
				new NumberFormatAnnotationFormatterFactory();

	
	/**
	 * Get Printer for class and property name
	 * @param clazz the class
	 * @param propertyName the property name
	 * @return Printer or null if none
	 */
	public static Printer<?> getPrinter(Class<?> clazz, String propertyName) {
		return getFormatter(clazz, propertyName);
	}
	
	/**
	 * Get Parser for property name
	 * @param clazz Class
	 * @param propertyName the property name
	 * @return Parser or null if none
	 */
	public static Parser<?> getParser(Class<?> clazz, String propertyName) {
		return getFormatter(clazz, propertyName);
	}

	/**
	 * Get a formatter for class and property name
	 * @param clazz the class
	 * @param propertyName the property name
	 * @return the formatter or null if none
	 */
	public static Formatter<?> getFormatter(Class<?> clazz, String propertyName) {
		PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(clazz, propertyName);
		if (pd != null) {
			NumberFormat format = getAnnotation(pd, NumberFormat.class);
			if (format != null) {
				return (Formatter<?>) numberFormatFactory.getPrinter(format, pd.getPropertyType());
			}
			
			PeriodFormat periodFormat = getAnnotation(pd, PeriodFormat.class);
			if (periodFormat != null)
				return new PeriodFormatter();
		}
		
		return null;
	}
	
	public static <A extends Annotation> A getAnnotation(PropertyDescriptor pd, Class<A> annotationType) {
		A  annotation = AnnotationUtils.getAnnotation(pd.getReadMethod(), annotationType);
		if (annotation != null) 
			return annotation;
		
		Field field = getDeclaredField(pd);
	
		if (field != null)
			annotation = AnnotationUtils.getAnnotation(field, annotationType);
	
		return annotation;
	}

	/**
	 * Field for property name, if any
	 * @param pd PropertyDescriptor
	 * @return property field or null if none.
	 */
	public static Field getDeclaredField(PropertyDescriptor pd) {
	
		try {
			return pd.getReadMethod().getDeclaringClass().getDeclaredField(pd.getName());
		} 
		catch (Exception e) {
			if (log.isDebugEnabled())
				log.debug("Cannot access to field: " + pd.getName());
			
			return null;
		}
	}

	
}
