package info.joseluismartin.gui.table;

import info.joseluismartin.gui.form.FormUtils;

import java.awt.event.ActionEvent;

import javax.swing.JDialog;

/**
 * Default Add Action for TablePanel that use configured TablePanel editor to add an Object.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class AddAction extends TablePanelAction {

	private static final long serialVersionUID = 1L;
	private static final String DEFAULT_ICON = "/images/table/22x22/document-new.png";
	
	public void init() {
		setIcon(FormUtils.getIcon(getIcon(), DEFAULT_ICON));
	}

	public void actionPerformed(ActionEvent e) {
		JDialog dlg = getTablePanel().getDialog();
		dlg.pack();
		dlg.setModal(true);
		dlg.setVisible(true);
		getTablePanel().refresh();
	}
}
