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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Component;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;

/**
 * Configurable FieldFactory, to configure fieldFactories friendly from Spring context bean definition
 * files.	
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 * @since 1.1
 */
public class ConfigurableFieldFactory extends DefaultFieldFactory {
	
	/** log */
	private static final Log log = LogFactory.getLog(ConfigurableFieldFactory.class);
	/** class to class field map */
	private Map<Class<?>, Class<?extends Field>> classFieldMap = Collections.synchronizedMap(
			new HashMap<Class<?>, Class<?extends Field>>());
	/** propertyId to FieldBuilder map */
	private Map<Object, FieldBuilder> idBuilderMap = Collections.synchronizedMap(
			new HashMap<Object, FieldBuilder>());
	/** propertyId to class map */
	private Map<Object, Class<?extends Field>> idClassMap = Collections.synchronizedMap(
			new HashMap<Object, Class<?extends Field>>());
	/** class to field builder map */
	private Map<Class<?>, FieldBuilder> classBuilderMap = Collections.synchronizedMap(
			new HashMap<Class<?>, FieldBuilder>());
	
	/** fiedl processor list */
	private List<FieldProcessor> fieldProcessors = new ArrayList<FieldProcessor>();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field createField(Container container, Object itemId, Object propertyId, Component uiContext) {
		return createField(container.getItem(itemId), propertyId, uiContext);
	}

	/**
	 * @param f
	 */
	protected void applyFieldProcessors(Field f, Object propertyId) {
		if (f != null) {
			for (FieldProcessor fp : fieldProcessors)
				fp.processField(f, propertyId);
		}
			
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field createField(Item item, Object propertyId, Component uiContext) {
		
		BeanItem<?> beanItem = (BeanItem<?>) item;
		
		Field f = getField(propertyId, beanItem.getBean().getClass());
	
		if (f != null) {
			f.setCaption(createCaptionByPropertyId(propertyId));
		}
		else {
			// fail back to default
			f = super.createField(item, propertyId, uiContext);
		}
		
		applyFieldProcessors(f, propertyId);
		
		return f;
	}
	
	/**
	 * Try to find a field. It will tray the four configured maps in order:
	 * <ol>
	 *  <li> propertyId to FieldBuilder map.</li>
	 *  <li> propertyId to Field map.</li>
	 *  <li> propertyClass to FieldBuilder map.</li>
	 *  <li> propertyClass to Field map.</li>
	 * </ol>
	 * @param propertyId the propertyId
	 * @param clazz the bean class holding the propertyId
	 * @return Field or null if none configured
	 */
	@SuppressWarnings("unchecked")
	protected Field getField(Object propertyId, Class<?> clazz) {
		// try id to builder map
		FieldBuilder builder = idBuilderMap.get(propertyId);
		if (builder != null) {
			return builder.build(clazz, (String) propertyId);
		}
		// try id to class Map
		Class<?extends Field> fieldClass = idClassMap.get(propertyId);
		if (fieldClass != null)
			return BeanUtils.instantiate(fieldClass);
		
		// try class to builder map
		Class<?> propertyClass = BeanUtils.getPropertyDescriptor(clazz, (String) propertyId).getPropertyType();
		builder  = (FieldBuilder) findByClass(propertyClass, classBuilderMap);
		if (builder != null)
			return builder.build(clazz, (String) propertyId);
		
		// try class to field map
		fieldClass =  (Class<? extends Field>) findByClass(propertyClass, classFieldMap);
		
		return fieldClass != null ? BeanUtils.instantiate(fieldClass) : null;
	}


	@SuppressWarnings("unchecked")
	protected Object findByClass(Class<?> clazz, Map<Class<?>, ?> map) {
		Object target;
		target = map.get(clazz);
		
		if (target == null) { // try with superclasses

			List superclasses = ClassUtils.getAllSuperclasses(clazz);
			superclasses.addAll(ClassUtils.getAllInterfaces(clazz));
			Iterator iter = superclasses.iterator();
		
			while (iter.hasNext() && target == null)  {
				target = map.get(iter.next());
			}
		}
		
		return target;
	}
	
	/**
	 * @return the fieldMap
	 */
	public Map<Class<?>, Class<? extends Field>> getClassFieldMap() {
		return classFieldMap;
	}

	/**
	 * @param fieldMap the fieldMap to set
	 */
	public void setClassFieldMap(Map<Class<?>, Class<? extends Field>> fieldMap) {
		this.classFieldMap.clear();
		this.classFieldMap.putAll(fieldMap);
	}

	/**
	 * @return the fieldProcessors
	 */
	public List<FieldProcessor> getFieldProcessors() {
		return fieldProcessors;
	}

	/**
	 * @param fieldProcessors the fieldProcessors to set
	 */
	public void setFieldProcessors(List<FieldProcessor> fieldProcessors) {
		this.fieldProcessors = fieldProcessors;
	}

	/**
	 * @param builderMap the builderMap to set
	 */
	public void setIdBuilderMap(Map<Object, FieldBuilder> idBuilderMap) {
		this.idBuilderMap.clear();
		this.idBuilderMap.putAll(idBuilderMap);
	}

	/**
	 * @return the builderMap
	 */
	public Map<Object, FieldBuilder> getIdBuilderMap() {
		return idBuilderMap;
	}

	/**
	 * @return the idClassMap
	 */
	public Map<Object, Class<? extends Field>> getIdClassMap() {
		return idClassMap;
	}

	/**
	 * @param idClassMap the idClassMap to set
	 */
	public void setIdClassMap(Map<Object, Class<? extends Field>> idClassMap) {
		this.idClassMap = idClassMap;
	}

	/**
	 * @return the classBuilderMap
	 */
	public Map<Class<?>, FieldBuilder> getClassBuilderMap() {
		return classBuilderMap;
	}

	/**
	 * @param classBuilderMap the classBuilderMap to set
	 */
	public void setClassBuilderMap(Map<Class<?>, FieldBuilder> classBuilderMap) {
		this.classBuilderMap.clear();
		this.classBuilderMap.putAll(classBuilderMap);
	}
}
