package info.joseluismartin.gui.table;

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
	                                        "Elija un color",
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
