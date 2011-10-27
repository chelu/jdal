package info.joseluismartin.gui.render;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;

/**
 * Boolean Editor from JTable. JTable by default not handle boolean primitive values
 *
 */
public class BooleanEditor extends DefaultCellEditor {
	public BooleanEditor() {
		super(new JCheckBox());
		JCheckBox checkBox = (JCheckBox) getComponent();
		checkBox.setHorizontalAlignment(JCheckBox.CENTER);
	}
}

