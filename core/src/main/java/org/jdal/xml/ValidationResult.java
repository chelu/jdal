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
package org.jdal.xml;

/**
 * Result of a schema validation 
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class ValidationResult {
	/** true if validation was valid */
	private boolean valid = false;
	/** Error Message */
	private String message = "";
	
	/** 
	 * Default Ctor.
	 */
	public ValidationResult() {} 

	/**
	 * Ctor
	 * @param valid true if document is valid
	 * @param message Informative message for logs.
	 */
	public ValidationResult(boolean valid, String message) {
		this.valid = valid;
		this.message = message;
	}
	
	/** 
	 * Ctor
	 * @param valid true if document is valid.
	 */
	public ValidationResult(boolean valid) {
		this.valid = valid;
	}
	
	/**
	 * @return the valid
	 */
	public boolean isValid() {
		return valid;
	}
	
	/**
	 * @param valid the valid to set
	 */
	public void setValid(boolean valid) {
		this.valid = valid;
	}
	
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
}
