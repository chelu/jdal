package org.jdal.vaadin.ui.bind;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.util.List;

import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdal.annotation.SerializableProxy;
import org.jdal.annotations.Reference;
import org.jdal.beans.PropertyUtils;
import org.jdal.ui.bind.ControlInitializerSupport;
import org.jdal.ui.bind.InitializationConfig;
import org.springframework.core.ResolvableType;

import com.vaadin.ui.AbstractSelect;

/**
 * Control initializer for Vaadin controls
 * 
 * @author Jose Luis Martin
 */
@SerializableProxy
public class VaadinControlInitializer extends ControlInitializerSupport {

private static final Log log = LogFactory.getLog(VaadinControlInitializer.class);
	
	/**
	 * {@inheritDoc}
	 */
	public void initialize(Object control, String property, InitializationConfig config) {
		if (this.dao == null) {
			log.warn("Nothing to do without persistent service");
			return;
		}
		Class<?> clazz = config.getType();
		PropertyDescriptor pd = PropertyUtils.getPropertyDescriptor(clazz, property);

		if (pd == null) {
			log.error("Not found property descriptor for property [" + property + "]") ;
			return;
		}
			
		ResolvableType propertyType = ResolvableType.forMethodReturnType(pd.getReadMethod());
		Annotation[] annotations = getAnnotations(property, clazz);
		for (Annotation a : annotations) {
			List<Object> items = null;
			
			if (ManyToOne.class.equals(a.annotationType()) || ManyToMany.class.equals(a.annotationType()) ) {
				items = getEntityList(propertyType, config.getSortPropertyName());
				
			}
			else if (Reference.class.equals(a.annotationType())) {
				Reference r = (Reference) a;
				Class<?> type = void.class.equals(r.target()) ? propertyType.resolve() : r.target();
				List<Object> entities = getEntityList(type, config.getSortPropertyName());
				items = StringUtils.isEmpty(r.property()) ?  entities : 
					getValueList(entities, r.property());	
			}
			
			if (items != null) {
				if (control instanceof AbstractSelect) {
					for (Object item : items) 
						((AbstractSelect) control).addItem(item);
				}
				break;
			}
		}
	}
}
