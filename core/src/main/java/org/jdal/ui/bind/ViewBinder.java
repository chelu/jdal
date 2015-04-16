/*
 * Copyright 2009-2015 Jose Luis Martin
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
package org.jdal.ui.bind;

import javax.xml.bind.Binder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdal.ui.View;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

/**
 * {@link Binder} for {@link View Views} 
 *
 * @author Jose Luis Martin
 * @since 1.2
 */
@SuppressWarnings("unchecked")
public class ViewBinder extends AbstractBinder implements BinderHolder {

	private static final Log log = LogFactory.getLog(ViewBinder.class);
	
	public void doRefresh() {
		Object value = getValue();
		View<Object> view = getView();
		view.setModel(value);
		view.refresh();
	}

	private View<Object> getView() {
		View<Object> view = (View<Object>) component;
		return view;
	}
	
	public void doUpdate() {
		View<Object> view = getView();
		BindingResult br = view.getBindingResult();
		
		if (br != null) {
			br.setNestedPath(propertyName);
		}
			
		view.update();
		setValue(view.getModel());
		
		if (br != null && view.getBindingResult().hasErrors()) {
			for (ObjectError oe : view.getBindingResult().getAllErrors()) {
				getBindingResult().addError(oe);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public PropertyBinder getBinder(String propertyName) {
		View<Object> view = getView();
		if (view instanceof BinderHolder)
			return ((BinderHolder) view).getBinder(propertyName);
		
		log.warn("View class: [" + view.getClass().getName() +"] " +
				"must implements BinderHolder to validate property: [" + propertyName + "]");
		
		return null;
	}

}
