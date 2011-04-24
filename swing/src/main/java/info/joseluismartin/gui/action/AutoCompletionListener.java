/**
 * 
 */
package info.joseluismartin.gui.action;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.ComboBoxEditor;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTextField;

/**
 * JComboBox Listener that refresh the list of items based on text that is typed on
 * the ComboBoxEditor. Implement abstract getList() to provide the new item list.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public abstract class AutoCompletionListener extends KeyAdapter  {
	
	private JComboBox combo;
	
	public AutoCompletionListener() {}

	/**
	 * @param street
	 */
	public AutoCompletionListener(JComboBox combo) {
		this.combo = combo;
		combo.setEditable(true);
		combo.getEditor().getEditorComponent().addKeyListener(this);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		char ch = e.getKeyChar();
		if (ch == KeyEvent.CHAR_UNDEFINED || Character.isISOControl(ch))
			return;
		
		ComboBoxEditor editor = (ComboBoxEditor) combo.getEditor();
		String editing =  ((JTextField) editor.getEditorComponent()).getText();
		combo.removeAllItems();
		addList(getList(editing));
		combo.setPopupVisible(true);
		((JTextField) editor.getEditorComponent()).setText(editing);
		combo.repaint();
	}

	/**
	 * @param model
	 * @param list
	 */
	private void addList(List<?> list) {
		ComboBoxModel model = new DefaultComboBoxModel(list.toArray());
		model.setSelectedItem(null);
		combo.setModel(model);
	}

	/**
	 * @param editing String
	 * @return a List of objects with match editing string
	 */
	protected abstract List<?> getList(String editing);

	
	public JComboBox getCombo() {
		return combo;
	}

	public void setCombo(JComboBox combo) {
		this.combo = combo;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		char ch = e.getKeyChar();
		if (ch == KeyEvent.CHAR_UNDEFINED || Character.isISOControl(ch))
			return;
		
		if (combo.getSelectedItem() !=  null) {
			combo.setSelectedItem(null);
			combo.getEditor().setItem(null);
		}
	}	

}
