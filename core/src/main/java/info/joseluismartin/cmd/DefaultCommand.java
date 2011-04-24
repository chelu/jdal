package info.joseluismartin.cmd;

/** 
 * Simple base command to extends that do nothing
 * 
 * @author Jose Luis Martin - (chelu.es@gmail.com)
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
