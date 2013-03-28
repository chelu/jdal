/*
 * Copyright 2008-2011 the original author or authors.
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
package info.joseluismartin.gui.form;

import info.joseluismartin.beans.StaticMessageSource;
import info.joseluismartin.gui.SimpleDialog;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.border.Border;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * Static utility library for use in Swing Forms
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
@SuppressWarnings("unchecked")
public abstract class FormUtils {
	
	public static final Icon CANCEL_ICON = getIcon("images/16x16/dialog-cancel.png");
	public static final Icon OK_ICON = getIcon("/images/16x16/dialog-ok.png");
	
	private static final Log log = LogFactory.getLog(FormUtils.class);
	
	public static void link(final JComboBox primary, final JComboBox dependent, final String propertyName) {
		link(primary, dependent, propertyName, false);
	}
	
	/**
	 * Add a link on primary and dependent JComboBoxes by property name. 
	 * When selection changes on primary use propertyName to get a Collection and fill dependent JComboBox with it
	 * @param primary JComboBox when selection changes
	 * @param dependent JComboBox that are filled with collection	
	 * @param propertyName the property name for get the collection from primary selected item
	 * @param addNull if true, add a null as first combobox item
	 */
	@SuppressWarnings("rawtypes")
	public static void link(final JComboBox primary, final JComboBox dependent, final String propertyName, 
			final boolean addNull) {
		
		primary.addActionListener(new ActionListener() {
		
			public void actionPerformed(ActionEvent e) {
				Object selected = primary.getSelectedItem();
				if (selected != null) {
					BeanWrapper wrapper = PropertyAccessorFactory.forBeanPropertyAccess(selected);
					if (wrapper.isWritableProperty(propertyName)) {
						Collection collection = (Collection) wrapper.getPropertyValue(propertyName);
						Vector<Object> vector = new Vector<Object>(collection);
						if (addNull) vector.add(0, null);
						DefaultComboBoxModel model = new DefaultComboBoxModel(vector);
						dependent.setModel(model);
					}
					else {
						log.error("Can't write on propety '" + propertyName + "' of class: '" + selected.getClass() + "'");
					}
				}
			}
		});
	}
	
	/**
	 * Return a List of Objects from a ComboBoxModel
	 * @param model ComboBoxModel
	 * @return a list of Objects with ComboBoxModel items
	 */
	public static List<Object> getComboModelList(ComboBoxModel model) {
		ArrayList<Object> list = new ArrayList<Object>();
		for (int i = 0; i < model.getSize(); i++) {
			list.add(model.getElementAt(i));
		}
		return list;
	}
	
	public static JComboBox newCombo(int chars) {
		StringBuilder sb = new StringBuilder(chars);
		while (chars-- > 0) sb.append("X");
		JComboBox combo = new JComboBox();
		combo.setPrototypeDisplayValue(sb.toString());
		return combo;
	}
	
	/**
	 * Make font of JLabel bold
	 * @param label JLabel to make bold
	 */
	public static void setBold(JLabel label) {
		label.setFont(label.getFont().deriveFont(Font.BOLD));
	}
	
	/**
	 * Create Titled Border
	 * @param name the title
	 * @return Border
	 */
	public static Border createTitledBorder(String name) {
		Border margin = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		Border title = BorderFactory.createTitledBorder(name);
		
		return BorderFactory.createCompoundBorder(title, margin);
	}
	
	
	/**
	 * Get Default OK Button from LookAndFeel (like JOptionPane)
	 */
	public static JButton newOKButton() {
		String text = StaticMessageSource.getMessage("Accept");
		int mnemonic = getMnemonic("OptionPane.okButtonMnemonic");
		JButton b = new JButton(text, OK_ICON);
		b.setMnemonic(mnemonic);
		b.setAlignmentX(Container.CENTER_ALIGNMENT);
		b.setAlignmentY(Container.CENTER_ALIGNMENT);
		return b;
	}
	

	/**
	 * Get Default Cancel Button from LookAndFeel (like JOptionPane)
	 */
	public static JButton newCancelButton() {
		String text = StaticMessageSource.getMessage("Cancel");
		int mnemonic = getMnemonic("OptionPane.cancelButtonMnemonic");
		JButton b = new JButton(text, CANCEL_ICON);
		b.setMnemonic(mnemonic);
		b.setAlignmentX(Container.CENTER_ALIGNMENT);
		b.setAlignmentY(Container.CENTER_ALIGNMENT);
		
		return b;
	}

	private static int getMnemonic(String key) {
		String value = (String) UIManager.get(key);
		
		if (value == null) {
			return 0;
		}
		try {
			return Integer.parseInt(value);
		}
		catch (NumberFormatException nfe) { }

		return 0;
	}
	
	/**
	 * Load Icon from url
	 * @param url
	 * @return Icon, null on faliure
	 */
	public static Icon getIcon(String url) {
		Resource resource = new ClassPathResource(url);
		Icon icon = null;
		try {
			Image image = Toolkit.getDefaultToolkit().getImage(resource.getURL());
			icon = new ImageIcon(image);
		} catch (IOException e) {
			log.error(e);
		}
		return icon;
	}

	/**
	 * Load icon if icon = null, else return icon.
	 * @param icon icon to load
	 * @param url String with url
	 * @return icon
	 */
	public static Icon getIcon(Icon icon, String url) {
		return icon != null ? icon : getIcon(url);
	}
	
	/**
	 * Make a JMenu from an Action List 
	 * @param actions the Action List
	 * @return JMenu 
	 */
	public static JMenu toMenu(List<Action> actions) {
		JMenu menu = new JMenu();
		
		for (Action a : actions)
			menu.add(a);
		
		return menu;
	}

	/**
	 * @param message
	 * @return JLabel
	 */
	public static JLabel newLabelForBox(String message) {
		JLabel label = new JLabel(message);
		label.setMaximumSize(new Dimension(Short.MAX_VALUE, label.getFont().getSize() + 10));
		label.setAlignmentX(Container.CENTER_ALIGNMENT);
		return label;
	}

	/**
	 * Show error message
	 * @param message message to show
	 */
	public static void showError(String message) {
		showError(null, message);
	}
	
	/**
	 * Show error message
	 * @param parent component parent
	 * @param message message to show
	 */
	public static void showError(Component parent, String message) {
		JOptionPane.showMessageDialog(parent, message, "Error", JOptionPane.ERROR_MESSAGE);
	}
	
	/**
	 * Creates a new JDialog with default accept/cancel buttons
	 * @param component to show in
	 * @return new created dialog
	 */
	public static JDialog newDialog(Component component) {
		return newDialog(null, component);
	}

	/**
	 * Creates a new JDialog with default accept/cancel buttons
	 * @param owner owner window
	 * @param component component to show in
	 * @return new crated dialog
	 */
	public static JDialog newDialog(Window owner, Component component) {
		return new SimpleDialog(owner, component);
	}

	/**
	 * Creates empty border
	 * @param size border size
	 * @return empty border
	 */
	public static Border createEmptyBorder(int size) {
		return BorderFactory.createEmptyBorder(size, size, size, size);
	}
	
}
