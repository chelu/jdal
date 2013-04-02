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

import info.joseluismartin.beans.StaticMessageSource;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

/**
 * TableCellEditor that show a JColorChooser.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */

public class ColorTableCellEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {

	private static final long serialVersionUID = 1L;
	private Color currentColor;
    private JButton button;
    private JColorChooser colorChooser;
    private JDialog dialog;
    protected static final String EDIT = "edit";

	 public ColorTableCellEditor() {
	        button = new JButton();
	        button.setActionCommand(EDIT);
	        button.addActionListener(this);
	        button.setBorderPainted(false);

	        //Set up the dialog that the button brings up.
	        colorChooser = new JColorChooser();
	        dialog = JColorChooser.createDialog(button,
	                                        StaticMessageSource.getMessage("ColorChooser.chooseColor"),
	                                        true,  //modal
	                                        colorChooser,
	                                        this,  //OK button handler
	                                        null); //no CANCEL button handler
	    }

	    public void actionPerformed(ActionEvent e) {
	        if (EDIT.equals(e.getActionCommand())) {
	            //The user has clicked the cell, so
	            //bring up the dialog.
	            button.setBackground(currentColor);
	            colorChooser.setColor(currentColor);
	            dialog.setVisible(true);

	            fireEditingStopped(); //Make the renderer reappear.

	        } else { //User pressed dialog's "OK" button.
	            currentColor = colorChooser.getColor();
	        }
	    }

	    public Object getCellEditorValue() {
	        return currentColor;
	    }

	    public Component getTableCellEditorComponent(JTable table,
	                                                 Object value,
	                                                 boolean isSelected,
	                                                 int row,
	                                                 int column) {
	        currentColor = (Color)value;
	        return button;
	    }


}
