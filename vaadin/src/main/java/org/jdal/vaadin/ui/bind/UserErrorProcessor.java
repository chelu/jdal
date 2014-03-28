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

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.jdal.beans.StaticMessageSource;
import org.jdal.ui.validation.ErrorProcessor;
import org.springframework.validation.FieldError;

import com.vaadin.server.UserError;
import com.vaadin.ui.AbstractField;

/**
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 *
 */
public class UserErrorProcessor implements ErrorProcessor, Serializable {

	private Set<AbstractField<?>> fieldSet = new HashSet<AbstractField<?>>();
	/**
	 * {@inheritDoc}
	 */
	public void processError(Object control, FieldError error) {
		if (control instanceof AbstractField) {
			AbstractField<?> f = (AbstractField<?>) control;
			f.setComponentError(new UserError(StaticMessageSource.getMessage(error)));
			fieldSet.add(f);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void reset() {
		for (AbstractField<?> f : fieldSet)
			f.setComponentError(null);
		
		fieldSet.clear();
	}

}
