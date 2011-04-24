package info.joseluismartin.mvc.json;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSON;

/**
 * Default JSonResponseWriter implementation
 */
public class DefaultJSonResponseWriter implements JSonResponseWriter {
	
	/** contentType */
	private String contentType = "application/x-json;charset=UTF-8";
	/**
	 * Write json String to response
	 * @param aResponse HttpServletResponse
	 * @param aJson Object
	 * @throws IOException Exception
	 **/
	public void write(HttpServletResponse aResponse, 
			JSON aJson) throws IOException {
		// write json string to response
        aResponse.setContentType(contentType);
        aResponse.getWriter().write(aJson.toString());
	}

	/**
	 * @return the contentType
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * @param aContentType the contentType to set
	 */
	public void setContentType(String aContentType) {
		this.contentType = aContentType;
	}

}
