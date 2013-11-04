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
package org.jdal.swing.render;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import org.jdal.swing.ListTableModel;
import org.jdal.util.BeanUtils;

/**
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 *
 */
public class PropertyColorCellRender extends DefaultTableCellRenderer {
	
	private String colorPropertyName = "color";
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		
		TableCellRenderer renderer = table.getDefaultRenderer(table.getColumnClass(column));
		Component c = renderer.getTableCellRendererComponent(table, value, isSelected, hasFocus,
				row, column);
	
		if (!isSelected) {
			ListTableModel tableModel = (ListTableModel) table.getModel();
			Object model = tableModel.getList().get(row);
			Color color = (Color) BeanUtils.getProperty(model, colorPropertyName);
			
			if (color != null)
				c.setBackground(color);
		}
		
		return c;
	}

	/**
	 * @return the colorPropertyName
	 */
	public String getColorPropertyName() {
		return colorPropertyName;
	}

	/**
	 * @param colorPropertyName the colorPropertyName to set
	 */
	public void setColorPropertyName(String colorPropertyName) {
		this.colorPropertyName = colorPropertyName;
	}
	

}
