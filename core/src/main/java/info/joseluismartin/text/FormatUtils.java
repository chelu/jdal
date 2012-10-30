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
package info.joseluismartin.text;

import java.beans.PropertyDescriptor;

import org.springframework.beans.BeanUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Printer;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.number.NumberFormatAnnotationFormatterFactory;

/**
 * Utility class for getting Printers an Parsers for Format annotated properties.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
@SuppressWarnings("unchecked")
public abstract class FormatUtils {
	private  static AnnotationFormatterFactory<NumberFormat> numberFormatFactory =
				new NumberFormatAnnotationFormatterFactory();

	
	public static Printer<Object> getPrinter(Class<?> clazz, String propertyName) {
		PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(clazz, propertyName);
		if (pd != null) {
			NumberFormat format = AnnotationUtils.getAnnotation(pd.getReadMethod(), NumberFormat.class);
			if (format != null) {
				return (Printer<Object>) numberFormatFactory.getPrinter(format, pd.getPropertyType());
			}
		}
		
		return null;
	}
}
