
package org.jdal.reporting;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.zip.ZipFile;

import javax.persistence.Column;
import javax.persistence.Id;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdal.util.ZipFileUtils;

/**
 * @author Jose A. Corbacho
 */

public class Report {

	private static final Log log = LogFactory.getLog(Report.class);
	
	private Long id;
	private String name;
	private ReportType type;
	private String fileName;
	private byte[] data;
	private String description;
	/**  Whether this report is built using a SQL query */
	private Boolean hasQuery;
	
	/**
	 * @return a JasperReport
	 */
	public JasperReport newJasperReport() {
		String suffix = FilenameUtils.getExtension(getFileName());
		String prefix = FilenameUtils.getBaseName(getFileName());
		JasperReport jasperReport = null;
		
		try {
			File file =  File.createTempFile(prefix, "." + suffix);
			org.apache.commons.io.FileUtils.writeByteArrayToFile(file, data);
			
			if ("zip".equalsIgnoreCase(suffix)) {
				String dir = System.getProperty("java.io.tmpdir") + "/"  + prefix;
				ZipFileUtils.unzip(new ZipFile(file), dir);
				File dirFile = new File(dir);
				Iterator<File> iter = org.apache.commons.io.FileUtils.iterateFiles(dirFile, 
						new String[] {"jrxml", "jasper"}, false);
				while (iter.hasNext()) {
					file = iter.next();
					break;
				}
			}
			// now file points to jrxml or jasper file
			
			suffix = FilenameUtils.getExtension(file.getName());
			FileInputStream reportStream = new FileInputStream(file);
			
			if ("jrxml".equalsIgnoreCase(suffix))
				jasperReport = JasperCompileManager.compileReport(reportStream);
			else if ("jasper".equalsIgnoreCase(suffix))
				jasperReport = (JasperReport) JRLoader.loadObject(reportStream);
			
		} catch (Exception e) {
			log.error(e);
		}
		
		return jasperReport;
	}
	
	/**
	 * GETTERS AND SETTERS
	 */
	@Id
	
	public Long getId() {
		return id;
	}

	@Column(name="nombre")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setId(Long id) {
		this.id = id;
	}
	

	public ReportType getType() {
		return type;
	}

	public void setType(ReportType type) {
		this.type = type;
	}

	
	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}
	
	public void setFile(File file) throws IOException {
		data = org.apache.commons.io.FileUtils.readFileToByteArray(file);
	}
	

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}


	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
	public Boolean getHasQuery() {
		return hasQuery;
	}

	public void setHasQuery(Boolean hasQuery) {
		this.hasQuery = hasQuery;
	}

	
	@Override
	public String toString(){
		return this.name;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Report other = (Report) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
