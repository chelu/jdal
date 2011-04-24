/**
 * 
 */
package info.joseluismartin.logic;

import info.joseluismartin.dao.ReportDao;
import info.joseluismartin.reporting.Report;
import info.joseluismartin.reporting.ReportType;
import info.joseluismartin.service.ReportService;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Report Serivice Implementation
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class ReportManager extends PersistentManager<Report, Long> implements ReportService {

	private final static Log log = LogFactory.getLog(ReportManager.class);
	
	protected ReportDao dao;
	

	public List<Report> getReportsByType(ReportType type) {
		if (type == null) return getAll();
		return dao.getReportsByType(type);
	}

	public ReportDao getReporDao() {
		return dao;
	}

	public void setReportDao(ReportDao dao) {
		this.dao = dao;
	}
}
