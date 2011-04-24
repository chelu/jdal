package info.joseluismartin.xml;

import java.io.File;
import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.commons.io.FileUtils;
import org.xml.sax.SAXException;

/**
 * Command Line Xml utility
 * 
 * @author Jose Luis Martin - (jolmarting@matchmind.es)
 */
public class Main {
	
	private static String filename = "/tmp/ticket.xml";
	private static String schema = "/home/chelu/src/otrs/schema/otrs.xsd";

	public static void main(String[] args) throws Exception {
		validateXML(filename, schema);
		
	}

	public static void validateXML(String xmlPath, String schemaPath) throws SAXException, IOException {
		SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Schema schema = schemaFactory.newSchema(new File(schemaPath));
		ValidationResult vr = XMLUtils.validateSchema(
				FileUtils.readFileToString(new File(xmlPath), "UTF-8"), schema);
		
		String result = vr.isValid() ? "Valido" : "Inválido\n";
		System.out.println(result  + vr.getMessage());
	}
}
