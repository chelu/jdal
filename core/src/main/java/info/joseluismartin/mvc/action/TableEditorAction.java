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
package info.joseluismartin.mvc.action;

import info.joseluismartin.dao.Page;
import info.joseluismartin.model.Filter;
import info.joseluismartin.mvc.json.DateJsonBeanProcessor;
import info.joseluismartin.mvc.mapper.DefaultViewModelMapper;
import info.joseluismartin.mvc.mapper.ViewModelMapper;
import info.joseluismartin.table.AlwaysValidValidator;
import info.joseluismartin.table.DataTableModel;
import info.joseluismartin.table.TableEditor;
import info.joseluismartin.table.TableEditorService;
import info.joseluismartin.table.ValidationResult;
import info.joseluismartin.table.Validator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;
import net.sf.json.util.PropertyFilter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

/**
 * Receive Table Editor Requests
 * 
 * Manage request and response because original code is for Struts 1.x
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 * @author Manuel Jesus Antunez
 */
// TODO: Use json plugin?

@SuppressWarnings("unchecked")
public class TableEditorAction implements Preparable, ModelDriven, 
	ServletRequestAware, ServletResponseAware, TableEditor, SessionAware {
	
	/** session name to store filter map */
	public static final String TEA_SESSION_FILTER_MAP = 
		"TEA_SESSION_FILTER_MAP";
	/** log */
	private static Log log = LogFactory.getLog(TableEditorAction.class);
	/** true if store filters in session */
	private boolean storeFiltersInSession = true;
	/** Class of the model*/
	private Class modelClass;
	/** Editor Service*/
	private TableEditorService tableEditorService;
	/** PropertyFilter*/
	private PropertyFilter propertyFilter;
	/** JsonConfig*/
	private JsonConfig config = new JsonConfig();
	/** JsonConfig*/
	private Validator validator = new AlwaysValidValidator();
	/** view model mapper */
	private ViewModelMapper mapper = new DefaultViewModelMapper();
	/** servlet request */
	private HttpServletRequest request;
	/** servlet response */
	private HttpServletResponse response;
	/** DataTable Model */
	private DataTableModel dtm = new DataTableModel();
	/** filter class */
	private Class filterClass;
	/** session */
	private Map session;
	
	
	/** Default Ctor */
	public TableEditorAction() {
		config.registerJsonBeanProcessor(
				Date.class, new  DateJsonBeanProcessor());
		config.registerJsonBeanProcessor(
				java.sql.Date.class, new  DateJsonBeanProcessor());
	}
	

	/**
	 * @return the model Classs
	 */
	public Class getModelClass() {
		return modelClass;
	}

	/**
	 * @param aClazz the clazz to set
	 */
	public void setModelClass(Class aClazz) {
		this.modelClass = aClazz;
	}
	
	/** 
	 * view BaseAction method, mapped to /view path
	 * @throws IllegalAccessException on error
	 * @throws InstantiationException on error
	 * @throws IOException on error
	 */
	public void view() throws InstantiationException, IllegalAccessException,
			IOException {
		
		Page page = dtm.getPage();
		if (dtm.getFilter() != null) {
			Filter filter = parseFilter(dtm.getFilter());
			page.setFilter(filter);
			saveFilterInSession(filter);
		}
		tableEditorService.getPage(page);
		Collection records = page.getData();
		records = mapper.toViewModelCollection(records);
		dtm.setRecords(records);
		dtm.setTotalRecords(page.getCount());
		
        JSONObject jsonObject = JSONObject.fromObject(dtm, config);
        
        if (log.isDebugEnabled()) {
        	log.debug("JSON Object:" +  jsonObject.toString());
        }
        // write json string to response
        response.addHeader("Pragma", "no-cache");
        response.addHeader("Cache-Control", "no-cache");             // HTTP/1.1
        response.addHeader("Cache-Control", "no-store");             // HTTP/1.1
        response.addHeader("Cache-Control", "must-revalidate");      // HTTP/1.1
        
        response.setContentType("application/x-json;charset=UTF-8");
        
        response.getWriter().write(jsonObject.toString());
        response.flushBuffer();
	}
	
	/**
	 * parse json string  on filter
	 * @param aFilter the json filter 
	 * @return the Filter model
	 * @throws IllegalAccessException On error
	 * @throws InstantiationException On error
	 */
	public Filter parseFilter(String aFilter)
			throws InstantiationException, IllegalAccessException {
		return (Filter)  JSONObject.toBean(JSONObject.fromObject(aFilter),
				filterClass.newInstance(), config);
	}


	/** 
	 * save BaseAction method, mapped to /save path
	 * @throws IOException on error
	 */
	public void save() throws IOException {

		Collection updated = getModelList(request, "records");
		Collection deleted = getModelList(request, "deleted");
		Collection news = getModelList(request, "news");
		
		//Validamos solo los updates y los news

		ValidationResult validationResult = null;
		Iterator  iter = null;
		Object obj = null;
		//Collection of errors 
		Collection colObjectErrors = new ArrayList();
		// update models
		iter = updated.iterator();
		while (iter.hasNext()) {
			obj = iter.next();
			validationResult = validator.validate(obj);
			if (validationResult.isValid()) {
				tableEditorService.save(obj);
			} else {
				colObjectErrors.add(validationResult);
			}
		}
		// insert news
		iter = news.iterator();
		while (iter.hasNext()) {
			obj = iter.next();
			validationResult = validator.validate(obj);
			if (validationResult.isValid()) {
				tableEditorService.save(obj);
			} else {
				colObjectErrors.add(validationResult);
			}
		}
		// delete delete
		iter = deleted.iterator();
		while (iter.hasNext()) { 
			tableEditorService.delete(iter.next());
		}

		if (!colObjectErrors.isEmpty()) {
			//Enviamos los errores a la pantalla
			response.setContentType("application/x-json;charset=UTF-8");
			response.getWriter().write(JSONArray.fromObject(
					colObjectErrors).toString());
			response.flushBuffer();
		}
	}

	/**
	 * @param aRequest HttpServletRequest
	 * @param aKey String
	 * @return Collection
	 */
	protected Collection getModelList(HttpServletRequest aRequest,
			String aKey) {
		String records = aRequest.getParameter(aKey);
	
		if (records == null) {
			return new ArrayList();
		}
		if (log.isDebugEnabled()) {
			log.debug(records);
		}
		JSONArray jsonArray = (JSONArray) JSONSerializer.toJSON(
				records, config);
		Collection viewModelCollection = JSONArray.toCollection(
				jsonArray, modelClass);
		List modelList = new ArrayList();
		Iterator iter =  viewModelCollection.iterator();
		while (iter.hasNext()) {
			modelList.add(mapper.fromViewModel(iter.next()));
		}
		
		return modelList;
	}
	
	
	/**
	 * @return the tableEditorService
	 */
	public TableEditorService getTableEditorService() {
		return tableEditorService;
	}

	/**
	 * @param aTableEditorService the tableEditorService to set
	 */
	public void setTableEditorService(TableEditorService aTableEditorService) {
		this.tableEditorService = aTableEditorService;
	}
	
	/**
	 * @return the validator
	 */
	public Validator getValidator() {
		return validator;
	}

	/**
	 * @param aValidator the validator to set
	 */
	public void setValidator(Validator aValidator) {
		this.validator = aValidator;
	}
	
	/**
	 * @return PropertyFilter
	 */
	public PropertyFilter getPropertyFilter() {
		return propertyFilter;
	}

	/**
	 * @param aPropertyFilter PropertyFilter
	 */
	public void setPropertyFilter(PropertyFilter aPropertyFilter) {
		this.propertyFilter = aPropertyFilter;
		config.setJsonPropertyFilter(aPropertyFilter);
	}

	/**
	 * @return the mapper
	 */
	public ViewModelMapper getMapper() {
		return mapper;
	}


	/**
	 * @param aMapper the mapper to set
	 */
	public void setMapper(ViewModelMapper aMapper) {
		this.mapper = aMapper;
	}

	/**
	 * Prepare
	 */
	public void prepare() {
				
	}

	/**
	 * Gets the model
	 * @return the model
	 */
	public Object getModel() {
		return dtm;
	}
	
	/**
	 * Sets the servlet request
	 * @param aRequest the request to set
	 */
	public void setServletRequest(HttpServletRequest aRequest) {
		this.request = aRequest;
	}

	/**
	 * Sets the servlet response
	 * @param aResponse the response to set
	 */
	public void setServletResponse(HttpServletResponse aResponse) {
		this.response = aResponse;
	}


	/**
	 * @return the filterClass
	 */
	public Class getFilterClass() {
		return filterClass;
	}

	/**
	 * @param aFilterClass the filterClass to set
	 */
	public void setFilterClass(Class aFilterClass) {
		this.filterClass = aFilterClass;
	}

	/**
	 * @param aSession the session to set
	 */
	public void setSession(Map session) {
		this.session = session;
		
	}


	/**
	 * @return the storeFiltersInSession
	 */
	public boolean isStoreFiltersInSession() {
		return storeFiltersInSession;
	}


	/**
	 * @param aStoreFiltersInSession the storeFiltersInSession to set
	 */
	public void setStoreFiltersInSession(boolean storeFiltersInSession) {
		this.storeFiltersInSession = storeFiltersInSession;
	}
	
	/**
	 * Save Filter in Session, for use in show* Actions
	 * @param aFilter object
	 */
	public void saveFilterInSession(Filter filter) {
		Map filterMap = (Map) session.get(TEA_SESSION_FILTER_MAP);
		if (filterMap == null) {
			filterMap = new Hashtable<String, Filter>();
			session.put(TEA_SESSION_FILTER_MAP,  filterMap);
		}
		filterMap.put(filter.name(), filter);
	}
}
