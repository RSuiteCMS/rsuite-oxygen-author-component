package com.rsicms.rsuite.editors.oxygen.applet.common.utils;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.rsicms.rsuite.editors.oxygen.applet.common.OxygenIntegrationException;

public class OxygenXMLUtil {

	/**
	 * Serialize the Element.
	 * 
	 * @param elm
	 *            The Element.
	 * @throws IOException
	 *             If any errors are encountered.
	 * @return The serialized element contents as a String.
	 */
	public static String serializeElementToString(Element elm)
			throws OxygenIntegrationException {

		return serializeElementToString(elm, false, false);
	}

	/**
	 * Serialize the Element.
	 * 
	 * @param elm
	 *            The Element.
	 * @throws IOException
	 *             If any errors are encountered.
	 * @return The serialized element contents as a String.
	 */
	public static String serializeElementToString(Element elm, boolean format,
			boolean xmlDeclaration) throws OxygenIntegrationException {

		Writer writer = new StringWriter();

		serializeElement(elm, writer, format, xmlDeclaration);

		return writer.toString().trim();
	}

	private static void serializeElement(Node node, Writer writer,
			boolean format, boolean xmlDeclaration) throws OxygenIntegrationException {

		try {
			Element elementToSerialze = null;

			if (node instanceof Document) {
				Document doc = (Document) node;
				elementToSerialze = doc.getDocumentElement();

			} else if (node instanceof Element) {
				elementToSerialze = (Element) node;
			}

			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");

			if (!xmlDeclaration) {
				transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,
						"yes");
			}

			if (format) {
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			}

			transformer.transform(new DOMSource(elementToSerialze),
					new StreamResult(writer));

			writer.close();

		} catch (ClassCastException e) {
			throw new OxygenIntegrationException(e);
		} catch (IOException e) {
			throw new OxygenIntegrationException(e);
		} catch (TransformerException e) {
			throw new OxygenIntegrationException(e);
		}
	}
}
