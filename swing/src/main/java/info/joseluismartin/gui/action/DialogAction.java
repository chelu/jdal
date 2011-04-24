package info.joseluismartin.gui.action;

import info.joseluismartin.gui.IconAction;

import java.awt.Window;

public abstract class DialogAction extends IconAction {

	private static final long serialVersionUID = 1L;
	private Window dialog;

	
	public Window getDialog() {
		return dialog;
	}

	public void setDialog(Window dialog) {
		this.dialog = dialog;
	}
}
