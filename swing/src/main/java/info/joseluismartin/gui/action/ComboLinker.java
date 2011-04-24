/**
 * 
 */
package info.joseluismartin.gui.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;

/**
 * Links two combos by a property name
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class ComboLinker implements ActionListener {
	
		private JComboBox primary;
		private JComboBox dependent;
		private String propertyName;

	public ComboLinker(JComboBox primary, JComboBox dependent, String propertyName) {
		this.primary = primary;
		this.dependent = dependent;
		this.propertyName = propertyName;
		
		primary.addActionListener(this);
	}

	/**
	 * {@inheritDoc}
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@SuppressWarnings("unchecked")
	public void actionPerformed(ActionEvent e) {
		Object selected = primary.getSelectedItem();
		if (selected != null) {
			BeanWrapper wrapper = PropertyAccessorFactory.forBeanPropertyAccess(selected);
			Collection collection = (Collection) wrapper.getPropertyValue(propertyName);
			DefaultComboBoxModel model = new DefaultComboBoxModel(new Vector<Object>(collection));
			dependent.setModel(model);
		}
	}
}
