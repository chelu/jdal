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
package org.jdal.vaadin.ui.bind;

import java.util.Map;

import org.jdal.ui.bind.ConfigurableControlAccessorFactory;
import org.jdal.ui.bind.ControlAccessor;
import org.jdal.vaadin.ui.table.PageableTable;
import org.jdal.vaadin.ui.table.TableEditor;

import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;

/**
 * Register Vaadin control accessors
 * 
 * @author Jose Luis Martin
 */
public abstract class VaadinBindingUtils {

	public static void registerControlAccessors(ConfigurableControlAccessorFactory accessorFactory) {
		Map<Class<?>, Class<?extends ControlAccessor>> accessors = accessorFactory.getAccessors();
		
		accessors.put(AbstractField.class, FieldAccessor.class);
		accessors.put(TextField.class, TextFieldAccessor.class);
		accessors.put(Label.class, LabelAccessor.class);
		accessors.put(PageableTable.class, PageableTableAccessor.class);
		accessors.put(Table.class, TableAccessor.class);
		accessors.put(TableEditor.class, TableEditorAccessor.class);
	}
}
