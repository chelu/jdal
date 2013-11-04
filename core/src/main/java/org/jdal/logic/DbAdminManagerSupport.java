/*
 * Copyright 2009-2012 the original author or authors.
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
package org.jdal.logic;

import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlProvider;
import org.springframework.jdbc.core.StatementCallback;


/**
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 *
 */
public class DbAdminManagerSupport {
	private static final Log log = LogFactory.getLog(DbAdminManagerSupport.class);

	private DataSource dataSource;
	private JdbcTemplate template;
	
	
	public boolean execute(final String sql) throws DataAccessException {
		if (log.isDebugEnabled()) {
			log.debug("Executing SQL statement [" + sql + "]");
		}
		class ExecuteStatementCallback implements StatementCallback<Boolean>, SqlProvider {
			public Boolean doInStatement(Statement stmt) throws SQLException {
				return stmt.execute(sql);
			}
			public String getSql() {
				return sql;
			}
		}
		
		return  template.execute(new ExecuteStatementCallback());
	}

	/**
	 * @return the dataSource
	 */
	public DataSource getDataSource() {
		return dataSource;
	}

	/**
	 * @param dataSource the dataSource to set
	 */
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * @return the template
	 */
	public JdbcTemplate getTemplate() {
		return template;
	}

	/**
	 * @param template the template to set
	 */
	public void setTemplate(JdbcTemplate template) {
		this.template = template;
	}
}
