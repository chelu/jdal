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

import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ActionEvent;

/**
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class ShowRowAction extends TableRowAction {
	
	private static final long serialVersionUID = 1L;
	private String viewName;
	private GuiFactory guiFactory;
	private boolean modal = false;

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		Window dlg = getTable().getEditor(getRow());
		if (dlg != null) {
			if (dlg instanceof Frame) {
				((Frame) dlg).setState(Frame.NORMAL);
				((Frame) dlg).requestFocus();
			}
			dlg.setVisible(true);
		}
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

	/**
	 * @return the modal
	 */
	public boolean isModal() {
		return modal;
	}

	/**
	 * @param modal the modal to set
	 */
	public void setModal(boolean modal) {
		this.modal = modal;
	}
	
	
}

	
