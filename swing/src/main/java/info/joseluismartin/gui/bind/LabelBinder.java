package info.joseluismartin.gui.bind;

import javax.swing.JLabel;

public class LabelBinder extends AbstractBinder {

	public void refresh() {
		JLabel label = (JLabel) component;
		Object value = getValue();
		label.setText(value.toString());
	}

	public void update() {
		// TODO Auto-generated method stub
		
	}

}
