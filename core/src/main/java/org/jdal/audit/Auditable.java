/*
 * Copyright 2008-2010 the original author or authors.
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
package org.jdal.audit;

import java.util.Date;

/**
 * Auditable Objects Interface
 * 
 * @author Jose Luis Martin.
 * @since 1.0
 */
public interface Auditable {
	
	/**
	 * @return the date
	 */
	Date getCreationDate();
	/**
	 * @param date the date to set
	 */
	void setCreationDate(Date date);
	/**
	 * @return the date
	 */
	Date getModificationDate();
	/**
	 * @param date the date to set
	 */
	void setModificationDate(Date date);
	/**
	 * @return the user
	 */
	String getCreationUser();
	/**
	 * @param user the user to set
	 */
	void setCreationUser(String user);
	/**
	 * @return the user
	 */
	String getModificationUser();
	/**
	 * @param user the user to set
	 */
	void setModificationUser(String user);
	
	/**
	 * clean all auditable data (set null all values)
	 */
	void nullAuditable ();
}
