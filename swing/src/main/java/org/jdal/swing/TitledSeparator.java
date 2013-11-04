/*
 * Copyright 2009-2012 the original author or authors.
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
package org.jdal.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.UIManager;

/**
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 *
 */
public class TitledSeparator extends Box {

	public static final int BORDER = 0;
	public static final int BOLD = 1;
	public static final int NORMAL = 2;
	
	private Font font;
	private Color color;
	
	public TitledSeparator(String title) {
		this(title, BORDER);
	}
	
	public TitledSeparator(String title, int fontType) {
		super(BoxLayout.LINE_AXIS);
		this.font = getFont(fontType);
	
		if (fontType == BORDER)
			color = UIManager.getColor("TitledBorder.titleColor");
		
		build(title);
	}
	
	public TitledSeparator(String title, Font font, Color color) {
		super(BoxLayout.LINE_AXIS);
		this.font = font;
		this.color = color;
		build(title);
	}
	
	private void build(String title) {
	
		JLabel titleLabel = new JLabel(title);
		
		if (color != null) 
			titleLabel.setForeground(color);
		
		titleLabel.setFont(font);
		
		titleLabel.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		this.add(titleLabel);
		this.add(Box.createHorizontalStrut(5));
		JSeparator separator = new JSeparator();
		separator.setAlignmentY(Container.TOP_ALIGNMENT);
		this.add(separator);
		this.setMaximumSize(new Dimension(Short.MAX_VALUE, 20));
	}
	
	/**
	 * Get font from type
	 * @param fontType font type
	 * @return the font
	 */
	private static Font getFont(int fontType) {
		Font font = null;
		switch (fontType) {
			case BORDER: 
				font = UIManager.getFont("TitledBorder.font");
				break;
			case NORMAL:
			
			
			case BOLD:
				return UIManager.getFont("Label.font").deriveFont(Font.BOLD);

			default:
				font =  UIManager.getFont("Label.font");
			break;
		}
		
		return font;
	}
}
