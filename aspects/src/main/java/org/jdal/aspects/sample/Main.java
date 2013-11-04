/*
 * Copyright 2009-2011 the original author or authors.
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
package org.jdal.aspects.sample;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdal.beans.AppCtx;
import org.jdal.dao.Dao;

/**
 * HibernateLazyGuard sample.
 *  
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class Main {
	
	private static final Log log = LogFactory.getLog(Main.class);
	
	@SuppressWarnings("unchecked")
	public static void main (String[] arg) {
		Dao<Dog, Long> dogDao = (Dao<Dog, Long>) AppCtx.getInstance().getBean("dogDao");
		Dog dog = dogDao.get(1l);
		
		// will throw exception without HibernateLazyGuard.aj
		for (Cat cat : dog.getCats()) 
			log.info(cat.getName());
	}

}
