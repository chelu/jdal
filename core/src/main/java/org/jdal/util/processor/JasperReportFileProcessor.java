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
package org.jdal.util.processor;

import java.awt.Desktop;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;

import org.apache.commons.io.FileUtils;

/**
 * Process a jasper file using the connection in the system to retrieve the data.
 * 
 * @author Jose A. Corbacho
 * @since 1.0
 */
public class JasperReportFileProcessor implements FileProcessor {

	private byte[] rawData;
	// The connection used to fill the report
	private Connection conn;
	
	private JRDataSource service;

	private Map<String, Object> parameters = new HashMap<String, Object>();
	
	public JasperReportFileProcessor(){
	}
	
	public JasperReportFileProcessor(Connection conn){
		this.conn = conn;
	}
	
	public void processFile(File file, String outputType, boolean hasQuery) {
		try {
			// Fill the report using the connection
			JasperPrint print = null;
			if (hasQuery) print = JasperFillManager.fillReport(
					new FileInputStream(file), parameters, conn);
			else print = JasperFillManager.fillReport(
					new FileInputStream(file), parameters, service);
			
			// Create a PDF exporter
			// Configure the exporter (set output file name and print object)
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			
			// By default, PDF
			JRExporter exporter = new JRPdfExporter();
			
			if ("pdf".equals(outputType)){
				exporter = new JRPdfExporter();
			}
			else if ("xls".equals(outputType)){
				exporter = new JRXlsExporter();
			}
			
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
			exporter.exportReport();
			setRawData(baos.toByteArray());
			
		} catch (JRException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void processFile(byte[] rawData) {
		// TODO Auto-generated method stub
	}
	
	
	public void setRawData(byte[] data) {
		this.rawData = data;
	}

	public byte[] getRawData() {
		return rawData;
	}

	public void setConnection(Connection conn) {
		this.conn = conn;
	}
	
	
	public static void main(String[] args){
			try {
				JasperReport report = JasperCompileManager.compileReport("/home/jose/Projects/telmma/Documents/Code/testParameters.jrxml");
				
				System.out.println("Query en el jasper: " + report.getQuery().getText());
				for (JRParameter param : report.getParameters()){
					if (!param.isSystemDefined())
						System.out.println(param.getName());
				}
				Map<String, Object> parameters = new HashMap<String, Object>();				
				// TEST
				parameters.put("NombreCiudad", "Huelva");

		
				JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, new JREmptyDataSource());
				byte[] reportBin = JasperExportManager.exportReportToPdf(jasperPrint); 
								
				if (Desktop.isDesktopSupported()) {
					Desktop desktop = Desktop.getDesktop();
					File outputFile;
					try {
						outputFile = File.createTempFile("simple_report", ".pdf");
						//outputFile.deleteOnExit();
						// Create the file with the raw data provided by the file processor 
						FileUtils.writeByteArrayToFile(outputFile, reportBin);
						
						System.out.println("OutputFile -> " + outputFile.getName() + " " + outputFile.getTotalSpace());
						
						desktop.open(outputFile);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			} catch (JRException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}

	public void setService(JRDataSource service) {
		this.service = service;
	}
	
	public void setParameters(Map<String, Object> parameters){
		this.parameters = parameters;
	}
}
