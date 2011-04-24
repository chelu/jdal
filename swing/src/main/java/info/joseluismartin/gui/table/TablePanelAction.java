package info.joseluismartin.gui.table;

import info.joseluismartin.gui.action.BeanAction;

/**
 * Base class for TablePanel Actions
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public abstract class TablePanelAction extends BeanAction {

	private static final long serialVersionUID = 1L;
	private TablePanel tablePanel;
	
	
	/**
	 * @return the tablePanel
	 */
	public TablePanel getTablePanel() {
		return tablePanel;
	}
	/**
	 * @param tablePanel the tablePanel to set
	 */
	public void setTablePanel(TablePanel tablePanel) {
		this.tablePanel = tablePanel;
	}
	
	
}
