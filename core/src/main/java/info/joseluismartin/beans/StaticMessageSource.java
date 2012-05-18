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
package info.joseluismartin.beans;

import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;

/**
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 *
 */
public class StaticMessageSource {
	
	@Autowired
	private static MessageSource messageSource = null;
	private static Locale locale = Locale.getDefault();
	private static final Log log = LogFactory.getLog(StaticMessageSource.class);
	
	private StaticMessageSource(MessageSource messageSource) {
		StaticMessageSource.messageSource = messageSource;
	}
	
	/**
	 * @return the messageSource
	 */
	public static MessageSource getMessageSource() {
		return messageSource;
	}
	
	/**
	 * @param messageSource the messageSource to set
	 */
	public static void setMessageSource(MessageSource messageSource) {
		StaticMessageSource.messageSource = messageSource;
	}
	
	/** 
	 * I18n Support
	 * @param code message code
	 * @return message or code if none defined
	 */
	public static String getMessage(String code) {
		try {
			return messageSource == null ?
				code : messageSource.getMessage(code, null, locale);
		}
		catch (Exception e) {
			log.error(e);
			return code;
		}
	}
	
	/** 
	 * I18n Support
	 * @param msr message source resolvable
	 * @return message or code if none defined
	 */
	 public static String getMessage(MessageSourceResolvable msr) {
		return messageSource == null ?
				msr.getDefaultMessage() : messageSource.getMessage(msr, Locale.getDefault());
	}
}
