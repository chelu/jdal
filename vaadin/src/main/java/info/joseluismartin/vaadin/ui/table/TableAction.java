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
package info.joseluismartin.vaadin.ui.table;

import com.vaadin.event.Action;
import com.vaadin.terminal.Resource;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

/**
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 *
 */
public abstract class TableAction extends Action implements ClickListener {
	
	PageableTable<?> table;
	
	
	public TableAction() {
		this("");
	}

	public TableAction(String caption, Resource icon) {
		super(caption, icon);
	}

	public TableAction(String caption) {
		super(caption);
	}

	/**
	 * @return the table
	 */
	public PageableTable<?> getTable() {
		return table;
	}

	/**
	 * @param table the table to set
	 */
	public void setTable(PageableTable<?> table) {
		this.table = table;
	}

	/**
	 * {@inheritDoc}
	 */
	public abstract void buttonClick(ClickEvent event);

	
}
