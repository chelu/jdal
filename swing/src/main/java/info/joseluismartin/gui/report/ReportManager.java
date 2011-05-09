package info.joseluismartin.gui.report;

import info.joseluismartin.dao.Page;
import info.joseluismartin.reporting.Report;
import info.joseluismartin.reporting.ReportingException;
import info.joseluismartin.reporting.datasource.PageJRDatasourceAdapter;
import info.joseluismartin.service.PersistentService;
import info.joseluismartin.util.ZipFileUtils;
import info.joseluismartin.util.processor.FileProcessor;
import info.joseluismartin.util.processor.JasperReportFileProcessor;
import info.joseluismartin.util.processor.JasperReportXMLFileProcessor;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
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


/**
 * This class is used to generate and display reports.
 * Combine it with info.joseluismartin.util.processor.FileProcessor
 * 
 * @author Jose A. Corbacho
 * @author Jose Luis Martin (jlm@joseluismartin.info)
 */
public class ReportManager {
	
	/** apache common log */
	private static final Log log = LogFactory.getLog(ReportManager.class);

	/**
	 * Default ctor.
	 */
	public ReportManager(){
	}

	/**
	 * Display a report in <code>outputType</code> using the service and available ids to create  
	 * the data source
	 * @param report
	 * @param service
	 * @param ids
	 * @param outputType
	 */
	public void showReport(Report report, PersistentService<Object, Serializable> service, Object filter, String sortPropertyName, Page.Order sortOrder, String outputType) throws Exception{
		PageJRDatasourceAdapter dataSource = new PageJRDatasourceAdapter(true);
		Page<Object> page = new Page<Object>(100, 0, sortPropertyName, sortOrder);
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
		try {
			boolean continueWithReport = st.preprocessFile(report);
			if (continueWithReport) st.processFile(report, outputType, dataSource.getConnection());
		} catch (Exception e) {
			log.error(e);
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
		
		private Map<String, Object> parameters = new HashMap<String, Object>();;
		
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
		 * @param file
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
				if (!jrParameters.isEmpty()) {
					if (!showParameterDialog(jrParameters)) return false;
				}
			}
			return true;
		}
		
		private boolean showParameterDialog(Map<String, JRParameter> jrParameters){
			JRParameterEditorDialog dialog = new JRParameterEditorDialog(null, true,jrParameters);
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
					
				// The processor of this file
				FileProcessor fp = null;
				
				// TODO In case more file types are allowed in the system, create Factory
				if (".jasper".equals(suffix)){
					fp = new JasperReportFileProcessor();
				}
				else if (".jrxml".equals(suffix)){
					fp = new JasperReportXMLFileProcessor();
				}
				else {
					throw new ReportingException("Process not yet implemented for file type " + suffix);
				}
				
				
				fp.setParameters(parameters);

				setReportDataSource(fp, reportDataSource);

				// Process the file. This method sets its rawData attribute to the result of the processing.
				fp.processFile(file, outputType, report.getHasQuery());
				
				if (Desktop.isDesktopSupported()) {
					Desktop desktop = Desktop.getDesktop();
					File outputFile;
					try {
						outputFile = File.createTempFile(getPrefix(report.getFileName()), "." + outputType);
						outputFile.deleteOnExit();
						// Create the file with the raw data provided by the file processor 
						FileUtils.writeByteArrayToFile(outputFile, fp.getRawData());
						desktop.open(outputFile);
					} catch (IOException e) {
						throw new ReportingException("No ha sido posible abrir el fichero del informe: " + report.getFileName(), e);
					}
				}
				
			} catch (IOException e) {
				throw new ReportingException("No ha sido posible abrir el fichero del informe: " + report.getFileName(), e);
			}			
		}
		
	}
	
	public static void main(String[] args){
		String fileName = "/home/jose/Projects/telmma/reports/IncidencesByMonth.jasper";
		File file;
		file = new File(fileName);
		// The processor of this file
		FileProcessor fp = new JasperReportFileProcessor();

		System.out.println(System.getProperty("user.dir"));
		//See your driver documentation for the proper format of this string :
	    String DB_CONN_STRING = "jdbc:postgresql://localhost:5433/guardian2";
	    //Provided by your driver documentation. In this case, a MySql driver is used : 
	    String DRIVER_CLASS_NAME = "org.postgresql.Driver";
	    String USER_NAME = "guardian";
	    String PASSWORD = "guardian2010";
	    
	    Connection conn = null;
	    try {
	       Class.forName(DRIVER_CLASS_NAME).newInstance();
	    }
	    catch (Exception ex){
	    	log.error(ex);
	    }
	    

	    try {
	      conn = DriverManager.getConnection(DB_CONN_STRING, USER_NAME, PASSWORD);
	    }
	    catch (SQLException e){
	    	log.error(e);
	    }

		
		fp.setConnection(conn);
		// Process the file. This method sets its rawData attribute to the
		// result of the processing.
		fp.processFile(file, "pdf", true);

		if (Desktop.isDesktopSupported()) {
			Desktop desktop = Desktop.getDesktop();
			File outputFile;
			try {
				outputFile = File.createTempFile("IncidentList", "." + "pdf");
				outputFile.deleteOnExit();
				// Create the file with the raw data provided by the file
				// processor
				FileUtils.writeByteArrayToFile(outputFile, fp.getRawData());
				desktop.open(outputFile);
			} catch (IOException e) {
				log.error("No ha sido posible abrir el fichero del informe: "
								+ fileName + " " + e);
			}
		}
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
