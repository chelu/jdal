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
package info.joseluismartin.gui.table;

import info.joseluismartin.gui.ListTableModel;
import info.joseluismartin.text.FormatUtils;

import java.awt.Component;
import java.util.Locale;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;

import org.springframework.format.Printer;

/**
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 *
 */
public class AnnotationFormatTableCellRenderer extends DefaultTableCellRenderer {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		
		Class<?> clazz = table.getColumnClass(column);
		if (Number.class.isAssignableFrom(clazz)) {
			TableModel tableModel = table.getModel();
			if (tableModel instanceof ListTableModel) {
				ListTableModel listTableModel = (ListTableModel) tableModel;
				Class<?> modelClass = ((ListTableModel) tableModel).getModelClass();
				String propertyName = listTableModel.getPropertyName(column);
				Printer<Object> printer = FormatUtils.getPrinter(modelClass, propertyName);
				if (printer != null) {
					value = printer.print(value, Locale.getDefault());
					JLabel label  = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
					label.setHorizontalAlignment(JLabel.RIGHT);
					return label;
				}
			}
		}
		return table.getDefaultRenderer(clazz)
			.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	}

}
