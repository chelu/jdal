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

import java.awt.Dimension;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JTextField;

/**
 * Database Connection Form
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class DbConnectionForm  extends AbstractView<DbConnection> {
	
	private JComboBox database = new JComboBox();
	private JTextField port = new JTextField();
	private JTextField host = new JTextField();
	private JTextField dbName = new JTextField();
	
	/**
	 * @param dbConnection
	 */
	public DbConnectionForm(DbConnection dbConnection) {
		super(dbConnection);
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
		
		JComponent c = fb.getForm();
		c.setBorder(FormUtils.createTitledBorder(getMessage("DbConnectionForm.title")));
		
		return c;
	}
	
	/**
	 * Test Method
	 * @param args none
	 */
	public static void main(String[] args) {
		JFrame f = new JFrame();
		DbConnectionForm dbf = new DbConnectionForm(new DbConnection());
		f.add(dbf.getPanel());
		f.setSize(new Dimension(600,400));
		f.setVisible(true);
	}

}
