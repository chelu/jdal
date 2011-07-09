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

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.ManyToOne;

/**
 * Hold State info for UI Tables
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 * @since 1.1
 */
public class TableState extends Entity {
	
	@ManyToOne
	private User user;
	private List<String> visibleColumns = new ArrayList<String>();
	
	
	/**
	 * Gets visible columns property names
	 * @return the visible Columns
	 */
	public List<String> getVisibleColumns() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Set the visible columns property names
	 * @param visibleColumns the visibleColumns to set
	 */
	public void setVisibleColumns(List<String> visibleColumns) {
		this.visibleColumns = visibleColumns;
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}
}
