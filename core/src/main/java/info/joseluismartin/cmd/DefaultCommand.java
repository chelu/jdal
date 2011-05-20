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
package info.joseluismartin.cmd;

/** 
 * Simple base command to extends that do nothing
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */


public class DefaultCommand implements Command {
	
	/** command name */
	private String name;

	/** 
	 * Execute the command
	 * @param data generic command data
	 * @return true if command ok.
	 */
    public boolean execute(Object data) {
        return true;
    }

    /**
     * Handle fault on command chain.
     * @param data generic command data
     */
	public void onFault(Object data) {
		// Don't handled 
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Undo the command, do nothig by default.
	 */
	public void undo() {
		
	}
}
