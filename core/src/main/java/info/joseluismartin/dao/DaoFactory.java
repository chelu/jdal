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
package info.joseluismartin.dao;

import java.io.Serializable;

/**
 * A Factory for Daos
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 * @since 1.1
 */
public interface DaoFactory {
	
	/**
	 * Create a Dao for entity class
	 * @param <T> entity class
	 * @param entityClass entityClass 
	 * @return a new Dao for entityClass
	 */
	<T> Dao<T, Serializable> createDao(Class<T> entityClass);
}
