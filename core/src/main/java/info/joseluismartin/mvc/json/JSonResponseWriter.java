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
 * One method interface to write JSon Object to response
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 *
 */
public interface JSonResponseWriter {
	/** 
	 * Write JSONObject to response
	 * @param aResponse HttpServletResponse
	 * @param aJson the JSONObject
	 * @throws IOException Exception
	 */
	void write(HttpServletResponse aResponse, JSON aJson)  throws IOException;
}
