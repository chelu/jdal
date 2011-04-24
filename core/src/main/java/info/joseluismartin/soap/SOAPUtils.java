package info.joseluismartin.soap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * SOAP Utility Library
 * 
 * @author Jose Luis Martin - (jolmarting@matchmind.es)
 */
public abstract class SOAPUtils {

	private static Log log = LogFactory.getLog(SOAPUtils.class);
	
	/**
	 * Create a SOAPMessage from a XML InputStream
	 * @param is input stream to read xml from
	 * @return a SOAPMessage
	 */
//	public static SOAPMessage createSOAPMessage(InputStream is) {
//		SOAPMessage msg = null;
//		
//		try  {	
//			MessageFactory mf = MessageFactory.newInstance();
//			msg = mf.createMessage();
//			StreamSource prepMsg = new StreamSource(is);
//			msg.getSOAPPart().setContent(prepMsg);
//			msg.saveChanges();
//		} catch (SOAPException e) {
//			log.error(e);
//		}
//
//		return msg;
//	}
	
	/**
	 * Create a SOAPMessage from XML String
	 * @param xml
	 * @return
	 */
//	public static SOAPMessage createSOAPMessage(String xml) {
//		ByteArrayInputStream is = null;
//		try {
//			is = new ByteArrayInputStream(xml.getBytes("UTF-8"));
//		} catch (UnsupportedEncodingException e) {
//			log.error(e);
//		}
//		
//		return createSOAPMessage(is);	
//	}
	
	/**
	 * Get a SOAPMessage As String 
	 * @param message
	 * @return
	 */
//	public static String SOAPMessageAsString(SOAPMessage message) {
//		OutputStream os = new ByteArrayOutputStream();
//		try {
//			message.writeTo(os);
//		} catch (SOAPException e) {
//			log.error(e);
//		} catch (IOException e) {
//			log.error(e);
//		}
//		return os.toString();
//	}

	/** 
	 * Create a new SOAPMessage with document as body
	 * @param doc for body
	 * @return a new SOAPMessage
	 */
//	public static SOAPMessage createSOAPMessage(Document doc) {
//		SOAPMessage msg = null;
//		
//		try  {	
//			MessageFactory mf = MessageFactory.newInstance();
//			msg = mf.createMessage();
//			msg.getSOAPBody().addDocument(doc);
//			msg.saveChanges();
//		} catch (SOAPException e) {
//			log.error(e);
//		}
//
//		return msg;
//	}
}
