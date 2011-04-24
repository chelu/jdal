package info.joseluismartin.xml;

import java.io.StringReader;
import java.io.StringWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.JiBXException;

/**
 * XMLMapper implementation using JibX binding framework
 * 
 * @author Jose Luis Martin - (chelu.es@gmail.com)
 */
public class JibXXMLMapper implements XMLMapper {
	
	 private Log log = LogFactory.getLog(XMLMapper.class);

     private String bindingName = "";
     private String packageName = "";

     /**
      * Convert xml string to Java Objects
      */
     public Object deserialize(String xml) {
     IBindingFactory bfact = null;
     StringReader sr = new StringReader(xml);
     Object obj = null;
     try {
             bfact = BindingDirectory.getFactory(bindingName, packageName);
             obj = bfact.createUnmarshallingContext().unmarshalDocument(sr);
     } catch (JiBXException e) {
             log.error(e);
             throw new XMLMappingException("Can't deserialize xml: " + xml, e);
     }
             return obj;
     }

     /**
      * Convert Java Object to XML String
      */
     public String serialize(Object obj) {
             IBindingFactory bfact = null;
             StringWriter sw = new StringWriter();
             try {
                     bfact =  BindingDirectory.getFactory(obj.getClass());
                     bfact.createMarshallingContext().marshalDocument(obj, "UTF-8", null, sw );
             } catch (JiBXException e) {
                     log.error(e);
                     throw new XMLMappingException("Can't serialize object: " + obj.toString(), e );
             }
             return sw.toString();
     }

	/**
	 * @return the bindingName
	 */
	public String getBindingName() {
		return bindingName;
	}

	/**
	 * @param bindingName the bindingName to set
	 */
	public void setBindingName(String bindingName) {
		this.bindingName = bindingName;
	}

	/**
	 * @return the packageName
	 */
	public String getPackageName() {
		return packageName;
	}

	/**
	 * @param packageName the packageName to set
	 */
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
}
