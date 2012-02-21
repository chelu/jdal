package xml;

import info.joseluismartin.xml.ValidationResult;
import info.joseluismartin.xml.XMLUtils;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import junit.framework.TestCase;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Test for XMLUtils
 * 
 * @author Jose Luis Martin
 */

public class TestXMLUtils extends TestCase {
	/** test document */
	private static final String TEST_DOCUMENT = 
		"/info/joseluismartin/xml/test/Test.xml";
	/** test schema */
	private static final String TEST_SCHEMA = 
		"/info.joseluismartin/xml/test/Test.xsd";

	/**
	 * 
	 * @throws ParserConfigurationException pce
	 * @throws SAXException  saxe
	 * @throws IOException ioe 
	 */
	public void notestNewDocumentFromResource () throws 
		ParserConfigurationException, SAXException, IOException {
		String test = "<?xml version=\"1.0\"?>\n<test xmlns=\"http://www.jdal.org/test\">\n    <name>Johnny</name>\n    <surname>Be Good</surname>\n</test>";

		Document doc = getTestDocument();
		String docString = XMLUtils.documentToString(doc);

		assertEquals(test, docString);
    }
	
	/**
	 * 
	 * @throws Exception any
	 */
	public void notestValidateSchema() throws Exception {
		InputStream stream = getClass().getResourceAsStream(TEST_SCHEMA);
		Source schemaSource = new StreamSource(stream);
		
		Schema schema = SchemaFactory
			.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)
			.newSchema(schemaSource);
		
		ValidationResult vr = XMLUtils
			.validateSchema(getTestDocument(), schema);
		
		assertTrue(vr.isValid());
	}
	
	/**
	 * Test copy node
	 */
	public void testCopyNode() {
		Document from = getTestDocument();
		Document to = XMLUtils.newDocument("<?xml version=\"1.0\"?><test/>");
		
		if (from != null) {	
			XMLUtils.copyNode(from.getDocumentElement(), to.getFirstChild());
			assertEquals("test", to.getElementsByTagName("test")
				.item(0).getNodeName());
		}
	}

	/**
	 * 
	 * @return the test document
	 */
	private Document getTestDocument() {
		return XMLUtils.newDocumentFromResource(TEST_DOCUMENT);
	}
	
	/** 
	 * 
	 */
	public void  testNothing() {
	}
	
}
