package org.jdal.vaadin;

import org.springframework.context.ApplicationContext;

import com.vaadin.server.UIClassSelectionEvent;
import com.vaadin.server.UICreateEvent;
import com.vaadin.server.UIProvider;
import com.vaadin.ui.UI;

public class SpringUIProvider extends UIProvider {

	@Override
	public UI createInstance(UICreateEvent event) {
		ApplicationContext ctx = VaadinUtils.getApplicationContext();	
		return ctx.getBean(UI.class);
	}

	@Override
	public Class<? extends UI> getUIClass(UIClassSelectionEvent event) {
		return UI.class;
	}

}
