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
package info.joseluismartin.system;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * System utility library
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public abstract class SystemUtils {
	
	private static final Log log = LogFactory.getLog(SystemUtils.class);
	
	public static void open(byte[] data, String extension) {
		if (data != null && Desktop.isDesktopSupported()) {
			Desktop desktop = Desktop.getDesktop();
			File file;
			try {
				file = File.createTempFile("tmp", "." + extension);
				file.deleteOnExit();
				FileUtils.writeByteArrayToFile(file, data);
				desktop.open(file);
			} catch (IOException e) {
				String message = "No ha sido posible abrir el fichero";
				JOptionPane.showMessageDialog(null, message, "Error de datos", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	public static byte[] getFileAsByteArray() {
		JFileChooser chooser = new JFileChooser();
		chooser.setMultiSelectionEnabled(false);
		if (JFileChooser.APPROVE_OPTION == chooser.showOpenDialog(null)) {
			File file = chooser.getSelectedFile();
			try {
				return FileUtils.readFileToByteArray(file);
			} catch (IOException e) {
				log.error(e);
			} 
		}
		return null;
	}
}
