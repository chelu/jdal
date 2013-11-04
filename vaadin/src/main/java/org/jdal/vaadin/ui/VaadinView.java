package org.jdal.vaadin.ui;

import org.jdal.ui.View;

import com.vaadin.ui.Component;

public interface VaadinView<T> extends View<T> {
	
	public Component getPanel();

}
