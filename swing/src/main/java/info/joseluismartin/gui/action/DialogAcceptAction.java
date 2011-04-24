package info.joseluismartin.gui.action;

import info.joseluismartin.gui.ViewDialog;

import java.awt.event.ActionEvent;

public class DialogAcceptAction extends ViewAction {

	private static final long serialVersionUID = 1L;

	public void actionPerformed(ActionEvent e) {
		if (getDialog() instanceof ViewDialog) {
			((ViewDialog) getDialog()).setValue(ViewDialog.OK);
		}
		
		getDialog().dispose();
	}

}
