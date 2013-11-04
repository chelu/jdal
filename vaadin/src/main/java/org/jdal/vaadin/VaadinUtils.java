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

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.vaadin.server.LegacyApplication;

/**
 * Easy access to vaadin or servlet classes.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 * @since 2.0
 */
public abstract class VaadinUtils {

	private static final Log log = LogFactory.getLog(VaadinUtils.class);
	
	public static HttpServletRequest getRequest() {
		return (HttpServletRequest) RequestContextHolder.getRequestAttributes()
				.resolveReference(RequestAttributes.REFERENCE_REQUEST);
	}
	
	public static HttpSession getSession() {
		return (HttpSession) RequestContextHolder.getRequestAttributes()
				.resolveReference(RequestAttributes.REFERENCE_SESSION);
	}
	
	
	public static ServletContext getServletContext() {
		return getSession().getServletContext();
	}
	
	public static String getResourceAsString(String relativePath) {
	    String realPath = getServletContext().getRealPath(relativePath);
        if (realPath == null) {
            return null;
        }
        
        File file =  new File(realPath);
        String content =  null;
        
        try {
			content = FileUtils.readFileToString(file);
		} catch (IOException e) {
			log.error(e);
		}
        
        return content;
	}

	public static ApplicationContext getApplicationContext() {
		return WebApplicationContextUtils.getWebApplicationContext(getServletContext());
	}
}
