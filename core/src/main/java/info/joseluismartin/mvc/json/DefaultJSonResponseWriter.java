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
package info.joseluismartin.mvc.json;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSON;

/**
 * Default JSonResponseWriter implementation
 */
public class DefaultJSonResponseWriter implements JSonResponseWriter {
	
	/** contentType */
	private String contentType = "application/x-json;charset=UTF-8";
	/**
	 * Write json String to response
	 * @param aResponse HttpServletResponse
	 * @param aJson Object
	 * @throws IOException Exception
	 **/
	public void write(HttpServletResponse aResponse, 
			JSON aJson) throws IOException {
		// write json string to response
        aResponse.setContentType(contentType);
        aResponse.getWriter().write(aJson.toString());
	}

	/**
	 * @return the contentType
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * @param aContentType the contentType to set
	 */
	public void setContentType(String aContentType) {
		this.contentType = aContentType;
	}

}
