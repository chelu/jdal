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
package info.joseluismartin.gui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;
import javax.swing.plaf.ColorUIResource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.jgoodies.looks.plastic.Plastic3DLookAndFeel;

/**
 * GuiFactory that use ApplicationContext to create and configure widgets.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class ApplicationContextGuiFactory implements GuiFactory, ApplicationContextAware {
	
	/** log */
	private static final Log log = LogFactory.getLog(ApplicationContextGuiFactory.class);
	/** application context reference */
	protected ApplicationContext context;

	/**
	 * {@inheritDoc}
	 */
	public JComponent getComponent(String name) {
		Object bean = context.getBean(name);
		if (bean instanceof JComponent) {
			return (JComponent) bean;
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public JDialog getDialog(String name) {
		Object bean = context.getBean(name);
		if (bean instanceof JDialog) {
			return (JDialog) bean;
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public Object getObject(String name) {
		return context.getBean(name);
	}
	

	/**
	 * {@inheritDoc}
	 */
	public JPanel getPanel(String name) {
		Object bean = context.getBean(name);
		if (bean instanceof JPanel) {
			return (JPanel) bean;
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.context = applicationContext;
		
	}
	
	public ApplicationContext getApplicationContext() {
		return context;
	}

	/* (non-Javadoc)
	 * @see info.joseluismartin.gui.GuiFactory#getView(java.lang.String)
	 */
	public View<?> getView(String name) {
		Object bean = context.getBean(name);
		
		return bean != null ? (View<?>) bean : null;
	}
	
	/**
	 * @param name view name
	 * @param clazz view class template
	 * @return the view or null if none.
	 */
	@SuppressWarnings("unchecked")
	public <T> View<T> getView(String name, Class<T> clazz) {
		return (View<T>) getView(name);
	}


	public Object getObject(String name, Object[] args) {
		return context.getBean(name, args);
	}
	
	public static JFrame getJFrame() {
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(new Dimension(800, 600));
		return f;
	}
	
	public static void setPlasticLookAndFeel() {
		try { 
			UIManager.setLookAndFeel(new Plastic3DLookAndFeel());
			UIManager.put("Table.gridColor", new ColorUIResource(Color.GRAY));
			UIManager.put("TableHeader.background", new ColorUIResource(220, 220, 220));
			
		} catch (UnsupportedLookAndFeelException e) {
			log.error(e);
		}
	}

	/**
	 * 
	 * @param name title string
	 * @return a new titled border
	 * @deprecated use FormUtils.createTitledBorder instead.
	 */
	@Deprecated
	public static Border createTitledBorder(String name) {
		Border margin = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		Border title = BorderFactory.createTitledBorder(name);
		
		return BorderFactory.createCompoundBorder(title, margin);
	}


}
