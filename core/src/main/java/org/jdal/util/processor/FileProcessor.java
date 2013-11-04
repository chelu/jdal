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
package org.jdal.util.processor;

import java.io.File;
import java.sql.Connection;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;

/**
 * @author Jose A. Corbacho
 *
 */
public interface FileProcessor {

	/**
	 * Process the parameter file
	 * @param file
	 */
	public void processFile(File file, String outputType, boolean hasQuery);

	/**
	 * Process the parameter file passed as raw data
	 * @param rawData
	 */
	public void processFile(byte[] rawData);
	
	/**
	 * Returns a raw set of the data of this file
	 * @return report as raw data
	 */
	public byte[] getRawData();
	
	
	/**
	 * Set the database connection to be used to process the file
	 * @param conn
	 */
	public void setConnection(Connection conn);
	
	public void setService(JRDataSource source);
	
	public void setParameters(Map<String, Object> parameters);
}
