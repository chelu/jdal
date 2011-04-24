/**
 * 
 */
package info.joseluismartin.service;

import info.joseluismartin.reporting.Report;
import info.joseluismartin.reporting.ReportType;
import info.joseluismartin.service.PersistentService;

import java.util.List;


/**
 * @author Jose A. Corbacho
 *
 */
public interface ReportService extends PersistentService<Report, Long> {

	/**
	 * Get a list of reports matching the type
	 * @param type the type of the reports
	 * @return a list of reports
	 */
	public List<Report> getReportsByType(ReportType type);
}
