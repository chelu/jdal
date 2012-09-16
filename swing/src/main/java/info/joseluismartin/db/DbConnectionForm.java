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
package info.joseluismartin.db;

import info.joseluismartin.gui.AbstractView;
import info.joseluismartin.gui.ViewDialog;
import info.joseluismartin.gui.bind.ConfigurableControlAccessorFactory;
import info.joseluismartin.gui.form.BoxFormBuilder;
import info.joseluismartin.gui.form.FormUtils;
import info.joseluismartin.gui.form.SimpleBoxFormBuilder;
import info.joseluismartin.gui.list.ListComboBoxModel;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
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
	private JPasswordField password = new JPasswordField();
	private JButton test;
	private JLabel testResult = new JLabel();
	
	public DbConnectionForm() {
		this(new DbConnection());
	}
	
	/**
	 * @param dbConnection
	 */
	public DbConnectionForm(DbConnection dbConnection) {
		super(dbConnection);
	
		
	}
	
	/**
	 * Init method, called by container after property sets.
	 */
	public void init() {
		test = new JButton(getMessage("DbConnectionForm.test"));
		test.addActionListener(this);
		database.setModel(new ListComboBoxModel(Database.DATABASES));
		FormUtils.setBold(testResult);
		testResult.setHorizontalAlignment(JLabel.CENTER);
		testResult.setAlignmentX(Component.CENTER_ALIGNMENT);
		autobind();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected JComponent buildPanel() {
		BoxFormBuilder fb = new BoxFormBuilder(FormUtils.createTitledBorder(getMessage("DbConnectionForm.title")));
		
		fb.row();
		fb.startBox();
		fb.setFixedHeight(true);
		fb.row();
		fb.add(getMessage("DbConnectionForm.database"),database);
		fb.row();
		fb.add(getMessage("DbConnectionForm.host"),host);
		fb.row();
		fb.add(getMessage("DbConnectionForm.port"), port);
		fb.row();
		fb.add(getMessage("DbConnectionForm.dbName"), dbName);
		fb.row();
		fb.add(getMessage("DbConnectionForm.user"), user);
		fb.row();
		fb.add(getMessage("DbConnectionForm.password"), password);
		fb.endBox();
		fb.row();
		fb.startBox();
		fb.setFixedHeight(true);
		fb.row();
		fb.add(Box.createHorizontalGlue());
		fb.add(test);
		fb.add(Box.createHorizontalGlue());
		fb.endBox();
		fb.row();
		fb.add(testResult, SimpleBoxFormBuilder.SIZE_UNDEFINED);
		fb.row(SimpleBoxFormBuilder.SIZE_UNDEFINED);
		fb.add(Box.createVerticalGlue());
		
		return fb.getForm();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void actionPerformed(ActionEvent e) {
		update();
		if (getModel().test()) {
			testResult.setText(getMessage("DbConnectionForm.success"));
			testResult.setForeground(new Color(0, 150, 0));
		}
		else {
			testResult.setText(getMessage("DbConnectionForm.failed"));
			testResult.setForeground(Color.RED);
		}
	}
	
	@Override
	public void doRefresh() {
		for (Database db : Database.DATABASES) {
			if (db.equals(getModel().getDatabase())) {
				database.setSelectedItem(db);
			}
		}
	}
	
	/**
	 * Test Method
	 * @param args none
	 */
	public static void main(String[] args) {
		ViewDialog<DbConnection> d = new ViewDialog<DbConnection>();
		DbConnectionForm dbf = new DbConnectionForm(new DbConnection());
		dbf.setControlAccessorFactory(new ConfigurableControlAccessorFactory());
		dbf.init();
		d.setView(dbf);
		d.init();
		d.setVisible(true);
	}


}
