/**
 * 
 */
package info.joseluismartin.dao;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class DefaultFilter implements Filter {
	
	private String name;
	private Map<String, Object> parameterMap = new HashMap<String, Object>();

	/**
	 * {@inheritDoc}
	 * @see info.joseluismartin.dao.Filter#getName()
	 */
	public String getFilterName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * {@inheritDoc}
	 * @see info.joseluismartin.dao.Filter#getParameterMap()
	 */
	public Map<String, Object> getParameterMap() {
		return parameterMap;
	}


	public void setParameteterMap(Map<String, Object> parameterMap) {
		this.parameterMap = parameterMap;
	}

	public Object put(String key, Object value) {
		return parameterMap.put(key, value);
	}

}
