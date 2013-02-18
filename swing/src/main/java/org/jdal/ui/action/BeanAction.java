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
package org.jdal.ui.action;

import javax.swing.Action;

import org.jdal.ui.IconAction;


/**
 * Add bean properties to Actions to configure friendly in Spring bean configuration files
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public abstract class BeanAction  extends IconAction {

	private static final long serialVersionUID = 1L;
	
	public String getName() {
		return (String ) getValue(Action.NAME);
	}
	
	public void setName(String name) {
		putValue(Action.NAME, name);
	}
}
