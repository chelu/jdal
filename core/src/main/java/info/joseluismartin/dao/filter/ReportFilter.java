package info.joseluismartin.dao.filter;

import info.joseluismartin.dao.BeanFilter;
import info.joseluismartin.reporting.ReportType;

/**
 * Report Filter 
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class ReportFilter extends BeanFilter {
	
	/** filter name */
	private String name;
	/** filter type */
	private ReportType type;
	
	
	public ReportFilter(){
		this("report");
	}
	
	public ReportFilter(String name){
		setFilterName(name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ReportType getType() {
		return type;
	}

	public void setType(ReportType type) {
		this.type = type;
	}
}
