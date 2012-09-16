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

import com.vaadin.terminal.Resource;
import com.vaadin.ui.Button.ClickEvent;

import org.springframework.context.MessageSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

/**
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 *
 */
@Configurable
public abstract class TableButtonListener extends ButtonListener {
	
	private PageableTable<?> table;
	@Autowired
	protected transient MessageSource messageSource;
	
	
	public TableButtonListener() {
		this("");
	}

	public TableButtonListener(String caption, Resource icon) {
		super(caption, icon);
	}

	public TableButtonListener(String caption) {
		this(caption, null);
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

	/**
	 * @return the messageSource
	 */
	public MessageSource getMessageSource() {
		return messageSource;
	}

	/**
	 * @param messageSource the messageSource to set
	 */
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
}
