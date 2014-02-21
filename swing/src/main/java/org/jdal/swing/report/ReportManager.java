package org.jdal.swing.report;


import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.zip.ZipFile;

import javax.sql.DataSource;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.SimpleFileResolver;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdal.dao.Dao;
import org.jdal.dao.Page;
import org.jdal.reporting.Report;
import org.jdal.reporting.ReportingException;
import org.jdal.reporting.datasource.PageJRDatasourceAdapter;
import org.jdal.util.ZipFileUtils;
import org.jdal.util.processor.FileProcessor;
import org.jdal.util.processor.JasperReportFileProcessor;
import org.jdal.util.processor.JasperReportXMLFileProcessor;


/**
 * This class is used to generate and display reports.
 * Combine it with info.joseluismartin.util.processor.FileProcessor
 * 
 * @author Jose A. Corbacho
 */
public abstract class ReportManager {
	
	/** apache common log */
	private static final Log log = LogFactory.getLog(ReportManager.class);
	private DataSource dataSource;

	/**
	 * Default ctor.
	 */
	public ReportManager(){
	}

	/**
	 * Display a report in <code>outputType</code> using the service and available ids to create  
	 * the data source
	 * @param report report to display
	 * @param filter filter to use when query data to persistent service
	 * @param sortPropertyName sort property name
	 * @param sortOrder sort order (ASC, DESC)
	 * @param service persistent service to use
	 * @param outputType report output type (pdf, xml)
	 */
	public void showReport(Report report, Dao<Object, Serializable> service, Object filter, String sortPropertyName, Page.Order sortOrder, String outputType) throws Exception{
		PageJRDatasourceAdapter dataSource = new PageJRDatasourceAdapter(true);
		Page<Object> page = new Page<Object>(10, 0, sortPropertyName, sortOrder);
		page.setFilter(filter);
		page.setPageableDataSource(service);
		dataSource.setPage(page);
		
		ProcessFileStrategy st = new JRDataSourceFileStrategy();
		try {
			st.processFile(report, outputType, dataSource);
		} catch (IOException e) {
			log.error(e);
		}

	}
	
	
	/**
	 * Displays the report in a new window as a file
	 * @param report the report to be displayed
	 * @throws ReportingException 
	 */
	public void showReport(Report report, DataSource dataSource, String outputType) throws ReportingException {
		ProcessFileStrategy st = new ConnectionFileStrategy();
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			boolean continueWithReport = st.preprocessFile(report);
			if (continueWithReport) st.processFile(report, outputType, conn);
		} catch (Exception e) {
			log.error(e);
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					log.error(e);
				}
		}
	}
	
	
	public static String getPrefix(String fileName) {
		String name = fileName != null ? fileName : "";
		int index = name.lastIndexOf('.');
		return index == -1 ? name : name.substring(0, name.lastIndexOf('.'));
	}

	public static String getSuffix(String fileName) {
		String name = fileName != null ? fileName : "";
		int index = name.lastIndexOf('.');
		return index == -1 ? "" : name.substring(index);
	}
	
	protected abstract JRParameterEditorDialog createEditorDialog();

	
	/**
	 * Implementation of FileStrategy using a JRDataSource as report data source
	 * @author Jose A. Corbacho
	 *
	 */
	public class JRDataSourceFileStrategy extends ProcessFileStrategy {

		@Override
		public void setReportDataSource(FileProcessor fp, Object obj) {
			fp.setService((JRDataSource)obj);
		}
		
	}

	/**
	 * Implementation of FileStrategy using a Connection as report data source
	 * @author Jose A. Corbacho
	 *
	 */
	public class ConnectionFileStrategy extends ProcessFileStrategy{
		@Override
		public void setReportDataSource(FileProcessor fp, Object obj) {
			fp.setConnection((Connection)obj);
		}		
	}

	/**
	 * Strategy to process the jasper file. Different strategies depending on the system used to retrieve
	 * the data (JRDataSource, java.sql.Connection)
	 * @author Jose A. Corbacho
	 *
	 */
	public abstract class ProcessFileStrategy {
		
		private Map<String, Object> parameters = new HashMap<String, Object>();
		private boolean interactive = true;
		private FileProcessor fileProcessor;
		
		/**
		 * Set the data source in this file processor.
		 * @param fp
		 * @param obj
		 */
		public abstract void setReportDataSource(FileProcessor fp, Object obj);
		
		/**
		 * Pre-process file. In case this report includes params, display it.
		 * The fact a report has params can be determined at the moment of registering
		 * it the system. At this point, it could be possible to have this information
		 * thus making this process faster
		 * @param report report to process
		 */
		public boolean preprocessFile(Report report){
			if (log.isDebugEnabled())
				log.debug("ReportManager. Preprocess file: hasQuery -> " + report.getHasQuery());
		
			if (report.getHasQuery()) {
				JasperReport jasperReport = report.newJasperReport();

				if (log.isDebugEnabled())
						log.debug("Parameters in jasperReport");
				
				Map<String, JRParameter> jrParameters = new HashMap<String, JRParameter>();
				for (JRParameter param : jasperReport.getParameters()) {
					if (!param.isSystemDefined() && param.isForPrompting()){
						if (log.isDebugEnabled())
							log.debug("Param to fill from paramEntry: " + param.getName());
						jrParameters.put(param.getName(), param);					
					}
				}
				if (!jrParameters.isEmpty() && interactive) {
					if (!showParameterDialog(jrParameters)) return false;
				}
			}
			return true;
		}
		
		private boolean showParameterDialog(Map<String, JRParameter> jrParameters){
			JRParameterEditorDialog dialog = createEditorDialog();
			dialog.setParameters(jrParameters);
			dialog.initialize();
			dialog.setVisible(true);
			
			if (dialog.isCanceled()) return false;
			
			this.parameters = dialog.getReturnValues();
			return true;
		}

		/**
		 * Process the report with this reportDataSource. The data source can be JRDataSource or Connection
		 * @param report
		 * @param outputType
		 * @param reportDataSource
		 * @throws ReportingException
		 * @throws IOException
		 */
		public void processFile(Report report, String outputType, Object reportDataSource ) throws ReportingException, IOException{
			if (report == null || report.getData() == null)
				return;

			File file;
			String suffix = getSuffix(report.getFileName());
			
			try {
				file = File.createTempFile(getPrefix(report.getFileName()), suffix);
				file.deleteOnExit();
				FileUtils.writeByteArrayToFile(file, report.getData());
			
				if (".zip".equalsIgnoreCase(suffix)) {
					String dir = System.getProperty("java.io.tmpdir") + "/" + getPrefix(report.getFileName());
					ZipFileUtils.unzip(new ZipFile(file), dir);
					File dirFile = new File(dir);
					parameters.put(JRParameter.REPORT_FILE_RESOLVER, new ReportFileResolver(dirFile));
					Iterator<File> iter = FileUtils.iterateFiles(dirFile, new String[] {"jrxml", "jasper"}, false);
					while (iter.hasNext()) {
						file = iter.next();
						suffix = getSuffix(file.getName());
						break;
					}
				}
					
				// TODO In case more file types are allowed in the system, create Factory
				if (".jasper".equals(suffix)){
					fileProcessor = new JasperReportFileProcessor();
				}
				else if (".jrxml".equals(suffix)){
					fileProcessor = new JasperReportXMLFileProcessor();
				}
				else {
					throw new ReportingException("Process not yet implemented for file type " + suffix);
				}
				
				
				fileProcessor.setParameters(parameters);

				setReportDataSource(fileProcessor, reportDataSource);

				// Process the file. This method sets its rawData attribute to the result of the processing.
				fileProcessor.processFile(file, outputType, report.getHasQuery());
				
				if (interactive && Desktop.isDesktopSupported()) {
					Desktop desktop = Desktop.getDesktop();
					File outputFile;
					try {
						outputFile = File.createTempFile(getPrefix(report.getFileName()), "." + outputType);
						outputFile.deleteOnExit();
						// Create the file with the raw data provided by the file processor 
						FileUtils.writeByteArrayToFile(outputFile, fileProcessor.getRawData());
						desktop.open(outputFile);
					} catch (IOException e) {
						throw new ReportingException("No ha sido posible abrir el fichero del informe: " + report.getFileName(), e);
					}
				}
				
			} catch (IOException e) {
				throw new ReportingException("No ha sido posible abrir el fichero del informe: " + report.getFileName(), e);
			}			
		}

		/**
		 * @return the interactive
		 */
		public boolean isInteractive() {
			return interactive;
		}

		/**
		 * @param interactive the interactive to set
		 */
		public void setInteractive(boolean interactive) {
			this.interactive = interactive;
		}

		/**
		 * @return the parameters
		 */
		public Map<String, Object> getParameters() {
			return parameters;
		}

		/**
		 * @param parameters the parameters to set
		 */
		public void setParameters(Map<String, Object> parameters) {
			this.parameters = parameters;
		}

		/**
		 * @return the fileProcessor
		 */
		public FileProcessor getFileProcessor() {
			return fileProcessor;
		}

		/**
		 * @param fileProcessor the fileProcessor to set
		 */
		public void setFileProcessor(FileProcessor fileProcessor) {
			this.fileProcessor = fileProcessor;
		}

		
	}

	/**
	 * @return the dataSource
	 */
	public DataSource getDataSource() {
		return dataSource;
	}

	/**
	 * @param dataSource the dataSource to set
	 */
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
}

class ReportFileResolver extends SimpleFileResolver {

	/**
	 * @param parentFolder
	 */
	public ReportFileResolver(File parentFolder) {
		super(parentFolder);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public File resolveFile(String fileName) {
		return super.resolveFile(FilenameUtils.getName((fileName)));
	}
	
	
}
