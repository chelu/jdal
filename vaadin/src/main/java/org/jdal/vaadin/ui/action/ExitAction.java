/*
 * Copyright 2009-2013 the original author or authors.
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
package org.jdal.vaadin.ui.action;

import org.jdal.vaadin.VaadinUtils;
import org.jdal.vaadin.ui.table.ButtonListener;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.UI;

/**
 * Exit Action.
 * 
 * @author Jose Luis Martin
 * @since 2.0
 */
public class ExitAction extends ButtonListener {

	@Override
	public void buttonClick(ClickEvent event) {
		VaadinUtils.exit();
	}

}
