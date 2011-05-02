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

import java.util.Date;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonBeanProcessor;

/**
 * Convert a java.util.Date to JSONObject as "time: date.getTime()"
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class DateJsonBeanProcessor implements JsonBeanProcessor {

	/**
	 * Process bean method
	 * @param aBean Bean to process
	 * @param aJsonConfig JSON configuration
	 * @return Object converted into JSON object
	 */
	public JSONObject processBean(Object aBean, JsonConfig aJsonConfig) {
		JSONObject jo = null;
		
		if (aBean == null) {
			jo = new JSONObject(true);
		} else {
			jo = new JSONObject();
			jo.element("time", ((Date) aBean).getTime());
		}
		
		return jo;
	}

}
