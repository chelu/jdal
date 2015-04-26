/*
 * Copyright 2009-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jdal.vaadin;

import org.springframework.context.ApplicationContext;

import com.vaadin.server.DefaultUIProvider;
import com.vaadin.server.UIClassSelectionEvent;
import com.vaadin.server.UICreateEvent;
import com.vaadin.server.UIProvider;
import com.vaadin.ui.UI;
import com.vaadin.util.CurrentInstance;

/**
 * {@link UIProvider} implementation that look for UIs in a {@link ApplicationContext}.
 * 
 * @author Jose Luis Martin
 * @since 2.0
 */
public class SpringUIProvider extends DefaultUIProvider {
	
	private UiRequestMapping uiMapping;

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
		checkUiRequestMapping();
		Class<?extends UI> clazz = this.uiMapping.getUiClass(event.getRequest());
		
		return clazz != null ? clazz : super.getUIClass(event);
	}

	private void checkUiRequestMapping() {
		if (this.uiMapping != null)
			return;
		
		ApplicationContext ctx = VaadinUtils.getApplicationContext();
		String names[] = ctx.getBeanNamesForType(UiRequestMapping.class);
		if (names.length > 0) {
			this.uiMapping = ctx.getBean(names[0], UiRequestMapping.class);
		}
		else {
			this.uiMapping = new UrlBeanNameUiMapping();
			((UrlBeanNameUiMapping) this.uiMapping).init(ctx);
		}
	}

	/**
	 * @return the uiMapping
	 */
	public UiRequestMapping getUiMapping() {
		return uiMapping;
	}

	/**
	 * @param uiMapping the uiMapping to set
	 */
	public void setUiMapping(UiRequestMapping uiMapping) {
		this.uiMapping = uiMapping;
	}
	
	
}
