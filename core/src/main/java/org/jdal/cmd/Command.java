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

import java.io.Serializable;

/**
 * Commmand interface for Command Design Pattern
 * 
 * @author Jose Luis Martin
 * @since 1.0)
 */

public interface Command extends Serializable {

	/**
	 * The Command Action
	 * @param data generic command data
	 * @return true if command sucess
     * @throws CommandException
	 */	
	boolean execute(Object data);
	
	/**
	 * Notify Command object that some fault ocurred on a command list
	 * @param data generic command data
	 */
	void onFault(Object data);  
	
	/** 
	 * Try to undo the command
	 * @return false if fails.
	 */
	boolean undo();

	/** 
	 * @return the command name
	 */
	String getName();
}
