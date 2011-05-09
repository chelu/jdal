/**
 * 
 */
package info.joseluismartin.vaadin.ui;

import info.joseluismartin.gui.Binder;

import com.vaadin.ui.Component;

/**
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 *
 */
public interface View<T> extends Binder<T> {
	
	/**
	 * Get the View Component
	 * @return the view Component
	 */
	Component getComponent();
}
