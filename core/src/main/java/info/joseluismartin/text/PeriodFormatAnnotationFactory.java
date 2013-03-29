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
package info.joseluismartin.text;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Formatter;
import org.springframework.format.Parser;
import org.springframework.format.Printer;

/**
 * PeriodFormat Annotation Factory.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class PeriodFormatAnnotationFactory implements AnnotationFormatterFactory<PeriodFormat> {

	/**
	 * {@inheritDoc}
	 */
	public Set<Class<?>> getFieldTypes() {
		 return new HashSet<Class<?>>(Arrays.asList(new Class<?>[] {
		            Short.class, Integer.class, Long.class, Float.class,
		            Double.class, BigDecimal.class, BigInteger.class }));
	}

	/**
	 * {@inheritDoc}
	 */
	public Printer<?> getPrinter(PeriodFormat annotation, Class<?> fieldType) {
		return getFormatter(annotation, fieldType);
	}

	/**
	 * {@inheritDoc}
	 */
	public Parser<?> getParser(PeriodFormat annotation, Class<?> fieldType) {
		return getFormatter(annotation, fieldType);
	}
	
	/**
	 * @param annotation
	 * @param fieldType
	 * @return
	 */
	protected Formatter<?> getFormatter(PeriodFormat annotation, Class<?> fieldType) {
		return new PeriodFormatter();
	}
}
