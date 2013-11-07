package org.jdal.vaadin.ui;

import org.jdal.ui.Editor;
import org.jdal.ui.View;

import com.vaadin.ui.Component;

/**
 * Interface for Vaadin Views
 * 
 * @author Jose Luis Martin
 * @param <T> Model backend by this View
 */
public interface VaadinView<T> extends View<T>, Editor<T> {
	
	public Component getPanel();

	public int getHeight();
	
	public int getWidth();

}
