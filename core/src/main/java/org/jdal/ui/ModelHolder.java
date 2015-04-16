/*
 * Copyright 2008-2015 Jose Luis Martin.
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
package org.jdal.ui;

/**
 * Interface for classes that holds models, ie a model pointer interface
 * 
 * @author Jose Luis Martin.
 * @param <T> model class
 * @since 1.0
 */
public interface ModelHolder<T> {
	
	/**
	 * Gets model
	 * @return the model
	 */
	T getModel();
	
	/**
	 * Sets model
	 * @param model
	 */
	void setModel(T model);
}
