package org.chen.codegen.util;

import java.io.File;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.xml.sax.SAXException;

public class XmlUtil {
	
	/**
	 * @param xsdPath
	 * @param xmlPath
	 */
	public static String validXmlBySchema(String xsdPath, String xmlPath) {
		SchemaFactory schemaFactory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
		File localFile = new File(xsdPath);
		Schema localSchema = null;
		try {
			localSchema = schemaFactory.newSchema(localFile);
		} catch (SAXException localSAXException) {
			localSAXException.printStackTrace();
		}
		Validator localValidator = localSchema.newValidator();
		StreamSource localStreamSource = new StreamSource(FileHelper.getInputStream(xmlPath));
		try {
			localValidator.validate(localStreamSource);
		} catch (Exception localException) {
			return localException.getMessage();
		}
		return "";
	}
	
}
