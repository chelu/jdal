/*
 * Copyright 2009-2011 the original author or authors.
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

import info.joseluismartin.gui.AbstractView;
import info.joseluismartin.gui.bind.ConfigurableControlAccessorFactory;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 * Database Connection Form
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class DbConnectionForm  extends AbstractView<DbConnection> 
	implements ActionListener {
	
	private JComboBox database = new JComboBox();
	private JTextField port = new JTextField();
	private JTextField host = new JTextField();
	private JTextField dbName = new JTextField();
	private JTextField user = new JTextField();
	private JButton test;
	private JLabel testResult = new JLabel(" ");
	private List<Database> databases = new ArrayList<Database>();
	
	/**
	 * @param dbConnection
	 */
	public DbConnectionForm(DbConnection dbConnection) {
		super(dbConnection);
		test = new JButton(getMessage("DbConnectionForm.test"));
		test.addActionListener(this);
	
		
	}
	
	/**
	 * Init method, called by container after property sets.
	 */
	public void init() {
		autobind();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected JComponent buildPanel() {
		BoxFormBuilder fb = new BoxFormBuilder();
		fb.setDebug(true);
		fb.add(getMessage("DbConnectionForm.database"),database);
		fb.row();
		fb.add(getMessage("DbConnectionForm.host"),host);
		fb.row();
		fb.add(getMessage("DbConnectionForm.port"), port);
		fb.row();
		fb.add(getMessage("DbConnectionForm.dbName"), dbName);
		fb.row();
		fb.add(getMessage("DbConnectionForm.user"), user);
		
		JComponent c = fb.getForm();

		Box box = Box.createVerticalBox();
		box.add(c);
		box.add(Box.createVerticalStrut(10));
		box.add(test);
		box.add(Box.createVerticalStrut(10));
		box.add(testResult);
		
		box.setBorder(FormUtils.createTitledBorder(getMessage("DbConnectionForm.title")));
		
		return box;
	}
	
	/**
	 * Test Method
	 * @param args none
	 */
	public static void main(String[] args) {
		JFrame f = new JFrame();
		DbConnectionForm dbf = new DbConnectionForm(new DbConnection());
		dbf.setControlAccessorFactory(new ConfigurableControlAccessorFactory());
		dbf.init();
		f.add(dbf.getPanel());
		f.setSize(new Dimension(600,400));
		f.setVisible(true);
	}

	/**
	 * {@inheritDoc}
	 */
	public void actionPerformed(ActionEvent e) {
		if (getModel().test()) {
			testResult.setText(getMessage("DbConnectionForm.success"));
		}
		else {
			testResult.setText(getMessage("DbConnectionForm.failed"));
		}
	}

	/**
	 * @return the databases
	 */
	public List<Database> getDatabases() {
		return databases;
	}

	/**
	 * @param databases the databases to set
	 */
	public void setDatabases(List<Database> databases) {
		this.databases = databases;
	}

}
