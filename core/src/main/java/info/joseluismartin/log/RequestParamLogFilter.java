package info.joseluismartin.log;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

/**
 * Servlet 2.3 Filter that log request param
 * 
 * @author Jose Luis Martin - (chelu.es@gmail.com)
 *
 */
@SuppressWarnings("unchecked")
public class RequestParamLogFilter extends CommonsRequestLoggingFilter {
	
	private static Log log = LogFactory.getLog(RequestParamLogFilter.class);
	

	@Override
	public void beforeRequest(HttpServletRequest request, String message) {
		super.beforeRequest(request, message);
		Map parameters = request.getParameterMap();

		if (log.isDebugEnabled())
			log.debug("Request Parameters:\n " + getParameterString(parameters));
	}

	/**
	 * Build String from parameter map
	 * @param parameters
	 * @return
	 */
	private String getParameterString(Map parameters) {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
			for (Object key : parameters.keySet()) {
				sb.append(key);
				sb.append("=");
				sb.append(getObjectString(parameters.get(key)));
				sb.append(",");
			}
		
			String mapString = sb.toString();

			if (parameters.size() > 0)
				mapString = mapString.substring(0, mapString.length() - 1);
			
			return mapString + "}";
	}

	/** 
	 * Build String from String[] 
	 * @param object
	 * @return
	 */
	private String getObjectString(Object object) {
		StringBuilder sb = new StringBuilder();
		if (object instanceof String) {
			return (String) object;
		}
		else if (object instanceof String[]) {
			String[] objs =  (String[]) object;
			sb.append("[");
			for (int i = 0; i < objs.length ; i++) {
				sb.append(objs[i]);
				if (i < (objs.length - 1))
					sb.append(",");
			}
			sb.append("]");
		}
		
		return sb.toString();
	}

}
