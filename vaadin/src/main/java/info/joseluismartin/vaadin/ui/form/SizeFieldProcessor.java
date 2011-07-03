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
package info.joseluismartin.vaadin.ui.form;

import java.util.HashMap;
import java.util.Map;

import com.vaadin.ui.Field;

/**
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 *
 */
public class SizeFieldProcessor implements FieldProcessor {
	
	private String defaultWidth;
	private String defaultHeight;
	private Map<Object, String> widths = new HashMap<Object, String>();
	private Map<Object, String> heights = new HashMap<Object, String>();
	
	/**
	 * {@inheritDoc}
	 */
	public void processField(Field field, Object propertyId) {
		String width = widths.get(propertyId);
		if (width == null)
			width = defaultWidth;
		if (width != null) 
			field.setWidth(width);
		
		String height = heights.get(propertyId);
		if (height == null)
			height = defaultHeight;
		if (height != null) 
			field.setHeight(height);
	}
	
	/**
	 * @return the defaultWidth
	 */
	public String getDefaultWidth() {
		return defaultWidth;
	}
	/**
	 * @param defaultWidth the defaultWidth to set
	 */
	public void setDefaultWidth(String defaultWidth) {
		this.defaultWidth = defaultWidth;
	}
	/**
	 * @return the defaultHeight
	 */
	public String getDefaultHeight() {
		return defaultHeight;
	}
	/**
	 * @param defaultHeight the defaultHeight to set
	 */
	public void setDefaultHeight(String defaultHeight) {
		this.defaultHeight = defaultHeight;
	}
	/**
	 * @return the widths
	 */
	public Map<Object, String> getWidths() {
		return widths;
	}
	/**
	 * @param widths the widths to set
	 */
	public void setWidths(Map<Object, String> widths) {
		this.widths = widths;
	}
	/**
	 * @return the heights
	 */
	public Map<Object, String> getHeights() {
		return heights;
	}
	/**
	 * @param heights the heights to set
	 */
	public void setHeights(Map<Object, String> heights) {
		this.heights = heights;
	}
} 
