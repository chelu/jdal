/*
 * Copyright 2009-2012 the original author or authors.
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
package org.jdal.swing.bind;


import java.lang.annotation.Annotation;
import java.util.List;

import javax.persistence.ManyToOne;
import javax.swing.JComboBox;
import javax.swing.JList;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdal.annotations.Reference;
import org.jdal.swing.list.ListComboBoxModel;
import org.jdal.swing.list.ListListModel;
import org.jdal.ui.bind.ControlInitializer;
import org.jdal.ui.bind.ControlInitializerSupport;
import org.jdal.util.BeanUtils;


/**
 * Initialize control by JPA Annotations.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class AnnotationControlInitializer extends ControlInitializerSupport {
	
	private static final Log log = LogFactory.getLog(AnnotationControlInitializer.class);
	/**
	 * {@inheritDoc}
	 */
	public void initialize(Object control, String property, Class<?> clazz) {
		if (persistentService == null) {
			log.warn("Nothing to do without persistent service");
			return;
		}
		Class<?> propertyType = BeanUtils.getPropertyDescriptor(clazz, property).getPropertyType();
		Annotation[] annotations = getAnnotations(property, clazz);
		for (Annotation a : annotations) {
			if (ManyToOne.class.equals(a.annotationType())) {
				List<Object> entities = getEntityList(propertyType);
				if (control instanceof JComboBox) {
					((JComboBox) control).setModel(new ListComboBoxModel(entities));
				}
				else if (control instanceof JList) {
					((JList) control).setModel(new ListListModel(entities));
				}
				break;
			}
				
			if (Reference.class.equals(a.annotationType()) && control instanceof JComboBox) {
				Reference r = (Reference) a;
				Class type = void.class.equals(r.target()) ? propertyType : r.target();
				List entities = getEntityList(type);
				List values = StringUtils.isEmpty(r.property()) ?  entities : 
					getValueList(entities, r.property());
				
				((JComboBox) control).setModel(new ListComboBoxModel(values));
				break;
			}
			
		}
	}

}
