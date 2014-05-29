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
package org.jdal.vaadin.ui.table;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.event.Action;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;

/**
 * Contextual menu for Vaadin tables.
 * 
 * @author Jose Luis Martin
 * @since 2.0
 */
public class TableActionHandler implements Action.Handler {
	
	private List<Action> actions = new ArrayList<Action>();

	@Override
	public Action[] getActions(Object target, Object sender) {
		return actions.toArray(new Action[] {});
	}

	@Override
	public void handleAction(Action action, Object sender, Object target) {
		if (action instanceof Action.Handler) {
			((Action.Handler) action).handleAction(action, sender, target);
		}
		else if (action instanceof ButtonListener) {
			((ButtonListener) action).buttonClick(new ClickEvent((Component) sender) );
		}
		else {
			doHandleAction(action, sender, target);
		}
	}

	/**
	 * Handle the action
	 * @param action the action
	 * @param sender component that send action
	 * @param target target
	 */
	private void doHandleAction(Action action, Object sender, Object target) {
		// Do nothing by default.
	}

	public List<Action> getActions() {
		return actions;
	}

	public void setActions(List<Action> actions) {
		this.actions = actions;
	}

}
