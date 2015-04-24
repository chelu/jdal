package org.jdal.vaadin;

import org.springframework.context.ApplicationContext;

import com.vaadin.server.DefaultUIProvider;
import com.vaadin.server.UIClassSelectionEvent;
import com.vaadin.server.UICreateEvent;
import com.vaadin.ui.UI;
import com.vaadin.util.CurrentInstance;

public class SpringUIProvider extends DefaultUIProvider {
	
	private UiRequestMapping uiMapping = new UrlBeanNameUiMapping();

	@Override
	public UI createInstance(UICreateEvent event) {
		ApplicationContext ctx = VaadinUtils.getApplicationContext();
		CurrentInstance.set(UIid.class, new UIid(event.getUiId()));
		UI ui = this.uiMapping.getUi(event.getRequest());
		
		if (ui == null)
			ui =  ctx.getBean(event.getUIClass());
		
		CurrentInstance.set(UIid.class, null);
		
		return ui;
	}

	@Override
	public Class<? extends UI> getUIClass(UIClassSelectionEvent event) {
		UI ui = this.uiMapping.getUi(event.getRequest());
		
		return ui != null ? ui.getClass() : super.getUIClass(event);
	}
	
	
}
