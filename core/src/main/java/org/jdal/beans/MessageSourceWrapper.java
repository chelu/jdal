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
package org.jdal.beans;

import java.io.Serializable;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 * 
 * Add Simple message methods to a MessageSource
 *
 */
public class MessageSourceWrapper implements MessageSource, Serializable {

	private static final Log log = LogFactory.getLog(MessageSourceWrapper.class);
	private MessageSource messageSource;
	
	public MessageSourceWrapper() {
		
	}
	
	/**
	 * @param messageSource
	 */
	public MessageSourceWrapper(MessageSource messageSource) {
		super();
		this.messageSource = messageSource;
	}

	
	/** 
	 * Get message from code using default Locale
	 * @param code message code
	 * @return message or code if none defined
	 */
	public String getMessage(String code) {
		try {
			return messageSource == null ?
				code : messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
		} 
		catch (NoSuchMessageException nsme) {
			log.error(nsme);
		}
		
		return code;
	}
	
	/** 
	 * Get message from resolvable using default locale
	 * @param msr message source resolvable
	 * @return message or code if none defined
	 */
	public String getMessage(MessageSourceResolvable msr) {
		return messageSource == null ?
				msr.getDefaultMessage() : messageSource.getMessage(msr, LocaleContextHolder.getLocale());
	}

	/**
	 * @return the messageSource
	 */
	public MessageSource getMessageSource() {
		return messageSource;
	}

	/**
	 * @param messageSource the messageSource to set
	 */
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
	/**
	 * @param code
	 * @param args
	 */
	public String getMessage(String code, Object[] args) {
		return getMessage(code, args, "", LocaleContextHolder.getLocale());
	}


	/**
	 * @param code
	 * @param args
	 * @param defaultMessage
	 * @param locale
	 * @return message 
	 * @see org.springframework.context.MessageSource#getMessage(java.lang.String, java.lang.Object[], java.lang.String, java.util.Locale)
	 */
	public String getMessage(String code, Object[] args, String defaultMessage, Locale locale) {
		return messageSource == null ? code : messageSource.getMessage(code, args, defaultMessage, locale);
	}

	/**
	 * @param code
	 * @param args
	 * @param locale
	 * @return message
	 * @throws NoSuchMessageException
	 * @see org.springframework.context.MessageSource#getMessage(java.lang.String, java.lang.Object[], java.util.Locale)
	 */
	public String getMessage(String code, Object[] args, Locale locale) throws NoSuchMessageException {
		return messageSource == null ? code : messageSource.getMessage(code, args, locale);
	}

	/**
	 * @param resolvable
	 * @param locale
	 * @return message
	 * @throws NoSuchMessageException
	 * @see org.springframework.context.MessageSource#getMessage(org.springframework.context.MessageSourceResolvable, java.util.Locale)
	 */
	public String getMessage(MessageSourceResolvable resolvable, Locale locale) throws NoSuchMessageException {
		return messageSource == null ? resolvable.getDefaultMessage() : messageSource.getMessage(resolvable, locale);
	}
	
	/**
	 * Test if code is resolved
	 * @param code code to resolved
	 * @return true if source can resolve code
	 */
	public boolean hasMessage(String code) {
		if  (messageSource == null)
			return false;
		
		try {
			messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
			return true;
		} 
		catch (NoSuchMessageException nsme) {
			return false;
		}
	}
	
	
}
