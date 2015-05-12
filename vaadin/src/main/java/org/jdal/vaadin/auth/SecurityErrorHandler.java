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

import org.springframework.security.access.AccessDeniedException;

import com.vaadin.server.DefaultErrorHandler;
import com.vaadin.server.ErrorEvent;
import com.vaadin.server.ErrorHandler;
import com.vaadin.ui.Notification;

/**
 * {@link ErrorHandler} thats take care about Spring Security Exceptions.
 * 
 * @author Jose Luis Martin
 * @since 2.1
 */
public class SecurityErrorHandler extends DefaultErrorHandler {

	@Override
	public void error(ErrorEvent event) {
		Throwable t = findRelevantThrowable(event.getThrowable());
		if (isSecurityException(t)) {
			handleSecurityException(t);
			
			return;
		}
			
		super.error(event);
	}

	/**
	 * Show a notification with error
	 * @param event 
	 */
	protected void handleSecurityException(Throwable exception) {
		Notification.show(exception.getMessage(), Notification.Type.ERROR_MESSAGE);
	}

	/**
	 * @param thowable throwable to test
	 * @return true if is an access denied exception
	 */
	protected boolean isSecurityException(Throwable thowable) {
		return (thowable instanceof AccessDeniedException);
	}

	
	
}
