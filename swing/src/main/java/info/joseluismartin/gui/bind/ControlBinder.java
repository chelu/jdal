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
package info.joseluismartin.gui.bind;

import info.joseluismartin.text.FormatUtils;

import java.text.ParseException;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.format.Parser;
import org.springframework.format.Printer;

/**
 * Generic <code>Binder</code> that use a <code>ControlAccessorFactory</code> to get/set 
 * control values. Handle <code>NumberFormat</code> and <code>PeriodFormat</code> 
 * annotations when binding string values to text components.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 * @see info.joseluismartin.gui.Binder
 * @see info.joseluismartin.gui.bind.AbstractBinder
 * @since 1.1
 */
public class ControlBinder extends AbstractBinder {
	
	private static final Log log = LogFactory.getLog(ControlBinder.class);
	private ControlAccessorFactory controlAccessorFactory;
	private ControlAccessor controlAccessor;

	public ControlBinder() {
		this(new ConfigurableControlAccessorFactory());
	}
	
	public ControlBinder(ControlAccessorFactory controlAccessorFactory) {
		this.controlAccessorFactory = controlAccessorFactory;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doBind() {
		controlAccessor = controlAccessorFactory.getControlAccessor(component);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doRefresh() {
		Object value = getValue();
		
		if (value != null && controlAccessor.isTextControl()) {
			Printer<Object> printer = getPrinter();
			if (printer != null) {
				value = printer.print(value, Locale.getDefault());
			}
		}
		
		controlAccessor.setControlValue(value);
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doUpdate() {
		Object value = controlAccessor.getControlValue();
		
		if (controlAccessor.isTextControl()) {
			Parser<?> parser = getParser();
			if (parser != null)
				try {
					value = parser.parse((String) value, Locale.getDefault());
				} catch (ParseException e) {
					log.error("Can't parse String : " + value.toString());
				}
		}
		setValue(value);
		
	}
	
	@SuppressWarnings("unchecked")
	protected Printer<Object> getPrinter() {
		return (Printer<Object>) FormatUtils.getPrinter(getModel().getClass(), propertyName);
	}
	
	
	protected Parser<?> getParser() {
		return FormatUtils.getParser(getModel().getClass(), propertyName);
	}
	

	/**
	 * @return the controlAccessorFactory
	 */
	public ControlAccessorFactory getControlAccessorFactory() {
		return controlAccessorFactory;
	}

	/**
	 * @param controlAccessorFactory the controlAccessorFactory to set
	 */
	public void setControlAccessorFactory(ControlAccessorFactory controlAccessorFactory) {
		this.controlAccessorFactory = controlAccessorFactory;
	}

	
}
