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
package org.jdal.service;

import java.io.Serializable;

/**
 * Interface for classes that hold a persistent service.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public interface PersistentServiceAware<T> {

	/**
	 * Set PersistentService to use
	 * @param persistentService 
	 */
	void setPersistentService(PersistentService<T, ?extends Serializable> persistentService);
	
}
