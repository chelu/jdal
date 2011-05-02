package info.joseluismartin.table;

import info.joseluismartin.dao.Filter;
import info.joseluismartin.dao.Page;

/**
 * Service for TableEditor  
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
/*
 * Copyright 2008-2011 the original author or authors.
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
public interface TableEditorService<T> {
	
	/**
	 * Save a model (insert or update)
	 * @param model Object to save
	 */
	void save(Object model);
	/**
	 * delete a model
	 * @param model Object to delete
	 */
	void delete(Object model);
	
	/**
	 * get a page of models
	 * @param pageDefinition the page definition
	 * @return page
	 */
	Page<T> getPage(Page<T> page);
	
	
	/**
	 * Return the number registers
	 * @return number registers
	 */
	int count();
	
	/**
	 * Enable predefined filter
	 * 
	 * @param filter the filter data
	 */
	void enableFilter(Filter filter);
}
