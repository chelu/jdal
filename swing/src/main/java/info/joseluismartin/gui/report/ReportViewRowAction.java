/**
 * 
 */
package info.joseluismartin.gui.report;

import info.joseluismartin.gui.TableRowAction;
import info.joseluismartin.reporting.Report;
import info.joseluismartin.reporting.ReportManager;
import info.joseluismartin.reporting.ReportingException;

import java.awt.event.ActionEvent;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Jose A. Corbacho
 *
 */
public class ReportViewRowAction extends TableRowAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2255283318836584357L;
	private final static Log log = LogFactory.getLog(TableRowAction.class);
	
	private DataSource dataSource;
	private String reportOutputType;

	/* (non-Javadoc)
	 * @see info.joseluismartin.gui.TableRowAction#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		Report model = (Report) getRow();
		try {
			 new ReportManager().showReport(model, dataSource, reportOutputType);
		} catch (ReportingException re) {
			log.error(e);
		} 

	}

	/***********************************
	 * GETTERS AND SETTERS
	 ***********************************/
	

	/**
	 * Getter for dataSource attribute
	 * @return the dataSource
	 */
	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public String getReportOutputType() {
		return reportOutputType;
	}

	public void setReportOutputType(String reportOutputType) {
		this.reportOutputType = reportOutputType;
	}

}
