package com.rsicms.rsuite.editors.oxygen.integration.utils;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.service.XmlApiManager;

/**
 * 
 * @deprecated Use class from extension helper when available
 *
 */
@Deprecated
public class TransformationUtils {

	public static String transformDocument(ExecutionContext context, ManagedObject mo,
			String xslURI) throws RSuiteException {
		return transformDocument(context, mo, xslURI, null);
	}
	
	public static String transformDocument(ExecutionContext context, ManagedObject mo,
			String xslURI, Map<String, String> parameters) throws RSuiteException {
	
		StringWriter sw = new StringWriter();
		transformDocument(context, mo, xslURI,sw, parameters);
		try {
			sw.close();
		} catch (IOException e) {
			//do nothing
		}
		return sw.toString();
	}
	
	
	
	public static void transformDocument(ExecutionContext context, ManagedObject mo,
			String xslURI, Writer writer, Map<String, String> paramters) throws RSuiteException {
		try {

			XmlApiManager xmlManager = context.getXmlApiManager();

			Transformer transformer = xmlManager
					.getTransformer(new URI(xslURI));

			XMLReader xmlReader = XMLReaderFactory.createXMLReader();

			xmlReader.setEntityResolver(xmlManager
					.getRSuiteAwareEntityResolver());

			InputSource inputSource = new InputSource(mo.getInputStream());		

			SAXSource inputDocument = new SAXSource(xmlReader, inputSource);

			StreamResult streamResult = new StreamResult(writer);
			
			if (paramters != null){
				for (Entry<String, String> entry : paramters.entrySet()){
					transformer.setParameter(entry.getKey(), entry.getValue());
				}
			}
			
			transformer.transform(inputDocument, streamResult);

		} catch (TransformerConfigurationException e) {
			handleException(e);
		} catch (SAXException e) {
			handleException(e);
		} catch (TransformerException e) {
			handleException(e);
		} catch (URISyntaxException e) {
			handleException(e);
		}

	}

	private static void handleException(Exception e) throws RSuiteException {
		throw new RSuiteException(RSuiteException.ERROR_NOT_DEFINED,
				e.getLocalizedMessage(), e);
	}
}
