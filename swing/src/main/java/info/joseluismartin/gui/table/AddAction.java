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
package info.joseluismartin.gui.table;

import info.joseluismartin.gui.form.FormUtils;

import java.awt.event.ActionEvent;

import javax.swing.JDialog;

/**
 * Default Add Action for TablePanel that use configured TablePanel editor to add an Object.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class AddAction extends TablePanelAction {

	private static final long serialVersionUID = 1L;
	private static final String DEFAULT_ICON = "/images/table/22x22/document-new.png";
	
	public void init() {
		setIcon(FormUtils.getIcon(getIcon(), DEFAULT_ICON));
	}

	public void actionPerformed(ActionEvent e) {
		JDialog dlg = getTablePanel().getDialog();
		if (dlg != null) {
			dlg.setModal(true);
			dlg.setVisible(true);
			getTablePanel().refresh();
		}
	}
}
