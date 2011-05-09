/**
 * 
 */
package info.joseluismartin.vaadin.ui;

import com.vaadin.ui.Component;

/**
 * <p>
 * Base class for Views. Subclases must implements buildPanel()  method
 * to create the Vaadin component of the view.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public abstract class AbstractView<T> implements View<T> {
	
	Component component;
	T model;

	/**
	 * Build the Component of view.
	 * @return
	 */
	protected abstract Component buildPanel();


	/**
	 * {@inheritDoc}
	 */
	@Override
	public Component getComponent() {
		if (component == null) 
			component = buildPanel();
		
		return component;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T getModel() {
		return model;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setModel(T model) {
		this.model = model;
	}
}
