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
package org.jdal.vaadin;

import java.io.IOException;
import java.util.Enumeration;

import junit.framework.TestCase;

import org.jdal.beans.AppCtx;
import org.jdal.util.Serializer;
import org.jdal.vaadin.ui.table.PageableTable;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;


/**
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
@SuppressWarnings("rawtypes")
public class TestSerialization extends TestCase {
	
	private AbstractApplicationContext ctx = (AbstractApplicationContext) AppCtx.getInstance();
	MockHttpServletRequest request = new MockHttpServletRequest();  
	
	@Override
	public void setUp() {
		WebApplicationContextUtils.registerWebApplicationScopes(ctx.getBeanFactory());
	    ServletRequestAttributes attributes = new ServletRequestAttributes(request);  
	    RequestContextHolder.setRequestAttributes(attributes);  
	}


	public void testTableSerialization() throws IOException {	
		PageableTable table = (PageableTable) ctx.getBean("bookPageableTable");
		Enumeration names = request.getSession().getAttributeNames();

		while(names.hasMoreElements())
			System.out.println(names.nextElement());
		
		
		byte[] ser = Serializer.serialize(table);
		PageableTable t = (PageableTable) Serializer.deSerialize(ser);
		assertEquals(table.getService(), t.getService());
	}
}