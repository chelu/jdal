/*
 * Copyright 2002-2010 the original author or authors.
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
package info.joseluismartin.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JSeparator;

/**
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 *
 */
public class SeparatorTitled extends Box {
	
	private static final long serialVersionUID = 1L;

	public SeparatorTitled(String title) {
		super(BoxLayout.LINE_AXIS);
		JLabel titleLabel = new JLabel(title);
		titleLabel.setAlignmentY(Component.TOP_ALIGNMENT);
		this.add(titleLabel);
		this.add(Box.createHorizontalStrut(5));
		JSeparator separator = new JSeparator();
		separator.setAlignmentY(Container.BOTTOM_ALIGNMENT);
		this.add(separator);
		this.setMaximumSize(new Dimension(Short.MAX_VALUE, 20));
	}
}
