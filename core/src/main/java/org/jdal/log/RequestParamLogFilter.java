/*
 * Copyright 2008-2011 the original author or authors.
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
package org.jdal.log;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

/**
 * Servlet 2.3 Filter that log request params
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
@SuppressWarnings("rawtypes")
public class RequestParamLogFilter extends CommonsRequestLoggingFilter {
	
	private static Log log = LogFactory.getLog(RequestParamLogFilter.class);
	
	@Override
	public void beforeRequest(HttpServletRequest request, String message) {
		super.beforeRequest(request, message);
		Map parameters = request.getParameterMap();

		if (log.isDebugEnabled())
			log.debug("Request Parameters:\n " + getParameterString(parameters));
	}

	/**
	 * Build String from parameter map
	 * @param parameters
	 * @return
	 */
	private String getParameterString(Map parameters) {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
			for (Object key : parameters.keySet()) {
				sb.append(key);
				sb.append("=");
				sb.append(getObjectString(parameters.get(key)));
				sb.append(",");
			}
		
			String mapString = sb.toString();

			if (parameters.size() > 0)
				mapString = mapString.substring(0, mapString.length() - 1);
			
			return mapString + "}";
	}

	/** 
	 * Build String from String[] 
	 * @param object
	 * @return
	 */
	private String getObjectString(Object object) {
		StringBuilder sb = new StringBuilder();
		if (object instanceof String) {
			return (String) object;
		}
		else if (object instanceof String[]) {
			String[] objs =  (String[]) object;
			sb.append("[");
			for (int i = 0; i < objs.length ; i++) {
				sb.append(objs[i]);
				if (i < (objs.length - 1))
					sb.append(",");
			}
			sb.append("]");
		}
		
		return sb.toString();
	}

}
