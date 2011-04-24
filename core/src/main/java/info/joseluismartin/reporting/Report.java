
package info.joseluismartin.reporting;


import java.io.File;
import java.io.IOException;

import javax.persistence.Column;
import javax.persistence.Id;

import org.apache.commons.io.FileUtils;

/**
 * @author Jose A. Corbacho
 *
 */

public class Report{
	
	private Long id;
	private String name;
	private ReportType type;
	private String fileName;
	private byte[] data;
	private String description;
	/**  Whether this report is built using a SQL query */
	private Boolean hasQuery;
	
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
		data = FileUtils.readFileToByteArray(file);
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
