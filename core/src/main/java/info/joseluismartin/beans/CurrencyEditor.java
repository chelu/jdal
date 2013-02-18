/*
 * Copyright 2009-2012 Jose Luis Martin.
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
package info.joseluismartin.beans;

import java.beans.PropertyEditorSupport;
import java.util.Locale;

import org.springframework.format.number.CurrencyFormatter;

/**
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class CurrencyEditor extends PropertyEditorSupport {
	
	private Locale locale = Locale.getDefault();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getAsText() {
		return new CurrencyFormatter().print((Number) getValue(), locale);
	}

	/**
	 * @return the locale
	 */
	public Locale getLocale() {
		return locale;
	}

	/**
	 * @param locale the locale to set
	 */
	public void setLocale(Locale locale) {
		this.locale = locale;
	}

}
