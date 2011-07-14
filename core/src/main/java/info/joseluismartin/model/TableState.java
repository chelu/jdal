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
package info.joseluismartin.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.propertyeditors.StringArrayPropertyEditor;

/**
 * Hold State info for UI Tables
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 * @since 1.1
 */
public class TableState extends Entity {
	
	private List<String> visibleColumns = new ArrayList<String>();
	private int pageSize;
	
	
	/**
	 * Gets visible columns property names
	 * @return the visible Columns
	 */
	public List<String> getVisibleColumns() {
		return visibleColumns;
	}
	
	/**
	 * Set the visible columns property names
	 * @param visibleColumns the visibleColumns to set
	 */
	public void setVisibleColumns(List<String> visibleColumns) {
		this.visibleColumns = visibleColumns;
	}
	
	/**
	 * Set visible columns as CSV String 
	 * @param value the CSV String
	 */
	public void setVisibleColumns(String value) {
		StringArrayPropertyEditor pe = new StringArrayPropertyEditor();
		pe.setAsText(value);
		visibleColumns = Arrays.asList(( String[]) pe.getValue());
	}

	/**
	 * @param parseInt
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
		
	}

	/**
	 * @return the pageSize
	 */
	public int getPageSize() {
		return pageSize;
	}
}
