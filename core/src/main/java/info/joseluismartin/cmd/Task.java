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
