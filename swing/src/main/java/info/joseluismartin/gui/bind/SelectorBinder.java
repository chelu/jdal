/**
 * 
 */
package info.joseluismartin.gui.bind;

import info.joseluismartin.gui.Selector;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Binder for Selector
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class SelectorBinder extends AbstractBinder implements ActionListener{

	/**
	 * {@inheritDoc}
	 * @see info.joseluismartin.gui.bind.AbstractBinder#doBind(java.awt.Component)
	 */
	protected void doBind(Object component) {
		Selector<?> selector = (Selector<?>) component;
		selector.addActionListener(this);
		
	}

	/**
	 * {@inheritDoc}
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
	
	}

	@SuppressWarnings("unchecked")
	public void doRefresh() {
		List values = new ArrayList((Collection) getValue());
		Selector selector = (Selector) component;
		selector.setSelected(values);
		
	}

	public void doUpdate() {
		Selector<?> selector = (Selector<?>) component;
		setValue(selector.getSelected());
		
	}

}
