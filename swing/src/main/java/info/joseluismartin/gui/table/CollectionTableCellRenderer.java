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
package info.joseluismartin.gui.table;

import java.util.Collection;

import javax.swing.table.DefaultTableCellRenderer;

/**
 * Render Collections as colon separated String list
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 * @since 1.3
 */
public class CollectionTableCellRenderer extends DefaultTableCellRenderer {

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void setValue(Object value) {
		Collection<Object> collection = (Collection<Object>) value;
		StringBuilder sb = new StringBuilder();
		
		for (Object object : collection) {
			sb.append(",");
			sb.append(object.toString());
		}
	
		String stringValue = sb.toString();
		
		if (stringValue.length() > 0) 	// drop first ','
			super.setValue(sb.toString().substring(1));
		else 
			super.setValue("");
	}
}