/*
 * Copyright 2008-2010 the original author or authors.
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
package org.jdal.cmd;

/**
 * A Command Exception
 * 
 * @author Jose Luis Martin - (chelu.es@gmail.com)
 */
public class CommandException extends RuntimeException {
	
	/** serial */
    private static final long serialVersionUID = -4113289411547556084L;
    /** Generic command data */
    private Object arg;

    /**
     * Ctor.
     */
	public CommandException() {
		super();
	}

	/**
	 * Ctor.
	 * @param message exception message
	 * @param cause the upper cause
	 */
	public CommandException(String message, Throwable cause) {
		super(message, cause);
	}

	/** 
	 * Ctor.
	 * @param message exception message
	 */
	public CommandException(String message) {
		super(message);
	}

	/**
	 * Ctor.
	 * @param cause the upper cause
	 */
	public CommandException(Throwable cause) {
		super(cause);
	}
	
	/**
	 * Getter for arg.
	 * @return the arg
	 */
	public Object getArg() {
		return arg;
	}
	
	/**
	 * Setter for arg
	 * @param arg to set
	 */
	public void setArg(Object arg) {
		this.arg = arg;
	}

}
