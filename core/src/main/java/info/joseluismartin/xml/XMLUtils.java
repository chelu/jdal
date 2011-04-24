package info.joseluismartin.xml;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;



/**
 * XML DOM Utility static lib for XML (Thread safe) 
 * Cache the DOM DocumentBuilder in thread local variable.
 *  
 * @author Jose Luis Martin - (jolmarting@matchmind.es)
 */
@SuppressWarnings("unchecked")
public abstract class XMLUtils {
	
	/** commons log */
	private static Log log = LogFactory.getLog(XMLUtils.class);
	
	/** Cache a DocumentBuilder */
	private static ThreadLocal dbLocal = new ThreadLocal();
	
	/**
	 * Get a org.w3c.Document from resource String
	 * @param res ResourceString
	 * @return a new org.w3c.Document
	 */
	public static Document newDocumentFromResource(String res) {
	
		URL url = XMLUtils.class.getResource(res);
		return url != null ? newDocumentFromFile(url.getFile()) : null;
	}
	
	/**
	 * Get a new org.w3c.Document from files String
	 * @param path ResourceString
	 * @return a new org.w3c.Document
	 */
	public static Document newDocumentFromFile(String path) {
		
		Document doc = null;
		try {
			doc = getDocumentBuilder().parse(new File(path));
		} catch (SAXException e) {
			log.error(e);
		} catch (IOException e) {
			log.error(e);
		}
		return doc;
	}
	
	/**
	 * Get a new org.w3c.Document from xml string (UTF8)
	 * @param xml string with xml
	 * @return a new org.w3c.Document
	 */
	public static Document newDocument(String xml) {
		Document doc = null;
		DocumentBuilder db = getDocumentBuilder();
		try {
			doc = db.parse(new ByteArrayInputStream(xml.getBytes("UTF8")));
		} catch (Exception e) {
			log.error(e);
		}
		return doc;
	}
	
	/**
	 * Get new instance of DOM Document
	 * @return a new instance of DOM Document
	 */
	public static Document newDocument() {
		return getDocumentBuilder().newDocument();
	}
	
	/**
	 * Get a DocumentBuilder from a ThreadLocal variable, 
	 * Create if null
	 * 
	 * @return DocumentBuilder
	 */
	public static DocumentBuilder getDocumentBuilder() {
		DocumentBuilder db = (DocumentBuilder) dbLocal.get();
		if (db == null) {
			DocumentBuilderFactory dbf = 
				DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);
			// dbf.setValidating(true);
			try {
				db = dbf.newDocumentBuilder();
				dbLocal.set(db);
			} catch (ParserConfigurationException e) {
				log.error(e);
			}
		}
		return db;
	}
	
	/**
	 * Serialize a Document to a String using JAXP with identation (4 spaces) 
	 * @param doc to serialize
	 * @return xml string
	 */
	public static String prettyDocumentToString(Document doc) {
		StringWriter writer = new StringWriter();
		OutputFormat out = new OutputFormat();
		out.setOmitXMLDeclaration(true);
		out.setIndenting(true);
		out.setIndent(4);
		out.setLineSeparator(System.getProperty("line.separator"));
		out.setLineWidth(Integer.MAX_VALUE);
		
		XMLSerializer serializer = new XMLSerializer(writer, out);
		try {
			Element rootElement = doc.getDocumentElement();
			serializer.serialize(rootElement);
		} catch (IOException e) { 
			log.error(e);
		}
		return writer.toString();
	} 
	
	/**
	 * Serialize a Document to a Sring using JAXP without formating 
	 * 
	 * @param doc Document to serialize
	 * @return Strin with serialized doc
	 */
	public static String documentToString(Document doc) {
		return elementToString(doc.getDocumentElement());
	}

	/** 
	 * Element to String without format
	 * 
	 * @param elto to serialize
	 * @return serialized elto
	 */
	public static String elementToString(Element elto) {
		StringWriter writer = new StringWriter();
		OutputFormat of = new OutputFormat();
		of.setOmitXMLDeclaration(true);
		XMLSerializer serializer = new XMLSerializer(writer, of);
		serializer.setNamespaces(true);
		try {
			serializer.serialize(elto);
		} catch (IOException ioe) {
			log.error(ioe);
		}
		return writer.toString();
	}
	
	/**
	 * Get a DOM Element from xml string
	 * @param xml the string to parse 
	 * @return a new DOM Element
	 */
	public static Element stringToElement(String xml) {
		Document doc = newDocument(xml);
		Element elto = doc.getDocumentElement();
		return (Element) elto.cloneNode(true);
		
	}

	
	/**
	 * Copy one node to another node.
	 * 
	 * @param source source Node
	 * @param dest destination Node
	 * @return destination Node
	 */
	public static synchronized Node copyNode(Node source, Node dest) {
		if (source.getNodeType() == Node.TEXT_NODE) {
			Text tn = dest.getOwnerDocument()
				.createTextNode(source.getNodeValue());
			return tn;
		}
		
		Node attr = null;
		NamedNodeMap attrs = source.getAttributes();
		
		if (attrs != null) {
			for (int i = 0; i < attrs.getLength(); i++) {
				attr = attrs.item(i);
				((Element) dest).setAttribute(
						attr.getNodeName(), attr.getNodeValue());
			}
		}

		Node child = null;
		NodeList list = source.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			child = list.item(i);
			if (!(child instanceof Text)) {
				Element en = dest.getOwnerDocument().
				createElementNS(child.getNamespaceURI(), child.getNodeName());

				if (child.getNodeValue() != null) {
					en.setNodeValue(child.getNodeValue());
				}

				Node n = copyNode(child, en);
				dest.appendChild(n);
			} else if (child instanceof CDATASection) {
				CDATASection cd = dest.getOwnerDocument().createCDATASection(
						child.getNodeValue());
				dest.appendChild(cd);
			} else {
				Text tn = dest.getOwnerDocument()
					.createTextNode(child.getNodeValue());
				dest.appendChild(tn);
			}
		}	
		return dest;
	}
	
	
	/** 
	 * Creates a new Document with name and namespace
	 * @param ns namespace
	 * @param name name
	 * @return
	 */
	public static Document newDocument(String ns, String name) {
		return  getDocumentBuilder().getDOMImplementation()
			.createDocument(ns, name, null);
	}
	
	/** 
	 * Validate a Document with schema 
	 * @param xml to validate
	 * @param schema to falidate from
	 * @return a ValidationResult with validation result
	 */
	public static ValidationResult validateSchema(String xml, Schema schema) {
		
		try {
			ByteArrayInputStream bais = 
				new ByteArrayInputStream(xml.getBytes("UTF-8"));
			Source xmlSource = new StreamSource(bais);
			schema.newValidator().validate(xmlSource);
		} catch (Exception e) {
			log.warn(e);
			return new ValidationResult(false, e.getMessage());
		}
		
		return new ValidationResult(true);
	}
	
	/**
	 * Validate Document on schema
	 * @param doc to validate
	 * @param schema for validation
	 * @return a ValidationResult with validatin result
	 */
	public static ValidationResult validateSchema(Document doc, Schema schema) {
		try {
			Source xmlSource = new DOMSource(doc);
			schema.newValidator().validate(xmlSource);
		} catch (Exception e) {
			log.warn(e);
			return new ValidationResult(false, e.getMessage());
		}
		
		return new ValidationResult(true);
	}
	
}

