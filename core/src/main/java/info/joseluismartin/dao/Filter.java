/**
 * 
 */
package info.joseluismartin.dao;

import java.util.Map;

/**
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 *
 */
public interface Filter {
	public String getFilterName();
	public Map<String, Object> getParameterMap();
}
