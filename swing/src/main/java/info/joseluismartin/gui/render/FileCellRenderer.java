package info.joseluismartin.gui.render;

import java.awt.Component;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JList;


/**
 * List Cell Renderer that displays values with icon from extension.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 *
 */
public class FileCellRenderer extends DefaultListCellRenderer {

	private static final long serialVersionUID = 1L;
	private Map<String, Icon> iconMap = new HashMap<String, Icon>();
	
	@Override
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {

		super.getListCellRendererComponent(list, value, index, isSelected,
				cellHasFocus);
		
		// sets name and icon
		String name = value.toString();
		String extension = name.substring(name.lastIndexOf('.') + 1);
		Icon icon = iconMap.get(extension.toLowerCase());
		if (icon == null) 
			icon = iconMap.get("default");
		setIcon(icon);
		setText(name);
		
		return this;
	}

	public Map<String, Icon> getIconMap() {
		return iconMap;
	}

	public void setIconMap(Map<String, Icon> iconMap) {
		this.iconMap = iconMap;
	}

	
}
