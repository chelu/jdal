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
package info.joseluismartin.exception;

import java.lang.Thread.UncaughtExceptionHandler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * UncaughtExceptionHandler that silently log the exception.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 * @since 1.3
 */
public class LogginExceptionHandler implements UncaughtExceptionHandler {

	private static final Log log = LogFactory.getLog(LogginExceptionHandler.class);
	
	/**
	 * {@inheritDoc}
	 */
	public void uncaughtException(Thread t, Throwable e) {
		log.error(e);
	}

}
