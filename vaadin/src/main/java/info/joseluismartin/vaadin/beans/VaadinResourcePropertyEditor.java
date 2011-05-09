package info.joseluismartin.vaadin.beans;

import java.beans.PropertyEditorSupport;

import com.vaadin.terminal.ThemeResource;


public class VaadinResourcePropertyEditor extends PropertyEditorSupport {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		setValue(new ThemeResource(text));
	}
		
}
