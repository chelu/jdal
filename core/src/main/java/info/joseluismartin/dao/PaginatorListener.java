/*
 * Copyright 2002-2010 the original author or authors.
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

import java.util.EventListener;

/**
 * Listener interface for paginator changes
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 * @see info.joseluismartin.gui.Paginator
 * @see info.joseluismartin.gui.PaginatorView
 */
public interface PaginatorListener extends EventListener {
	
	/**
	 * Notify of paginator changes with a PaginatorChangedEvent
	 * @param event the PaginatorChanged event
	 */
	public void pageChanged(PageChangedEvent event);
}
