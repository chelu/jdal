package info.joseluismartin.gui.action;

import javax.swing.Action;

import info.joseluismartin.gui.IconAction;

/**
 * Add bean properties to Actions to configure friendly in Spring bean configuration files
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public abstract class BeanAction  extends IconAction {

	private static final long serialVersionUID = 1L;
	
	public String getName() {
		return (String ) getValue(Action.NAME);
	}
	
	public void setName(String name) {
		putValue(Action.NAME, name);
	}
}
