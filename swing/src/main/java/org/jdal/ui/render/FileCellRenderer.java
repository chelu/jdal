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
package org.jdal.ui.render;

import java.awt.Component;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JList;


/**
 * ListCellRenderer that displays values with icon from extension.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class FileCellRenderer extends DefaultListCellRenderer {

	private static final long serialVersionUID = 1L;
	private Map<String, Icon> iconMap = new HashMap<String, Icon>();
	
	@Override
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {

		super.getListCellRendererComponent(list, value, index, isSelected,
				cellHasFocus);
		
		// sets name and icon
		String name = value.toString();
		String extension = name.substring(name.lastIndexOf('.') + 1);
		Icon icon = iconMap.get(extension.toLowerCase());
		if (icon == null) 
			icon = iconMap.get("default");
		setIcon(icon);
		setText(name);
		
		return this;
	}

	public Map<String, Icon> getIconMap() {
		return iconMap;
	}

	public void setIconMap(Map<String, Icon> iconMap) {
		this.iconMap = iconMap;
	}

	
}
