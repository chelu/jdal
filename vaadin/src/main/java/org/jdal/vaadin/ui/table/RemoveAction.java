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
package org.jdal.vaadin.ui.table;

import java.util.Collection;

import org.jdal.cmd.Command;
import org.jdal.cmd.DefaultCommand;
import org.jdal.vaadin.ui.FormUtils;

import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button.ClickEvent;

/**
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 *
 */
public class RemoveAction extends TableButtonListener {

	public RemoveAction() {
		setIcon(new ThemeResource("images/table/edit-delete.png"));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		
		final PageableTable<?> table = getTable();
		final Collection<?> selected = table.getSelected();
		
		if (selected.isEmpty())  // nothing to delete
			return;
		
		Command command = new DefaultCommand() {
			public boolean execute(Object data) {
				table.delete(selected);
				table.refresh();
				return true;
			}
		};
	
		String message = messageSource.getMessage("removeAction.confirm", new Object[] {
				selected.size() }); 
		FormUtils.showConfirmDialog(getTable().getUI(), command, message);
	}
}
