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
package org.jdal.swing;

import org.jdal.service.PersistentServiceAware;

/**
 * Interface for model editors
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public interface Editor<T> extends PersistentServiceAware<T> {
	
	/**
	 * Add an editor listener to be notified on editor events
	 * @param listener editor listener to add
	 */
	void addEditorListener(EditorListener listener);
	
	/**
	 * Remove a previusly added EditorListener
	 * @param listener
	 */
	void removeEditorListener(EditorListener listener);
	
	
	/**
	 * Save editing model
	 */
	void save();
	
	/**
	 * Cancel edit
	 */
	void cancel();

}
