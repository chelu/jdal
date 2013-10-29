/*
 * Copyright 2009-2012 the original author or authors.
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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdal.vaadin.beans.VaadinScope;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.vaadin.server.LegacyApplication;
import com.vaadin.server.LegacyVaadinServlet;
import com.vaadin.server.VaadinServlet;

/**
 * Vaadin Servlet.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 *
 */
public class ApplicationServlet extends LegacyVaadinServlet {
	
	public static final String SESSION_LISTENER_NAME = "VAADIN_SCOPE_SESSION_LISTENER";
	private static final Log log = LogFactory.getLog(VaadinServlet.class);
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected LegacyApplication getNewApplication(HttpServletRequest request) throws ServletException {
		// try from context 
		WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(request.getSession()
				.getServletContext());
		
		ConfigurableListableBeanFactory beanFactory = ((AbstractApplicationContext) wac).getBeanFactory();
		
		LegacyApplication	app = super.getNewApplication(request);
			
		if (app == null)
				throw new ServletException("Can't instantiate Vaadin Application");
			
			
		beanFactory.autowireBean(app);
		
		// register a session listener to shut down scoped beans.
		if (request.getSession().getAttribute(SESSION_LISTENER_NAME) == null) {
			if (log.isDebugEnabled())
				log.debug("Registering a SessionListener for session [" + request.getSession().getId() + "]");
			request.getSession().setAttribute(SESSION_LISTENER_NAME, new SessionListener(beanFactory));
		}
		
		return app;
	}
	
}

class SessionListener implements HttpSessionBindingListener {

	private VaadinScope scope;
		
	/**
	 * @param beanFactory
	 */
	public SessionListener(ConfigurableListableBeanFactory beanFactory) {
		this.scope = (VaadinScope) beanFactory.getRegisteredScope(VaadinScope.SCOPE_NAME);
	}

	/**
	 * {@inheritDoc}
	 */
	public void valueBound(HttpSessionBindingEvent event) {
		// not interested
	}

	/**
	 * {@inheritDoc}
	 */
	public void valueUnbound(HttpSessionBindingEvent event) {
		scope.onSessionClose(event.getSession());
	}
	
}
