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
package info.joseluismartin.soap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * SOAP Utility Library
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public abstract class SOAPUtils {

	@SuppressWarnings("unused")
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
