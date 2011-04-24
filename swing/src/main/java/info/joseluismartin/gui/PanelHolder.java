package info.joseluismartin.gui;

import javax.swing.Icon;
import javax.swing.JComponent;

public abstract class PanelHolder {
	
	public PanelHolder() {
		
	}
	
	public PanelHolder(Icon icon, String name) {
		super();
		this.icon = icon;
		this.name = name;
	}

	private Icon icon;
	private String name;

	public abstract JComponent getPanel();

	/**
	 * @return the icon
	 */
	public Icon getIcon() {
		return icon;
	}

	/**
	 * @param icon the icon to set
	 */
	public void setIcon(Icon icon) {
		this.icon = icon;
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
	
	@Override
	public String toString() {
		return name;
	}
}
