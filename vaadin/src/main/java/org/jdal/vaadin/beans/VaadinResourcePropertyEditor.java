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
package org.jdal.vaadin.beans;

import java.beans.PropertyEditorSupport;

import org.apache.commons.lang.StringUtils;

import com.vaadin.server.ClassResource;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;

public class VaadinResourcePropertyEditor extends PropertyEditorSupport {
	
	private static final String THEME_PREFIX = "theme:";
	private static final String CLASSPATH_PREFIX = "classpath:";
	private static final String URL_PREFIX = "url:";
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		Resource resource;
		
		if (text.startsWith(CLASSPATH_PREFIX)) 
			resource = new ClassResource(StringUtils.substringAfter(text, CLASSPATH_PREFIX));
		else if (text.startsWith(THEME_PREFIX))
			resource = new ThemeResource(StringUtils.substringAfter(text, THEME_PREFIX));
		else if (text.startsWith(URL_PREFIX))
			resource = new ExternalResource(StringUtils.substringAfter(text, URL_PREFIX));
		else if (text.contains(":"))
			resource = new ExternalResource(text);
		else 
			resource = new ThemeResource(text);
		
		setValue(resource);
	}
		
}
