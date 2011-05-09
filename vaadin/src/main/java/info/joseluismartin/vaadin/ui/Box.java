package info.joseluismartin.vaadin.ui;

import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Label;

/**
 * Utility class for work with box layouts
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public abstract class Box {
	
	/**
	 * Try to imitate a HorizalGlue of Swing BoxLayout.
	 * A invisible component that get all extra space.
	 * @param layout layout to add glue
	 */
	public static void addHorizontalGlue(AbstractOrderedLayout layout) {
		Label label = new Label();
		label.setWidth("100%");
		layout.addComponent(label);
		layout.setExpandRatio(label, 1.0f);
	}
	
	/**
	 * Try to imitate a VerticalGlue of Swing BoxLayout
	 */
	public static void addVerticalGlue(AbstractOrderedLayout layout) {
		Label label = new Label();
		label.setHeight("100%");
		layout.addComponent(label);
		layout.setExpandRatio(label, 1.0f);
	}
	/**
	 * Try to imitate HorizontalStruct on Swing BoxLayout
	 * @param layout Layout to add the struct
	 * @param width struct withd
	 */
	public static void addHorizontalStruct(AbstractOrderedLayout layout, int width) {
		Label label = new Label();
		label.setWidth(width + "px");
		layout.addComponent(label);
		layout.setExpandRatio(label, 0f);
	}

	/**
	 * Try to imitate VerticalStruct of Swing BoxLayout
	 * @param layout  layout to add struct
	 * @param height struct height
	 */
	public static void addVerticalStruct(AbstractOrderedLayout layout, int height) {
		Label label = new Label();
		label.setHeight(height + "px");
		layout.addComponent(label);
		layout.setExpandRatio(label, 0f);
	}

}
