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
package info.joseluismartin.gui.bind;

import info.joseluismartin.logic.CollectionPersistenceService;

import java.util.Collection;

/**
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 *
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class CollectionPersistentServiceBinder extends AbstractBinder {

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doRefresh() {
		getComponent().getCollection().clear();
		getComponent().save((Collection) getValue());
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doUpdate() {
		setValue(getComponent().getAll());
		
	}
	
	@Override
	public CollectionPersistenceService getComponent() {
		return (CollectionPersistenceService) component;
	}

}