/*
 * Copyright 2009-2011 original author or authors.
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
package org.jdal.swing;


import javax.swing.JComponent;

import org.jdal.swing.View;
import org.jdal.ui.ViewSupport;
import org.jdal.ui.bind.BinderFactory;
import org.jdal.ui.bind.ConfigurableBinderFactory;
import org.jdal.ui.bind.ConfigurableControlAccessorFactory;
import org.jdal.ui.bind.ControlAccessorFactory;
import org.jdal.swing.bind.SwingBindingUtils;
import org.jdal.ui.validation.ErrorProcessor;

/**
 * Template class that simplifies {@link View} implementation.
 * 
 * <p> The central method is <code>buildPanel</code> that builds the <code>JComponent</code>
 * that hold the view controls. You may use custom binding of the view overwriting the methods 
 * <code>doUpdate</code> and <code>doRefresh</code>.
 * 
 * <p> For common binding code, you usually use autobinding facitility, that is, using 
 * the same name to the field control and model property name, and setting autobinding to true.
 * When using autobinding, you may exclude model properties from binding using some
 * of <code>ignoreProperty</code> methods.
 * 
 * <p> Manual binding is also supported via <code>bind</code> methods. When binding a control, a
 * the View is added to control as <code>ChangeListener</code> is added to the control for setting dirty property on control
 * changes.
 * 
 * <p> Only <code>org.springframework.util.validation.Validator</code> validators are supported
 * The <code>validateView</code> method calls configured
 * <code>ErrorProcessors</code> to process errors found in validation.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 * @since 1.0
 * @see BinderFactory
 * @see ControlAccessorFactory
 * @see ErrorProcessor
 */
public abstract class AbstractView<T> extends ViewSupport<T> implements View<T>{
	
	private JComponent panel;
	
	/**
	 * Default ctor
	 */
	public AbstractView() {
		this(null);
	}
	
	/**
	 * Create the view and set the model
	 * @param model model to set
	 */
	public AbstractView(T model) {
		setModel(model);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public JComponent getPanel() {
		if (panel == null) {
			panel = buildPanel();
			if (width != 0 && height != 0)
				panel.setSize(width, height);
			
		}
		return panel;
	}
	
	/**
	 * Build the JComponent that hold controls.
	 * @return a JCompoent
	 */
	protected abstract JComponent buildPanel();
	
	/**
	 * Check if there are binder and control accessor factories configured, if not
	 * use defaults.
	 */
	@Override
	protected void checkFactories() {
		if (getControlAccessorFactory() == null) {
			ConfigurableControlAccessorFactory  accessorFactory = ConfigurableControlAccessorFactory.getDefaultFactory();
			SwingBindingUtils.registerSwingAccessors(accessorFactory);
			setControlAccessorFactory(accessorFactory);
		}
		
		if (getBinderFactory() == null) {
			setBinderFactory(ConfigurableBinderFactory.getDefaultFactory());
		}
	}

}
