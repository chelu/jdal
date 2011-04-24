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
package info.joseluismartin.util.comparator;

import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
			String value2 = (String) BeanUtils.getProperty(o2, name).trim();
			Pattern pattern = Pattern.compile("([0-9]+)"); 
			Matcher matcher1 = pattern.matcher(value1);
			Matcher matcher2 = pattern.matcher(value2);
			
			if ((matcher1.find() && matcher2.find()) &&
				value1.startsWith(value2.substring(0, matcher2.start()))) {
				// two strings have numbers, and same prefix.
				// use numeric comparation
				Integer n1 = Integer.valueOf(matcher1.group());
				Integer n2 = Integer.valueOf(matcher2.group());
				return n1.compareTo(n2);
			}
			// else return string comparation
			return value1.compareToIgnoreCase(value2);
		} catch (Exception e) {
			log.error(e);
		}
		return 0; // On Exception object are equals
	}
}
