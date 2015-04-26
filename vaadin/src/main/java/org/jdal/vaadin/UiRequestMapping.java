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
package org.jdal.vaadin;

import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;

/**
 * Interface to define a mapping between request and Vaadin {@link UI UIs}.
 * 
 * @author Jose Luis Martin
 * @since 2.1
 */
public interface UiRequestMapping {

	/**
	 * Get the UI that applies for a request
	 * @param request request to map
	 * @return the mapped UI
	 */
	UI getUi(VaadinRequest request);

	/**
	 * Gets the UI Class taht applies for a request
	 * @param request the request to map
	 * @return the mapped UI class
	 */
	Class<?extends UI> getUiClass(VaadinRequest request);
}
