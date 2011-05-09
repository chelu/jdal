/*
 * Copyright 2009-2011 Jose Lus Martin.
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
package info.joseluismartin.reporting.datasource;

import info.joseluismartin.dao.Page;
import info.joseluismartin.dao.PageableDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.data.JRAbstractBeanDataSource;

import org.apache.commons.beanutils.BeanUtils;

/**
 * Adapter to use PageableDataSource and Pages as JRDataSource.
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class PageJRDatasourceAdapter extends JRAbstractBeanDataSource {
	
	private Page<Object> page;
	private Object currentObject;
	private int index = 0;
	
	/**
	 * 
	 */
	public PageJRDatasourceAdapter(PageableDataSource<Object> ds) {
		this(true);
		page = new Page<Object>();
		page.setPageableDataSource(ds);
		
	}
	
	/**
	 * @param isUseFieldDescription
	 */
	public PageJRDatasourceAdapter(boolean isUseFieldDescription) {
		super(isUseFieldDescription);
	}

	/**
	 * {@inheritDoc}
	 */
	public void moveFirst() throws JRException {
		page.firstPage();
	}

	/**
	 * {@inheritDoc}
	 */
	public Object getFieldValue(JRField field) throws JRException {
		try {
			return BeanUtils.getProperty(currentObject, field.getName());
		} catch (Exception e) {
			// rethrow
			throw new JRException(e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean next() throws JRException {
		if (index < page.getPageSize()) {
			getCurrentObject();
			return true;
		}
	
		if (page.hasNext()) {
			page.nextPage();
			index = 0;
			getCurrentObject();
			return true;
		}
		
		return false;
		
	}

	private void getCurrentObject() {
		currentObject = page.getData().get(index++);
	}

	/**
	 * @return the page
	 */
	public Page<Object> getPage() {
		return page;
	}

	/**
	 * @param page the page to set
	 */
	public void setPage(Page<Object> page) {
		this.page = page;
	}
}
