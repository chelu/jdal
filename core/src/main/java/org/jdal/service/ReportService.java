/**
 * 
 */
package org.jdal.service;

import java.util.List;

import org.jdal.reporting.Report;
import org.jdal.reporting.ReportType;


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
