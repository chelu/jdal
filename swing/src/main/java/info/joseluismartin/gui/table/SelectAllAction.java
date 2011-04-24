package info.joseluismartin.gui.table;

import info.joseluismartin.gui.form.FormUtils;

import java.awt.event.ActionEvent;

/**
 * Default Action to select All checks in pageable table
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class SelectAllAction extends TablePanelAction {

	private static final long serialVersionUID = 1L;
	public static final String DEFAULT_ICON = "/images/table/22x22/checkbox.png";
	
	public void init() {
		setIcon(FormUtils.getIcon(getIcon(), DEFAULT_ICON));
	}

	public void actionPerformed(ActionEvent e) {
		getTablePanel().selectAll();
	}
}
