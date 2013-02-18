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

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Composite of commands that delegate operations to collections.
 * A command may be added as preprocessor, command or postprocessor
 * 
 * @author Jose Luis Martin - (chelu.es@gmail.com)
 */

public class DefaultTask   implements Task {
	
	/** Log */
	private static Log log = LogFactory.getLog(DefaultTask.class);
	/** Stop executing commands on command fault */
	private boolean stopOnFault = false;
	/** List of commands executed first */
    private List<Command> preProcessors;
    /** List of commands executed last */
    private List<Command> postProcessors;
    /** List of commands to execute */
    private List<Command> commands;
    /** Command Name */
    private String name;
    
    /**
     * Default Ctor.
     */
    public DefaultTask() {
    	
        preProcessors = new LinkedList<Command>();
        postProcessors = new LinkedList<Command>();
        commands = new LinkedList<Command>();
    }
 
    /**
     * Add a <code>Command</code> to the command list of task
     * 
     * @param c Command to add.
     */
    public void addCommand(Command c) {
        commands.add(c);
    }
    
    /** 
     * Add a list of commands
     * @param cmdList the command list to add
     */
    public void addComand(List<Command> cmdList) {
    	commands.addAll(cmdList);
    }
    
    /**
     * Remove a <code>Command</code> from command list of task
     * 
     * @param c Commmand to remove
     */
    public void removeComannd(Command c) {
        if (commands.contains(c)) {
            commands.remove(c);
        }
    }

    /**
     * Add a <code>Command</code> to prepocesor command list
     * 
     * @param c Command to add.
     */
    public void addPreprocesor(Command c) {
        preProcessors.add(c);
    }

    /**
     * Remove a <code>Command</code> from preprocesor command list
     * 
     * @param c Command to remove.
     */
    public void removePreprocesor(Command c) {
    	if (preProcessors.contains(c)) {
    		preProcessors.remove(c);
    	}
    }
    
    /** 
     * Add a <code>Command</code> to postprocessor command list
     * 
     * @param c Command to add.
     */
    public void addPostProcessor(Command c) {
        postProcessors.add(c);
    }
    
    /**
     * Remove a <code>Command</code> to postprocesor list of task
     * 
     * @param c Command to removce.
     */
    public void removePostProcessor(Command c) {
    	if (postProcessors.contains(c)) {
    		postProcessors.remove(c);
    	}
    }
    
    /**
     * Execute prepocesors, commands and postprocessors in order
     * throw a CommandException if some command fails an stopOnfault
     * is true.
     *
     * @param data with command data
     * @return true if command ok.
     */
    public boolean execute(Object data) {
        boolean result = true;

        for (Command cmd : newCommandList()) {
        	try {
        		cmd.execute(data);
        	} catch (CommandException ce) {
        		log.error(ce);
        		onFault(ce);
        		result = false;
        		
        		if (stopOnFault) { 
        			throw ce;
        		}
        	}
        }
        return result;
    }
    
	/**
	 * Composite onFault to Commands
	 * @param data generic command data
	 */
    public void onFault(Object data) {
		for (Command cmd : newCommandList()) {
			cmd.onFault(data);
		}
	}
    
    /**
	 * Creates a new command list
	 * @return list a new Command List with all commands from 
	 * pre, commmand, post lists.
	 */
	public List<Command> newCommandList() {
		List<Command> list  = new LinkedList<Command>();
		list.addAll(this.preProcessors);
		list.addAll(this.commands);
		list.addAll(this.postProcessors);
		
		return list;
	}
    
    // Getter And Setters
	
	/**
	 * @return list of commands 
	 */
	public List<Command>  getCommands() {
		return commands;
	}
	/**
	 * @param commands list of commands to set
	 */
	public void setCommands(List<Command> commands) {
		this.commands = commands;
	}
	/**
	 * 
	 * @return postprocesor command list
	 */
	public List<Command>  getPostProcessors() {
		return postProcessors;
	}
	/**
	 * 
	 * @param postProcessors command list to set
	 */
	public void setPostProcessors(List<Command> postProcessors) {
		this.postProcessors = postProcessors;
	}
	/**
	 * @return preprocesor command list
	 */
	public List<Command>  getPreProcessors() {
		return preProcessors;
	}
	/**
	 * @param preProcessors command list to sets
	 */
	public void setPreProcessors(List<Command> preProcessors) {
		this.preProcessors = preProcessors;
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
	 * Undo command 
	 */
	public void undo() {
		for (Command cmd : newCommandList()) {
			cmd.undo();
		}
	}


}
