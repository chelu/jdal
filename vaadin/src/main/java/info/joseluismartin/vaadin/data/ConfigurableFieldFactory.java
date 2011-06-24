/*
 * Copyright 2009-2011 the original author or authors.
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
package info.joseluismartin.vaadin.data;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanInstantiationException;
import org.springframework.beans.BeanUtils;

import com.vaadin.data.Item;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormFieldFactory;

/**
 * Make FormFieldFactory application context friendly.
 *  
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class ConfigurableFieldFactory implements FormFieldFactory {
	
	private static final Log log = LogFactory.getLog(ConfigurableFieldFactory.class);
	private Map<Object, Class<Field>> fieldMap = 
		Collections.synchronizedMap(new HashMap<Object, Class<Field>>());

	/**
	 * {@inheritDoc}
	 */
	public Field createField(Item item, Object propertyId, Component uiContext) {
		Class<Field> clazz = fieldMap.get(propertyId);
		if (clazz != null) {
			try {
				return BeanUtils.instantiate(clazz);
			}
			catch(BeanInstantiationException bie) {
				log.error(bie);
			}
		}
		
		return null;
	}

}
