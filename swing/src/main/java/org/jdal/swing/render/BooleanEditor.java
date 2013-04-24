package org.jdal.swing.render;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;

/**
 * Boolean Editor for JTable. JTable by default not handle boolean primitive values
 *
 */
public class BooleanEditor extends DefaultCellEditor {
	public BooleanEditor() {
		super(new JCheckBox());
		JCheckBox checkBox = (JCheckBox) getComponent();
		checkBox.setHorizontalAlignment(JCheckBox.CENTER);
	}
}

