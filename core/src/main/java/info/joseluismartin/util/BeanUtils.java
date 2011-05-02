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
package info.joseluismartin.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.InvalidPropertyException;
import org.springframework.beans.PropertyAccessException;
import org.springframework.beans.PropertyValue;

/**
 * Some static funtions added to spring BeanUtils
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public abstract class BeanUtils extends org.springframework.beans.BeanUtils {

	/**
	 * Class log
	 */
	private static Log log = LogFactory.getLog(BeanUtils.class);
	
	/**
	 * Excluded properties
	 */
	private static final String[] EXCLUDED_PROPERTIES = {"class"};
	
	/**
	 * Get PropertyValues from Object
	 * @param obj Object to get PropertyValues
	 * @return the property values
	 */
	@SuppressWarnings("unchecked")
	public static PropertyValue[] getPropertyValues(Object obj) {
		
		PropertyDescriptor[] pds = getPropertyDescriptors(obj.getClass());
		ArrayList pvs = new ArrayList<PropertyValue>();
		List<String> excludedProperties = Arrays.asList(EXCLUDED_PROPERTIES);
		
		for (int i = 0; i < pds.length; i++) {
			Object value = null;
			String name = pds[i].getName();
			
			if (!excludedProperties.contains(name)) {
				try {
					value = pds[i].getReadMethod().invoke(obj, (Object[]) null);
				} catch (IllegalAccessException e) {
					log.error("Error reading property name: " + name, e);
				} catch (IllegalArgumentException e) {
					log.error("Error reading property name: " + name, e);
				} catch (InvocationTargetException e) {
					log.error("Error reading property name: " + name, e);
				}
				pvs.add(new PropertyValue (name, value));
			}
		}
		
		return (PropertyValue[]) pvs.toArray(new PropertyValue[pvs.size()]); 
	}
	
	/**
	 * Copy a property, avoid Execeptions
	 * @param source source bean
	 * @param dest destination bean
	 * @param propertyName the propertyName
	 * 
	 */
	public static void copyProperty(Object source,
						Object dest, String propertyName) {
		BeanWrapper wrapper = new BeanWrapperImpl(source);
		PropertyValue pv = new PropertyValue(propertyName,
								wrapper.getPropertyValue(propertyName));
		// wrapper.set(dest);
		wrapper.setPropertyValue(pv);
	}
	
	/**
	 * Set property, without trowing exceptions on errors
	 * @param bean bean name
	 * @param name name
	 * @param value value
	 */
	public static void setProperty(Object bean, String name, Object value) {
		try  {
			BeanWrapper wrapper = new BeanWrapperImpl(bean);
			wrapper.setPropertyValue(new PropertyValue(name, value));
		} catch (InvalidPropertyException ipe) {
			log.debug("Bean has no property: " + name);
		} catch (PropertyAccessException pae) {
			log.debug("Access Error on property: " + name);
		}
	}
	/**
	 * Get property value null if none
	 * @param bean beam
	 * @param name name
	 * @return the property value
	 */
	public static Object getProperty(Object bean, String name) {
		try {
			BeanWrapper wrapper = new BeanWrapperImpl(bean);
			return wrapper.getPropertyValue(name);
		} catch (PropertyAccessException pae) {
			return null;
		}
	}
}
