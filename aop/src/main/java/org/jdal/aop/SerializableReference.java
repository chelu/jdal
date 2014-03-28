/*
 * Copyright 2009-2014 the original author or authors.
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
package org.jdal.aop;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanReference;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

/**
 * Serializable reference to a bean in application context.
 * 
 * @author Jose Luis Martin
 * @since 2.0
 *
 */
public class SerializableReference implements Serializable {
	
	private static final Log log = LogFactory.getLog(SerializableReference.class);
	private static final Map<String, Object> serializedObjects = new ConcurrentHashMap<String, Object>();
	/** force to use cglib in deseriazed proxies */
	private boolean proxyTargetClass;
	/** hold a copy of bean in memory instead looking in a bean factory */
	private boolean useMemoryCache;
	/** dependency descriptor used to lookup bean on deserialization */
	private DependencyDescriptor descriptor;
	/** bean factory to use */
	private ConfigurableListableBeanFactory beanFactory;
	/** id for in memory cache */
	private String id;
	/** bean name holding the serializable proxy */
	private String beanName;
	/** target object */
	private transient Object target;
	/** target bean name */
	private String targetBeanName;
	
	public SerializableReference() {
		
	}
	
	public SerializableReference(Object target, boolean proxyTargetClass, 
			boolean useMemoryCache, ConfigurableListableBeanFactory beanFactory,
			DependencyDescriptor descriptor, String beanName) {
		
		this.proxyTargetClass = proxyTargetClass;
		this.useMemoryCache = useMemoryCache;
		this.beanFactory = beanFactory;
		this.beanName = beanName;
		this.descriptor = descriptor;
		this.target = target;
	}
	
	public SerializableReference(Object target, boolean proxyTargetClass, 
			boolean useMemoryCache, ConfigurableListableBeanFactory beanFactory, String targetBeanName) {
		this(target, proxyTargetClass, useMemoryCache, beanFactory, null, null);
		this.targetBeanName = targetBeanName;
	}

	public void serialize() {
		if (useMemoryCache) {
			this.id = ObjectUtils.identityToString(target);
			serializedObjects.put(id, target);
			
			if (log.isDebugEnabled())
				log.debug("Added new serialized reference. serialized objects size [" + serializedObjects.size() + "]");
		}
	}
	
	private Object readResolve() throws ObjectStreamException {
		if (beanFactory == null) {
			log.error("Can't not deserialize reference without bean factory");
			return null;
		}
		
		if (useMemoryCache) {
			Object ret = serializedObjects.remove(this.id);
			if (ret != null) {
				if (log.isDebugEnabled())
					log.debug("Removed a serialized reference. serialized objects size [" + serializedObjects.size() + "]");
				
				return getSerializableProxy(ret);
			}
		}
		
		if (targetBeanName != null) {
			if (log.isDebugEnabled())
				log.debug("Resolving serializable object to bean name [" + targetBeanName + "]");
			
			return ProxyUtils.createSerializableProxy(beanFactory.getBean(targetBeanName), 
					proxyTargetClass, useMemoryCache, beanFactory, targetBeanName);
		}
			
		if (log.isDebugEnabled()) 
			log.debug("Resolving serializable object for [" + descriptor.getDependencyName() +"]");
		
		Field field = descriptor.getField();
		
		// Check bean definition
		BeanDefinition rbd = beanFactory.getBeanDefinition(beanName);
		PropertyValues pvs = rbd.getPropertyValues();
		if (pvs.contains(field.getName())) {
			Object value = pvs.getPropertyValue(field.getName());
			if (value instanceof BeanReference) 
				return getSerializableProxy(((BeanReference) value).getBeanName());
		}
		
		// Check Resource annotation
		if (field.isAnnotationPresent(Resource.class)) {
			Resource r = field.getAnnotation(Resource.class);
			String name = StringUtils.isEmpty(r.name()) ? descriptor.getField().getName() : r.name();

			return getSerializableProxy(beanFactory.getBean(name));
		}
		
		// Check Autowired
		try {
			return getSerializableProxy(beanFactory.resolveDependency(descriptor, beanName));
		}
		catch(BeansException be) {
			
		}
		
		// Try with dependt beans.		
		String[] dependentBeans = beanFactory.getDependenciesForBean(beanName);
		List<String> candidates = new ArrayList<String>();
		
		for (String name : dependentBeans) {
			if (beanFactory.isTypeMatch(name, descriptor.getDependencyType()));
				candidates.add(name);
		}	
	
		if (candidates.size() == 1)
			return getSerializableProxy(beanFactory.getBean(candidates.get(0)));
		
		if (candidates.size() > 1) {
			for (PropertyValue pv : pvs.getPropertyValues()) {
				if (pv.getValue() instanceof BeanReference) {
					BeanReference br = (BeanReference) pv.getValue();
					if (candidates.contains(br.getBeanName()))
						return getSerializableProxy(beanFactory.getBean(br.getBeanName()));
				}
			}
		}
		
		log.error("Cant not resolve serializable reference wiht candidates " + candidates.toArray());
		
		return null;
	}
	
	protected Object getSerializableProxy(Object targetObject) {
		return ProxyUtils.createSerializableProxy(targetObject, proxyTargetClass, useMemoryCache,
				beanFactory, descriptor,  beanName);
	}
	
	public void setBeanFactory(BeanFactory beanFactory) {
		this.beanFactory = (ConfigurableListableBeanFactory) beanFactory;
	}
	
	public void setTargetBeanName(String name) {
		this.targetBeanName = name;
	}

	public boolean isProxyTargetClass() {
		return proxyTargetClass;
	}

	public void setProxyTargetClass(boolean proxyTargetClass) {
		this.proxyTargetClass = proxyTargetClass;
	}

	public boolean isUseMemoryCache() {
		return useMemoryCache;
	}

	public void setUseMemoryCache(boolean useMemoryCache) {
		this.useMemoryCache = useMemoryCache;
	}

	public String getBeanName() {
		return beanName;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	public Object getTarget() {
		return target;
	}

	public void setTarget(Object target) {
		this.target = target;
	}

	public String getTargetBeanName() {
		return targetBeanName;
	}

}
