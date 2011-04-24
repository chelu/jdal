package info.joseluismartin.gui.table;

import info.joseluismartin.gui.PageableTable;
import info.joseluismartin.gui.form.FormUtils;

import java.awt.event.ActionEvent;

public class ClearFilterAction extends TablePanelAction {
	
	public static final String DEFAULT_ICON = "/images/table/22x22/edit-clear.png";
	private static final long serialVersionUID = 1L;
	
	public void init() {
		setIcon(FormUtils.getIcon(getIcon(), DEFAULT_ICON));
	}

	public void actionPerformed(ActionEvent arg0) {
		PageableTable table = getTablePanel().getTable();
		getTablePanel().getFilterView().clear();
		table.setFilter(null);
		table.refresh();
	}
	
}