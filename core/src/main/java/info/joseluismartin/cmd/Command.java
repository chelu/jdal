package info.joseluismartin.cmd;

/**
 * Commmand interface for Command Design Pattern (GoF)
 * 
 * @author Jose Luis Martin - (chelu.es@gmail.com)
 */

public interface Command {
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
	
	/** Undo the command, false if fail */
	void undo();

	/** 
	 * @return the command name
	 */
	String getName();
}
