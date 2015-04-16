/*
 * Copyright 2009-2012 Jose Luis Martin.
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
 * Default implementation for {@link ModelHolder ModelHolders}
 * 
 * @author Jose Luis Martin
 * @since 1.0
 */
public class DefaultModelHolder<T> implements ModelHolder<T> {
	
	private T model;
	
	public DefaultModelHolder(T model) {
		this.model = model;
	}

	/**
	 * {@inheritDoc}
	 */
	public T getModel() {
		return model;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setModel(T model) {
		this.model = model;
	}

}
