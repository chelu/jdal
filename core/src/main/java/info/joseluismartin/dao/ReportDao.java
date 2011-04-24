package info.joseluismartin.dao;

import info.joseluismartin.reporting.Report;
import info.joseluismartin.reporting.ReportType;

import java.util.List;

/**
 * Report Dao Interface
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public interface ReportDao extends Dao<Report, Long> {

	/**
	 * Get all reports of a type.
	 * @param type the report type
	 * @return List with all reports of that type
	 */
	List<Report> getReportsByType(ReportType type);
}
