/*
 * Copyright 2008-2011 the original author or authors.
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
package org.jdal.reporting;

/**
 * @author Jose A. Corbacho
 *
 */
public class ReportingException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7457804399442640547L;

	public ReportingException(){
		super();
	}
	
	public ReportingException(String message, Throwable origin){
		super(message, origin);
	}
	
	public ReportingException(String message){
		super(message);
	}

	/**
	 * @param cause
	 */
	public ReportingException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
}
