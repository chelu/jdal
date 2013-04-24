/*
 * Copyright 2009-2012 the original author or authors.
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
package org.jdal.swing.table;


import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.JOptionPane;

import org.jdal.swing.PageableTable;

/**
 * Remove only visible selected items on a table panel.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class RemoveAllVisibleAction extends RemoveAllAction {

	@SuppressWarnings("unchecked")
	public void actionPerformed(ActionEvent e) {
	
		PageableTable<Object> table = getTablePanel().getTable(); 
		List<Object> selected = getTablePanel().getVisibleSelected();
		
		if (selected.size() == 0)
			return;	// nothing to do
		
		String message = messageSource.getMessage("RemoveAllAction.confirm", new Object[] { selected.size() });
		
		if (JOptionPane.showConfirmDialog(getTablePanel(), message,
				messageSource.getMessage("RemoveAllAction.confirmationMessage"), 
				JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
			table.getTableModel().uncheckAll();
			getTablePanel().getPersistentService().delete(selected);
			// refresh table
			table.getPaginator().firstPage();
		}
	}
}
