/*
 * Copyright 2008-2010 the original author or authors.
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
package info.joseluismartin.beans;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Minimalistic singleton with application context
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */


public class AppCtx {
	
	/** spring application context */
	private static ApplicationContext context = null;
	
	/** 
	 * Search on classpath for context definition files and return the application context
	 * 
	 * @return the ApplicationContext
	 */
	public synchronized static ApplicationContext getInstance() {
		if (context == null) {
			context = new ClassPathXmlApplicationContext(
					new String[] {
//							"classpath*:/applicationContext-resources.xml",
//							"classpath*:/applicationContext-dao.xml",
//							"classpath*:/applicationContext.xml",
							"classpath*:**/applicationContext*.xml" });
		}
		
		return context;
	}
}
