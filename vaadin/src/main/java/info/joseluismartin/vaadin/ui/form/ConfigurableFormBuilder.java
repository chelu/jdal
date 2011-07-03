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
package info.joseluismartin.vaadin.ui.form;

import com.vaadin.ui.Form;
import com.vaadin.ui.FormFieldFactory;
import com.vaadin.ui.Layout;

/**
 * Simple Form Builder
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 * @since 1.1
 */
public class ConfigurableFormBuilder implements FormBuilder {
	
	private String[] visibleProperties;
	private String width;
	private String  height;
	private Layout layout;
	private FormFieldFactory formFieldFactory;

	/**
	 * {@inheritDoc}
	 */
	public Form build() {
		Form form = new Form();
		
		if (layout != null)
			form.setLayout(layout);
		if (formFieldFactory != null)
			form.setFormFieldFactory(formFieldFactory);
		if (visibleProperties != null)
			form.setVisibleItemProperties(visibleProperties);
		if (width != null)
			form.setWidth(width);
		if (height != null)
			form.setHeight(height);
		
		return form;
	}

}
