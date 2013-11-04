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
package org.jdal.swing;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 *
 */
public class ActionCellRenderer extends DefaultTableCellRenderer {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public ActionCellRenderer() {
		super();
		setHorizontalAlignment(JLabel.CENTER);
	}

	/** 
	 * Set the icon from Action
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	@Override
	public void setValue(Object value) {
		if (value instanceof Action) {
			Icon icon = (Icon) ((Action) value).getValue(Action.SMALL_ICON);
			setIcon(icon);
		}
	}
}
