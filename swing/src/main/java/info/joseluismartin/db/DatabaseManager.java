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
package info.joseluismartin.db;

import info.joseluismartin.gui.ViewDialog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

import javax.swing.JOptionPane;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 *
 */
public class DatabaseManager {

	private static final Log log = LogFactory.getLog(DatabaseManager.class);
	
	private String applicationName;

	/**
	 * @param applicationName
	 */
	public DatabaseManager(String applicationName) {
		this.applicationName = applicationName;
	}


	public DbConnection getDbConnection(String path) {
		File properties = new File(path);
		Properties prop = new Properties();
		DbConnection conn = new DbConnection();
	
		try {
			if (!properties.exists() || properties.createNewFile()) 
				return null;
			
			prop.load(new FileInputStream(properties));
			conn.fromProperties(prop);
			
			if (conn.test())  // Connection OK
				return conn;
			
			while (askToCreateConnection() && createConnection(conn) != null) {
				if (conn.test()) {
					prop = conn.toProperties();
					prop.store(new FileOutputStream(properties), "");
					return conn;
				}
			}
		
		} catch (Exception e) {
			log.error(e);
		}
		// failed
		return null;
	}

	public DbConnection createConnection() {
		return createConnection(new DbConnection());
	}
	
	public DbConnection createConnection(DbConnection conn) {
		DbConnectionForm  dbf = new DbConnectionForm(conn);
		dbf.init();
		ViewDialog<DbConnection> dlg = new ViewDialog<DbConnection>();
		dlg.setView(dbf);
		dlg.init();
		dlg.setSize(500, 400);
		dlg.setModal(true);
		dlg.setVisible(true);

		if (dlg.getValue() != ViewDialog.OK)
			return null;
		
		return dlg.getModel();
	}


	/**
	 * @return true if user want to create a new database configuration.
	 */
	public boolean askToCreateConnection() {
		return JOptionPane.showConfirmDialog(null, "There isn't a database connection configured for " +
				applicationName + "\nor configuration is not working.\nDo you want to create one now ?") == JOptionPane.OK_OPTION;
	}

}
