package info.joseluismartin.mvc.json;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSON;

/**
 * One method interface to write JSon Object to response
 * @author Jose Luis Martin - (chelu.es@gmail.com)
 *
 */
public interface JSonResponseWriter {
	/** 
	 * Write JSONObject to response
	 * @param aResponse HttpServletResponse
	 * @param aJson the JSONObject
	 * @throws IOException Exception
	 */
	void write(HttpServletResponse aResponse, JSON aJson)  throws IOException;
}
