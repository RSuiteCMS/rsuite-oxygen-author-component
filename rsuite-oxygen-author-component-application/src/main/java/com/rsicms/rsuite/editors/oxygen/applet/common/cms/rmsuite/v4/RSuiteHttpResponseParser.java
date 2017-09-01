package com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite.v4;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import com.rsicms.rsuite.editors.oxygen.applet.common.OxygenIntegrationException;
import com.rsicms.rsuite.editors.oxygen.applet.parameters.OxygenOpenDocumentParmaters;
import com.rsicms.rsuite.editors.oxygen.applet.parameters.OxygenOpenDocumentParmatersNames;

public class RSuiteHttpResponseParser {

	private static XMLInputFactory xmlInputFactory = XMLInputFactory
			.newFactory();

	static String parseResponseSaveResponse(String response, String elementName)
			throws Exception {

		try (StringReader stringReader = new StringReader(response)) {

			XMLEventReader reader = xmlInputFactory
					.createXMLEventReader(stringReader);

			String rootElement = null;
			String currentElement = null;

			Map<String, String> elementValues = new HashMap<>();
			elementValues.put("message", null);
			elementValues.put(elementName, null);

			Set<String> elementNames = elementValues.keySet();

			String elementValue = "";

			boolean isError = true;

			while (reader.hasNext()) {
				XMLEvent event = reader.nextEvent();

				if (event.isStartElement()) {
					StartElement element = event.asStartElement();
					currentElement = element.getName().getLocalPart();

					if (rootElement == null
							&& !"error".equalsIgnoreCase(currentElement)) {
						isError = false;
					}

					if (rootElement == null) {
						rootElement = currentElement;
					}
				}

				if (event.isCharacters()
						&& elementNames.contains(currentElement)) {
					elementValue += event.asCharacters().getData();
				}

				if (event.isEndElement()) {
					EndElement endElement = event.asEndElement();
					String endElementName = endElement.getName().getLocalPart();
					if (elementNames.contains(endElementName)) {
						elementValues.put(endElementName, elementValue);
						break;
					}
				}
			}

			if (isError) {
				throw new Exception(elementValues.get("message"));
			}

			return elementValues.get(elementName);

		}
	}

	static OxygenOpenDocumentParmaters parseOpenDocumentResponse(
			String responseText) throws Exception {
		OxygenOpenDocumentParmaters openDocumentParameter = new OxygenOpenDocumentParmaters();
		String error = null;
		
		try (StringReader stringReader = new StringReader(responseText)) {

			XMLStreamReader streamReader = xmlInputFactory.createXMLStreamReader(stringReader);
			
			while (streamReader.hasNext()) {
				int evenType = streamReader.next();
				
				if (XMLStreamReader.START_ELEMENT == evenType && isErrorElement(streamReader)){
					error = streamReader.getElementText();
					break;
				}
				
				if (XMLStreamReader.START_ELEMENT == evenType && isNotOpenDocumentElement(streamReader)){
					String elementName = streamReader.getName().getLocalPart();
					String value = streamReader.getElementText();
					openDocumentParameter.addParameter(OxygenOpenDocumentParmatersNames.fromName(elementName), value);
				}
			}
		}
		
		if (error != null){
			throw new OxygenIntegrationException(error);
		}
		
		return openDocumentParameter;
	}

	private static boolean isErrorElement(XMLStreamReader streamReader) {
		return "error".equals(streamReader.getName().getLocalPart());
	}

	private static boolean isNotOpenDocumentElement(XMLStreamReader streamReader) {
		return !"openDocument".equals(streamReader.getName().getLocalPart());
	}
	
}
