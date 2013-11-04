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
package org.jdal.dao;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;

/**
 * Base Filter for Bean Filters
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class BeanFilter implements Filter, Serializable {
	
	private static final Log log = LogFactory.getLog(BeanFilter.class);
	private String filterName;
	private List<String> ignoredProperties = new ArrayList<String>();
	private static final String PARAMETER_MAP = "parameterMap";
	// FIXME: possible colision if a paramert have this name
	private static final String FILTER_NAME = "filterName";

	
	public BeanFilter() {
		this.filterName = StringUtils.uncapitalize(this.getClass().getSimpleName());
		init();
	}
	
	public BeanFilter(String filterName) {
		this.filterName = filterName;
		init();
	}	

	private void init() {
		ignoredProperties.add(PARAMETER_MAP);
		ignoredProperties.add(FILTER_NAME);
		ignoredProperties.add("class");
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Map<String, Object> getParameterMap() {
		PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(getClass());
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		for (PropertyDescriptor pd : pds) {
			if (!ignoredProperties.contains(pd.getName()))
				try {
					map.put(pd.getName(), pd.getReadMethod().invoke(this, (Object[]) null));
				} catch (Exception e) {
					log.error(e);
				}
		}
		return map;	
	}

	/**
	 * {@inheritDoc}
	 */
	public void clear() {
		PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(getClass());
		
		for (PropertyDescriptor pd: pds) {
			if (!ignoredProperties.contains(pd.getName())) {
				try {
					pd.getWriteMethod().invoke(this, (Object) null);
				}
				catch (Exception e) {
					log.error("Error nullifing property: [" + pd.getName() + "]", e);
				}
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getFilterName() {
		return filterName;
	}
	
	/**
	 * @param name the name to set
	 */
	public void setFilterName(String name) {
		this.filterName = name;
	}
	


}
