/*
 * Copyright 2008-2010 the original author or authors.
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
package org.jdal.auth;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Servlet 2.3 Filter  for simple session auth
 * 
 * @author Jose Luis Martin.
 * @since 1.0
 */
public class AuthFilter extends OncePerRequestFilter {
	
	public static String SESSION_USER_KEY = "AUTHFILTER_USER";
	private static String loginPage="/login.jsp";

	public void destroy() {

	}

	@Override
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		
		if (!isLoginPage(req) && req.getSession().getAttribute(SESSION_USER_KEY) == null) {
			// not authorized
			res.sendRedirect(req.getContextPath() + "/" + loginPage);
		}
		else {
			// authorized
			chain.doFilter(request, response);
		}
		
	}
	
	protected boolean isLoginPage(HttpServletRequest request) {
		return  ("/login.jsp".equals(request.getServletPath()) || 
				 "/login.do".equals(request.getServletPath()));
	}

}
