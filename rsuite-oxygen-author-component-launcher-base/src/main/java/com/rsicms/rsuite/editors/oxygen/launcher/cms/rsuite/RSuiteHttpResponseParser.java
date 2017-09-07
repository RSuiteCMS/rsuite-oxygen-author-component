package com.rsicms.rsuite.editors.oxygen.launcher.cms.rsuite;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

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
										
					if ("faultType".equalsIgnoreCase(currentElement)){
							isError = true;
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
	
}
