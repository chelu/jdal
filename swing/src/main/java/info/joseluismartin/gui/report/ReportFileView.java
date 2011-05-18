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
package info.joseluismartin.gui.report;

import info.joseluismartin.gui.AbstractView;
import info.joseluismartin.reporting.Report;
import info.joseluismartin.reporting.ReportingException;
import info.joseluismartin.service.ReportService;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.sql.DataSource;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * 
 * @author Jose A. Corbacho
 */
public class ReportFileView extends AbstractView<Report> {

	private final static Log log = LogFactory.getLog(ReportFileView.class);
	
	private JTextField fileName = new JTextField();
	private Icon addIcon;

	private ReportService reportService;

	private DataSource dataSource;

	public ReportFileView(Report model){
		setModel(model);
	}

	public ReportFileView(){
		this(new Report());
	}

	public void init() {
		getPanel();
		refresh();
	}


	@Override
	protected JComponent buildPanel() {
		Box box = Box.createHorizontalBox();

		fileName.setEditable(false);

		box.add(new JLabel("Fichero"));
		box.add(fileName);
		box.add(Box.createHorizontalStrut(5));

		box.add(Box.createHorizontalStrut(5));
		box.add(new JButton(new FileAction()));

		return box;
	}

	/**
	 * Add a file to this report
	 * @throws ReportingException 
	 */
	private void addAttachment() throws ReportingException {
		Report report = getModel();
		JFileChooser chooser = new JFileChooser();
		chooser.setFileFilter(new ReportFileFilter());

		if (JFileChooser.APPROVE_OPTION == chooser.showOpenDialog(getPanel())) {
			File file = chooser.getSelectedFile();
			try {

				getModel().setData(FileUtils.readFileToByteArray(file));
				getModel().setFileName(file.getName());
				fileName.setText(file.getName());
				
				// Update hidden value hasSQL to determine whether this report requires a Connection
				// to be displayed.

			    InputStream reportStream = null;
				try {
					reportStream = new FileInputStream(file);
				} catch (FileNotFoundException e) {
					throw new ReportingException("Error cargando fichero");				
				}

				JasperReport jasperReport = null;
				try {
					String suffix = ReportManager.getSuffix(file.getName());
					
					if (".jrxml".equals(suffix))
						jasperReport = JasperCompileManager.compileReport(reportStream);
					else if (".jasper".equals(suffix))
						jasperReport = (JasperReport) JRLoader.loadObject(reportStream);
					else {
						throw new ReportingException("Archivos del tipo " + suffix + " no pueden ser procesados");
					}
					
					
					if (jasperReport.getQuery() != null)
						report.setHasQuery(new Boolean(true));
					else report.setHasQuery(new Boolean(false));
				} catch (JRException e) {
					log.error(e);
					e.printStackTrace();
					throw new ReportingException("Error procesando fichero");
				}

				
			} catch (IOException e) {
				throw new ReportingException("No se puede leer el Fichero: " + file.getName());
			}
		}
	}



	/**
	 * GETTERS AND SETTERS 
	 */

	public Icon getAddIcon() {
		return addIcon;
	}

	public void setAddIcon(Icon addIcon) {
		this.addIcon = addIcon;
	}

	public ReportService getService() {
		return reportService;
	}

	public void setService(ReportService service) {
		this.reportService = service;
	}


	@SuppressWarnings("serial")
	private class FileAction extends AbstractAction {
		public FileAction() {
			putValue(Action.SMALL_ICON, addIcon);
		}

		public void actionPerformed(ActionEvent e) {
			try {
				addAttachment();
			} catch (ReportingException e1) {
				JOptionPane.showMessageDialog(getPanel(), e1.getMessage());
			}
		}

	}

	public JTextField getFileName() {
		return fileName;
	}

	public void setFileName(JTextField fileName) {
		this.fileName = fileName;
	}


	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}


	/**
	 * This class filters the types of files the system can handle to create reports
	 * 
	 * @author Jose A. Corbacho
	 *
	 */
	class ReportFileFilter extends FileFilter {
		private String[] allowedFileTypes = {"jasper", "jrxml"};

		public boolean accept(File file) {
			if (file.isDirectory())
				return true;
			String s = file.getName();
			int i = s.lastIndexOf('.');

			if (i > 0 && i < s.length() - 1){
				for (String fileType : allowedFileTypes)
					if (s.substring(i + 1).toLowerCase().equals(fileType))
						return true;
			}
			return false;	    
		}
		public String getDescription() {
			StringBuffer sb = new StringBuffer();
			for (String fileType: allowedFileTypes)
				sb.append(" " + fileType);
			return sb.toString();
		}	
	}

}
