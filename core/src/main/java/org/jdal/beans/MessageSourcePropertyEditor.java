/*
 * Copyright 2009-2014 Jose Luis Martin.
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
package org.jdal.beans;

/**
 * PropertyEditor to convert translate codes with a MessageSource.
 * Use the prefix i18n: to force translation.
 * 
 * @author Jose Luis Martin
 * @since 2.1
 */
import java.beans.PropertyEditorSupport;

import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;

public class MessageSourcePropertyEditor extends PropertyEditorSupport {
	
	
	private MessageSourceAccessor messageSourceAccessor;

	public MessageSourcePropertyEditor(MessageSource messageSource) {
		this.messageSourceAccessor = new MessageSourceAccessor(messageSource);
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		String value = text;
		
		if (text.startsWith("i18n:")) {
			value = messageSourceAccessor.getMessage(text.substring(5));
		}
		
		setValue(value);
	}
}
