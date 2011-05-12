package info.joseluismartin.vaadin.ui.table;

import com.vaadin.terminal.Resource;

/**
 * Holder for configurable components by colum in a vaadin table.
 * For use friendly in Spring bean application context as inner bean.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class Column {
	private String name;
	private String displayName;
	private int width = -1;
 	private String align;
	private Resource icon;
	
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
	 * @return the displayName
	 */
	public String getDisplayName() {
		return displayName;
	}
	
	/**
	 * @param displayName the displayName to set
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * @param width the width to set
	 */
	public void setWidth(int width) {
		this.width = width;
	}
	
	/**
	 * @return the icon
	 */
	public Resource getIcon() {
		return icon;
	}
	
	/**
	 * @param icon the icon to set
	 */
	public void setIcon(Resource icon) {
		this.icon = icon;
	}

	/**
	 * @return the align
	 */
	public String getAlign() {
		return align;
	}

	/**
	 * @param align the align to set
	 */
	public void setAlign(String align) {
		this.align = align;
	}
}
