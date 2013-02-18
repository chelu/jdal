/*
 * Copyright 2008-2011 Jose Luis Martin
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
package org.jdal.dao;

import java.io.Serializable;
import java.util.List;

/**
 * Simple interface to get data by pages
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public interface PageableDataSource<T> {
	/**
	 * Fill and return a page of data
	 * @return the page
	 */
	public Page<T> getPage(Page<T> page);
	/**
	 * Gets a List with keys of page
	 * @param page
	 * @return List with keys
	 */
	public List<Serializable> getKeys(Page<T> page);
}
