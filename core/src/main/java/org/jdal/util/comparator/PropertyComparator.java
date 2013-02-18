/*
 * Copyright 2002-2010 the original author or authors.
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
package org.jdal.util.comparator;

import java.util.Comparator;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Comparator for generic beans by property value.
 * is an alphaNumeric comparator. When two string have numbers
 * and have same prefix, use numeric comparation so test_10 > than test_2 
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class PropertyComparator implements Comparator<Object> {
	/** common logs */
	private static Log log = LogFactory.getLog(PropertyComparator.class);
	/** property name */
	private String name;
	/** alpha numeric comparator */
	private Comparator<String> comparator = new AlphaNumericComparator();
	
	/** ctor 
	 * @param name property name
	 */
	public PropertyComparator(String name) {
		this.name = name;
	}
	
	/** 
	 * @param o1 one bean
	 * @param o2 another bean
	 * @return see the see
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Object o1, Object o2) {
		try {
			String value1 = (String) BeanUtils.getProperty(o1, name).trim();
			String value2;
			value2 = (String) BeanUtils.getProperty(o2, name).trim();
			
			return comparator.compare(value1, value2);
		} 
		catch (Exception e) {
			log.error(e);
		}
		
		return 0;
	}

	/**
	 * @return the comparator
	 */
	public Comparator<String> getComparator() {
		return comparator;
	}

	/**
	 * @param comparator the comparator to set
	 */
	public void setComparator(Comparator<String> comparator) {
		this.comparator = comparator;
	}
}
