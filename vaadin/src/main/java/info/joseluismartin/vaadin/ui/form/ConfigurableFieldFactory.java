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
package info.joseluismartin.vaadin.ui.form;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.ui.Component;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;

/**
 * Configurable FormFieldFactory. Configure friendly from Spring context bean definition
 * files
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class ConfigurableFieldFactory extends DefaultFieldFactory {
	
	private static final Log log = LogFactory.getLog(ConfigurableFieldFactory.class);
	private Map<Object, Class<?extends Field>> fieldMap = Collections.synchronizedMap(
			new HashMap<Object, Class<?extends Field>>());

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field createField(Container container, Object itemId, Object propertyId, Component uiContext) {
		Field f =  getField(container.getContainerProperty(itemId, propertyId).getType());
		if (f != null) {
			f.setCaption(createCaptionByPropertyId(propertyId));
			return f;
		}
		// fail back to default
		return super.createField(container, itemId, propertyId, uiContext);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field createField(Item item, Object propertyId, Component uiContext) {
		Field f =  getField(item.getItemProperty(propertyId).getType());
		if (f != null) {
			f.setCaption(createCaptionByPropertyId(propertyId));
			return f;
		}
		// fail back to default
		return super.createField(item, propertyId, uiContext);
	}
	
	/**
	 * 
	 * @param clazz
	 */
	// FIXME: Move code to helper class, used in BinderFactory too.
	@SuppressWarnings("unchecked")
	public Field getField(Class<?> clazz) {
		Class<?extends Field> fieldClass = null;
		Field field = null;
	
		fieldClass = fieldMap.get(clazz);
		
		if (fieldClass == null) { // try with superclasses

			List superclasses = ClassUtils.getAllSuperclasses(clazz);
			superclasses.addAll(ClassUtils.getAllInterfaces(clazz));
			Iterator iter = superclasses.iterator();
		
			while (iter.hasNext() && fieldClass == null)  {
				fieldClass = fieldMap.get(iter.next());
			}
		}
		
		if (fieldClass  != null) {	
			try {
				field = fieldClass.newInstance();
			} catch (InstantiationException e) {
				log.error(e);
			} catch (IllegalAccessException e) {
				log.error(e);
			}
		}
		else {
			log.warn("Can't find a Field for class: " + clazz.getName());
		}

		return field;
	}
	
	/**
	 * @return the fieldMap
	 */
	public Map<Object, Class<? extends Field>> getFieldMap() {
		return fieldMap;
	}

	/**
	 * @param fieldMap the fieldMap to set
	 */
	public void setFieldMap(Map<Object, Class<? extends Field>> fieldMap) {
		this.fieldMap.clear();
		this.fieldMap.putAll(fieldMap);
	}
	

}
