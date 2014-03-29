package org.jdal.vaadin;

import org.springframework.context.ApplicationContext;

import com.vaadin.server.DefaultUIProvider;
import com.vaadin.server.UICreateEvent;
import com.vaadin.ui.UI;
import com.vaadin.util.CurrentInstance;

public class SpringUIProvider extends DefaultUIProvider {

	@Override
	public UI createInstance(UICreateEvent event) {
		ApplicationContext ctx = VaadinUtils.getApplicationContext();
		CurrentInstance.set(UIid.class, new UIid(event.getUiId()));
		UI ui =  ctx.getBean(event.getUIClass());
		CurrentInstance.set(UIid.class, null);
		
		return ui;
	}
}
