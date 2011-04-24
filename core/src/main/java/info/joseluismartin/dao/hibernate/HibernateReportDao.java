/**
 * 
 */
package info.joseluismartin.dao.hibernate;

import info.joseluismartin.dao.ReportDao;
import info.joseluismartin.reporting.Report;
import info.joseluismartin.reporting.ReportType;

import java.util.List;

import org.hibernate.criterion.Restrictions;



/**
 * @author Jose A. Corbacho
 *
 */
public class HibernateReportDao extends HibernateDao<Report, Long> implements ReportDao {

	public HibernateReportDao() {
		super(Report.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<Report> getReportsByType(ReportType type) {
		return getSession().createCriteria(Report.class)
			.add(Restrictions.eq("type", type))
			.list();
	}

}
