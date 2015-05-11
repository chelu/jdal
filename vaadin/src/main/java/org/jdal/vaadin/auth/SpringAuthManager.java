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


import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdal.auth.AuthService;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import com.vaadin.server.VaadinServletService;
import com.vaadin.server.VaadinSession;

/**
 * Adapter to use Spring security {@link AuthenticationManager} as {@link AuthService}.
 * 
 * @author Jose Luis Martin
 * @since 2.1
 */
public class SpringAuthManager implements AuthService {
    
	private static final Log log = LogFactory.getLog(SpringAuthManager.class);
	
	private AuthenticationManager authenticationManager;
	private SessionAuthenticationStrategy sessionStrategy;
	private AccessDecisionManager accessDecisionManager;
	
	@PostConstruct
	public void init() {
		if (this.accessDecisionManager == null) {
			if (log.isDebugEnabled())
				log.debug("Creating default AffirmativeBased AccesDecisionManager with RoleVoter");
			
			List<AccessDecisionVoter<? extends Object>> defaultVoters =
					new ArrayList<AccessDecisionVoter<? extends Object>>();
			defaultVoters.add(new RoleVoter());
			this.accessDecisionManager = new AffirmativeBased(defaultVoters);
		}
	}
	
	@Override
	public boolean validate(String username, String password) {
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
		try {
			Authentication auth = this.authenticationManager.authenticate(token);
			if (auth.isAuthenticated()) {
				// execute session authentication strategy
				if (this.sessionStrategy != null)
					this.sessionStrategy.onAuthentication(auth, VaadinServletService.getCurrentServletRequest(),
							VaadinServletService.getCurrentResponse());
				SecurityContextHolder.getContext().setAuthentication(auth);
				// save request in context session
				VaadinSession.getCurrent().getSession().setAttribute(
						HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
						SecurityContextHolder.getContext());
				
				return  true;
			}
			SecurityContextHolder.clearContext();
			return false;
		}
		catch(AuthenticationException ae) {
			SecurityContextHolder.clearContext();
			return false;
		}
	}
	
	@Override
	public boolean checkAccess(Object target, Object expression, Object principal) {
		List<ConfigAttribute> configAttributes = createConfigAttributes(expression);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		try {
			this.accessDecisionManager.decide(authentication, target, configAttributes);
			return true;
		}
		catch(Exception e) {
			if (log.isDebugEnabled()) {
				log.debug("AccessDecisionManager return: " + e.getMessage());
			}
			
			return false;
		}
	}

	/**
	 * @param expression
	 * @return
	 */
	private List<ConfigAttribute> createConfigAttributes(final Object expression) {
		List<ConfigAttribute> configAttributes = new ArrayList<ConfigAttribute>();
		configAttributes.add(new ConfigAttribute() {
			
			@Override
			public String getAttribute() {
				return (String) expression;
			}
		});
		
		return configAttributes;
	}

	/**
	 * @return the authenticationManager
	 */
	public AuthenticationManager getAuthenticationManager() {
		return authenticationManager;
	}

	/**
	 * @param authenticationManager the authenticationManager to set
	 */
	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	/**
	 * @return the sessionStrategy
	 */
	public SessionAuthenticationStrategy getSessionStrategy() {
		return sessionStrategy;
	}

	/**
	 * @param sessionStrategy the sessionStrategy to set
	 */
	public void setSessionStrategy(SessionAuthenticationStrategy sessionStrategy) {
		this.sessionStrategy = sessionStrategy;
	}
	
	/**
	 * @return the accessDecisionManager
	 */
	public AccessDecisionManager getAccessDecisionManager() {
		return accessDecisionManager;
	}

	/**
	 * @param accessDecisionManager the accessDecisionManager to set
	 */
	public void setAccessDecisionManager(AccessDecisionManager accessDecisionManager) {
		this.accessDecisionManager = accessDecisionManager;
	}

}
