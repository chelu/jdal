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
package org.jdal.swing.bind;

import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JToggleButton;
import javax.swing.text.JTextComponent;

import org.jdal.swing.Selector;
import org.jdal.swing.table.TablePanel;
import org.jdal.ui.View;
import org.jdal.ui.bind.ConfigurableControlAccessorFactory;
import org.jdal.ui.bind.ControlAccessor;
import org.jdal.ui.bind.ViewAccessor;

/**
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 *
 */
public abstract class SwingBindingUtils {

	public static void registerSwingAccessors(ConfigurableControlAccessorFactory accessorFactory) {
		Map<Class<?> , Class<?extends ControlAccessor>> accessors = accessorFactory.getAccessors();
		
		accessors.put(JTextComponent.class, TextComponentAccessor.class);
		accessors.put(JList.class, ListAccessor.class);
		accessors.put(Selector.class, SelectorAccessor.class);
		accessors.put(JToggleButton.class, ToggleButtonAccessor.class);
		accessors.put(JComboBox.class, ComboAccessor.class);
		accessors.put(View.class, ViewAccessor.class);
		accessors.put(JLabel.class, LabelAccessor.class);
		accessors.put(TablePanel.class, TablePanelAccessor.class);
	}
}
