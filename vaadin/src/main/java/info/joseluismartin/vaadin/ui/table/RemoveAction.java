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

import info.joseluismartin.cmd.Command;
import info.joseluismartin.cmd.DefaultCommand;
import info.joseluismartin.vaadin.ui.FormUtils;

import java.util.Collection;

import com.vaadin.ui.Button.ClickEvent;

/**
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 *
 */
public class RemoveAction extends TableButtonListener {

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
	
		FormUtils.showConfirmDialog(getTable().getWindow(), command,
				"<b>This will remove " + selected.size() + " items.<br> <center>Are you sure?</center><br></b>");
		
		
	}

}
