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
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public class Main {
	
	private static String filename;
	private static String schema;
	
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
