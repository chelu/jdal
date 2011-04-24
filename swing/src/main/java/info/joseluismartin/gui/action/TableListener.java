package info.joseluismartin.gui.action;

import info.joseluismartin.gui.ListTableModel;
import info.joseluismartin.gui.TableRowAction;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTable;

/**
 * Listen clicks on table with TableModelListener and 
 * execute RowActions on actions columns.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class TableListener extends MouseAdapter {

	private JTable table;
	
	public TableListener(JTable table) {
		this.table = table;
		table.addMouseListener(this);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		Point point = e.getPoint();
		int row = table.rowAtPoint(point);
		int col = table.columnAtPoint(point);
		ListTableModel tableModel = (ListTableModel) table.getModel();
		
		// check Actions
		if (col != -1 && row != -1 && tableModel.isActionColumn(col)) {
			TableRowAction action = (TableRowAction) tableModel.getValueAt(row, col);
			action.setTableModel(tableModel);
			action.setRow(tableModel.getList().get(row));
			action.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "clicked"));
			
		}
	}
}