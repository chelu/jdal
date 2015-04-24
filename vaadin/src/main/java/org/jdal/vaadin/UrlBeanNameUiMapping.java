/*
 * Copyright 2009-2014 the original author or authors.
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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdal.vaadin.annotation.UiMapping;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;

import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;

/**
 * UiMaaping implementation that look for UIs by bean name.
 * 
 * @author Jose Luis Martin
 * @since 2.1
 */
public class UrlBeanNameUiMapping implements UiRequestMapping {
	
	private static final Log log = LogFactory.getLog(UrlBeanNameUiMapping.class);
	/** translate paths to bean names */
	private Map<String, String> urlMap;

	
	@Override
	public UI getUi(VaadinRequest request) {
		ApplicationContext ctx = VaadinUtils.getApplicationContext();
	
		if (this.urlMap == null)
			initUrlMap(ctx);
		
		String beanName = urlMap.get(request.getPathInfo());
		
		if (beanName == null)
			beanName = request.getPathInfo();
		
		if (beanName != null && ctx.containsBean(beanName)) 
			return VaadinUtils.getApplicationContext().getBean(beanName, UI.class);
		
		return null;
	}


	protected void initUrlMap(ApplicationContext ctx) {
		this.urlMap = new ConcurrentHashMap<String, String>();
		Map<String, Object> uis = ctx.getBeansWithAnnotation(UiMapping.class);
		
		for (String name : uis.keySet()) {
			Object ui = uis.get(name);
			if (ui instanceof UI) {
				UiMapping ann = AnnotationUtils.findAnnotation(ui.getClass(), UiMapping.class);
				if (ann != null) {
					if (log.isDebugEnabled())
						log.debug("Mapping UI [" + ui.getClass().getName() + "] to request path [" + ann.value() + "]");
					this.urlMap.put(ann.value(), name);
				}
				
			}
		}
	}

}
