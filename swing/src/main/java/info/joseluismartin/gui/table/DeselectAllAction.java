package info.joseluismartin.gui.table;

import info.joseluismartin.gui.form.FormUtils;

import java.awt.event.ActionEvent;

/**
 * Deselect all records in Table
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class DeselectAllAction extends TablePanelAction {
	private static final long serialVersionUID = 1L;

	public static final String DEFAULT_ICON = "/images/table/22x22/uncheckbox.png";
	
	public void init() {
		setIcon(FormUtils.getIcon(getIcon(), DEFAULT_ICON));
	}

	public void actionPerformed(ActionEvent e) {
		getTablePanel().getTable().getTableModel().uncheckAll();
	}
}
