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
package org.jdal.reporting;

import java.io.Serializable;

import org.jdal.dao.Page;
import org.jdal.service.PersistentService;

/**
 * This interface must be implemented by those classes that require working with reports
 * 
 * @author Jose A. Corbacho
 *
 */
public interface ReportDataProvider<T, PK extends Serializable> {

	/**
	 * Returns the data source used by this object
	 * @return the data source in use
	 */
	PersistentService<T, PK> getDataSource();
	
	public Object getFilter();
	
	public String getSortProperty();
	
	public Page.Order getSortOrder();
	
}
