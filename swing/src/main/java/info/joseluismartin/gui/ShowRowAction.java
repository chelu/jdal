/*
 * Copyright 2002-2010 the original author or authors.
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
package info.joseluismartin.gui;

import java.awt.event.ActionEvent;

import javax.swing.JDialog;

/**
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 *
 */
@SuppressWarnings("unchecked")
public class ShowRowAction extends TableRowAction {
	
	private static final long serialVersionUID = 1L;
	private String viewName;
	private GuiFactory guiFactory;

	/* (non-Javadoc)
	 * @see info.joseluismartin.gui.TableRowAction#actionPerformed(java.awt.event.ActionEvent)
	 */
	
	@Override
	public void actionPerformed(ActionEvent e) {
		JDialog dialog = guiFactory.getDialog(viewName);
		View view = (View) dialog;
		view.setModel(getRow());
		view.refresh();
		dialog.setVisible(true);
	}

	public String getViewName() {
		return viewName;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	public GuiFactory getGuiFactory() {
		return guiFactory;
	}

	public void setGuiFactory(GuiFactory guiFactory) {
		this.guiFactory = guiFactory;
	}
}
