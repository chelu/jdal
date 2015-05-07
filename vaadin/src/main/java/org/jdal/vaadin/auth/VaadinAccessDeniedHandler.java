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
package org.jdal.vaadin.auth;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;

import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.shared.ApplicationConstants;
import com.vaadin.ui.Notification;

/**
 * {@link AccessDeniedHandler} implementation taking care about UIDL request.
 * 
 * @author Jose Luis Martin
 * @since 2.1
 */
public class VaadinAccessDeniedHandler implements AccessDeniedHandler {
	
	private AccessDeniedHandlerImpl defaultHandler = new AccessDeniedHandlerImpl();
	private String errorView;
	private String errorMessage;
	
	@Override
	public void handle(HttpServletRequest request,
			HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException,
			ServletException {
		
		if (isUidlRequest()) {
			String error = errorMessage != null ? errorMessage : accessDeniedException.getMessage();
			Notification.show(error, Notification.Type.ERROR_MESSAGE);
			return;
		}
		
		this.defaultHandler.handle(request, response, accessDeniedException);
	}
	
	/**
	 * Test if current request is an UIDL request
	 * @return true if in UIDL request, false otherwise
	 */
	private boolean isUidlRequest() {
		VaadinRequest request = VaadinService.getCurrentRequest();
		
		if (request == null)
			return false;
		
		 String pathInfo = request.getPathInfo();

		 if (pathInfo == null) {
	            return false;
		 }
		 
		 if (pathInfo.startsWith("/" + ApplicationConstants.UIDL_PATH)) {
	            return true;
	        }

	        return false;
	}

	/**
	 * @return the errorView
	 */
	public String getErrorView() {
		return errorView;
	}

	/**
	 * @param errorView the errorView to set
	 */
	public void setErrorView(String errorView) {
		this.errorView = errorView;
	}


	/**
	 * @param errorPage the errorPage to set
	 */
	public void setErrorPage(String errorPage) {
		this.defaultHandler.setErrorPage(errorPage);
	}
	
	/**
	 * @return the errorMessage
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * @param errorMessage the errorMessage to set
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}
