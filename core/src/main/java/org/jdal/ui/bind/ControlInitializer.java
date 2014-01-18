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
package org.jdal.ui.bind;

/**
 * Interface for control initializers.
 * 
 * @author Jose Luis Martin 
 */
public interface ControlInitializer {
	
	
	/**
	 * Initalize control
	 * @param control control to initialize
	 * @param property property name
	 * @param clazz backing object class, ie model
	 */
	void initialize(Object control, String property, Class<?> clazz);
	
	/**
	 * Sets if initialize object, ie drop ORM  proxies.
	 * @param initializeEntities
	 */
	void setInitializeEntities(boolean initializeEntities);
}
