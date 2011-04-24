package info.joseluismartin.mvc.json;

import java.util.Date;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonBeanProcessor;

/**
 * Convert a java.util.Date to JSONObject as "time: date.getTime()"
 * 
 * @author Jose Luis Martin - (jolmarting@matchmind.es)
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
