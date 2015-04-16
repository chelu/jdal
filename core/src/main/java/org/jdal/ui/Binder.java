/*
 * Copyright 2009-2015 Jose Luis Martin
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

import org.springframework.validation.BindingResult;


/**
 * Binder interface define methods common to model based
 * representation of data like {@link View Views}.
 *  
 * @author Jose Luis Martin
 * @param <T> model type
 * @since 1.0
 */
public interface Binder<T> extends ModelHolder<T> {

	/**
	 * Update Model from Component
	 */
	void update();
	
	/**
	 * Update Component from model
	 */
	void refresh();
	
	/**
	 * Get binding result
	 * @return the binding result
	 */
	BindingResult getBindingResult();
}
