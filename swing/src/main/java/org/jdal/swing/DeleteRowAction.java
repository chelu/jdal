/*
 * Copyright 2009-2011 original author or authors.
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

package org.jdal.swing;


import java.awt.event.ActionEvent;
import java.io.Serializable;

import javax.swing.JOptionPane;

import org.jdal.dao.Dao;

/**
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 *
 */
public class DeleteRowAction extends TableRowAction {
	
	private static final long serialVersionUID = 1L;
	private Dao<Object, Serializable> service;

	/* (non-Javadoc)
	 * @see info.joseluismartin.gui.TableRowAction#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		String message = "¿Desea borrar el registro?";
		if (JOptionPane.showConfirmDialog(null, message, "Confirme, por favor", 
					JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
			service.delete(getRow());
			((PageableTable<?>)e.getSource()).refresh();
		}
		
	}

	public Dao<Object, Serializable> getService() {
		return service;
	}

	public void setService(Dao<Object, Serializable> service) {
		this.service = service;
	}
}
