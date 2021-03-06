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
package org.jdal.swing.report;


import java.awt.event.ActionEvent;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdal.reporting.Report;
import org.jdal.reporting.ReportingException;
import org.jdal.swing.TableRowAction;

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
	private ReportManager reportManager;
	private DataSource dataSource;
	private String reportOutputType;

	/* (non-Javadoc)
	 * @see info.joseluismartin.gui.TableRowAction#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		Report model = (Report) getRow();
		try {
			 reportManager.showReport(model, dataSource, reportOutputType);
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

	/**
	 * @return the reportManager
	 */
	public ReportManager getReportManager() {
		return reportManager;
	}

	/**
	 * @param reportManager the reportManager to set
	 */
	public void setReportManager(ReportManager reportManager) {
		this.reportManager = reportManager;
	}

}
