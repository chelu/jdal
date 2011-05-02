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
 * Task Interface for Composite of Commands
 * 
 * @author Jose Luis Martin - (chelu.es@gmail.com)
 */

public interface Task extends Command {
	/**
	 * Add Command to command list
	 * @param c command to add
	 */
	void addCommand(Command c);
	/**
	 * remove a Command from command list
	 * @param c command to remove
	 */
    void removeComannd(Command c);
    /**
     * Add a Command to preprocesor list
     * @param c command to add
     */
    void addPreprocesor(Command c);
    /**
     * Remove a Command from preprocesor list
     * @param c command to remove
     */
    void removePreprocesor(Command c);
    /**
     * Add a Command to postprocesor list
     * @param c command to add
     */
    void addPostProcessor(Command c);
    /**
     * Remove a Command from postprocesor list
     * @param c command to remove
     */
    void removePostProcessor(Command c);
    	
}
