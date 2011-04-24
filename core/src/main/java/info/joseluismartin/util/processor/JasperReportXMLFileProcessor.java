/**
 * 
 */
package info.joseluismartin.util.processor;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRXlsExporter;

/**
 * @author jose
 *
 */
public class JasperReportXMLFileProcessor implements FileProcessor {

	private byte[] rawData;
	// The connection used to fill the report
	private Connection conn;
	
	private JRDataSource service;

	private Map parameters = new HashMap();
	/* (non-Javadoc)
	 * @see info.joseluismartin.util.processor.FileProcessor#processFile(java.io.File)
	 */
	public void processFile(File file, String outputType, boolean hasQuery) {
	    // File is XML
	    InputStream reportStream = null;
		try {
			reportStream = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    		
		// Compile the XML report
	    try {
			JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

			// Fill the report using the connection
			JasperPrint jasperPrint = null;
			
			// Should be done testing if the report has a query
			if (hasQuery) jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
			else jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, service);
			
			byte[] reportBin = null;
			if ("pdf".equals(outputType)){
				reportBin = JasperExportManager.exportReportToPdf(jasperPrint);
			}
			else if ("xls".equals(outputType)){
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				JRExporter exporter = new JRXlsExporter();
				exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
				exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
				
				exporter.exportReport();
				reportBin = baos.toByteArray();
			}
						 
			setRawData(reportBin);


		} catch (JRException e) {
			System.out.println("Error processing report: " + e);
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see info.joseluismartin.util.processor.FileProcessor#processFile(byte[])
	 */
	public void processFile(byte[] rawData) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see info.joseluismartin.util.processor.FileProcessor#getRawData()
	 */
	public byte[] getRawData() {
		return rawData;
	}

	public void setRawData(byte[] rawData){
		this.rawData = rawData;
	}
	
	
	public void setConnection(Connection conn) {
		this.conn = conn;
	}

	public void setService(JRDataSource source) {
		this.service = source;
	}

	public void setParameters(Map parameters){
		this.parameters = parameters;
	}
}
