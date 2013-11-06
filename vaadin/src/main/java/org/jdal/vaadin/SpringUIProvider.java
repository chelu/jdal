package org.jdal.vaadin;

import org.springframework.context.ApplicationContext;

import com.vaadin.server.DefaultUIProvider;
import com.vaadin.server.UIClassSelectionEvent;
import com.vaadin.server.UICreateEvent;
import com.vaadin.ui.UI;

public class SpringUIProvider extends DefaultUIProvider {

	@Override
	public UI createInstance(UICreateEvent event) {
		ApplicationContext ctx = VaadinUtils.getApplicationContext();
		
		return ctx.getBean(event.getUIClass());
	}
}
