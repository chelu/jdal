/*
 * Copyright 2009-2014 the original author or authors.
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
package org.jdal.vaadin.data.converter;

import java.text.DateFormat;
import java.util.Locale;

import org.springframework.context.i18n.LocaleContextHolder;

import com.vaadin.data.util.converter.StringToDateConverter;

/**
 * Date converter using {@link LocaleContextHolder}
 * 
 * @author Jose Luis Martin
 * @since 1.0
 */
public class DateConverter extends StringToDateConverter {

	@Override
	protected DateFormat getFormat(Locale locale) {
		if (locale == null) 
			locale = LocaleContextHolder.getLocale();
		
	    DateFormat f = DateFormat.getDateTimeInstance(DateFormat.SHORT,
                DateFormat.SHORT, locale);
        f.setLenient(false);
        
        return f;
	}

}
