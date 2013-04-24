package org.jdal.swing.bind;

import javax.swing.JLabel;

import org.jdal.ui.bind.AbstractBinder;

public class LabelBinder extends AbstractBinder {

	public void  doRefresh() {
		JLabel label = (JLabel) component;
		Object value = getValue();
		label.setText(value.toString());
	}

	public void doUpdate() {

	}

}
