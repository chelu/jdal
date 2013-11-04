/**
 * 
 */
package org.jdal.swing.bind;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jdal.swing.Selector;
import org.jdal.ui.bind.AbstractBinder;

/**
 * Binder for Selector
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class SelectorBinder extends AbstractBinder implements ActionListener {

	
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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void doRefresh() {
		Collection c  = (Collection) getValue();
		ArrayList values = new ArrayList();
		
		if (c != null)
			values.addAll(c);
		
		Selector selector = (Selector) component;
		selector.setSelected(values);
		
	}

	public void doUpdate() {
		Selector<?> selector = (Selector<?>) component;
		setValue(selector.getSelected());
		
	}

}
