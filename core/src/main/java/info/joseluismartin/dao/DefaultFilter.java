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
package info.joseluismartin.dao;

import java.util.HashMap;
import java.util.Map;

/**
 * Default implementation of Filter that use a Map to store filter values.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class DefaultFilter implements Filter {
	
	private String name;
	private Map<String, Object> parameterMap = new HashMap<String, Object>();

	/**
	 * {@inheritDoc}
	 * @see info.joseluismartin.dao.Filter#getFilterName()
	 */
	public String getFilterName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * {@inheritDoc}
	 * @see info.joseluismartin.dao.Filter#getParameterMap()
	 */
	public Map<String, Object> getParameterMap() {
		return parameterMap;
	}


	public void setParameteterMap(Map<String, Object> parameterMap) {
		this.parameterMap = parameterMap;
	}

	public Object put(String key, Object value) {
		return parameterMap.put(key, value);
	}

}
