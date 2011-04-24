/**
 * 
 */
package info.joseluismartin.dao;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;

/**
 * Base Filter for Bean Filters
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class BeanFilter implements Filter {
	
	private static final Log log = LogFactory.getLog(BeanFilter.class);
	private String filterName;
	private List<String> ignoredProperties = new ArrayList<String>();
	private static final String PARAMETER_MAP = "parameterMap";
	// FIXME: possible colision if a paramert have this name
	private static final String FILTER_NAME = "filterName";

	
	public BeanFilter() {
		this(BeanFilter.class.getSimpleName());
	}
	
	public BeanFilter(String filterName) {
		this.filterName = filterName;
		ignoredProperties.add(PARAMETER_MAP);
		ignoredProperties.add(FILTER_NAME);
		ignoredProperties.add("class");
	}
	
	/**
	 * {@inheritDoc}
	 * @see info.joseluismartin.dao.Filter#getParameterMap()
	 */
	public Map<String, Object> getParameterMap() {
		PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(getClass());
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		for (PropertyDescriptor pd : pds) {
			if (!ignoredProperties.contains(pd.getName()))
				try {
					map.put(pd.getName(), pd.getReadMethod().invoke(this, (Object[]) null));
				} catch (Exception e) {
					log.error(e);
				}
		}
		return map;	
	}

	/**
	 * {@inheritDoc}
	 * @see info.joseluismartin.dao.Filter#getName()
	 */
	public String getFilterName() {
		return filterName;
	}
	
	/**
	 * @param name the name to set
	 */
	public void setFilterName(String name) {
		this.filterName = name;
	}

}
