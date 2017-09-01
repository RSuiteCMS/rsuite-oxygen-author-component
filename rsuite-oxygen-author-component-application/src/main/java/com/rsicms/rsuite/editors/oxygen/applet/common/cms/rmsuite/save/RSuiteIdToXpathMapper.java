package com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite.save;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import com.rsicms.rsuite.editors.oxygen.applet.common.OxygenIntegrationException;

public class RSuiteIdToXpathMapper {

	private static XMLInputFactory xmlInputFactory = XMLInputFactory
			.newInstance();

	public static Map<String, String> getRSuiteIDsMapFromDocument(
			InputStream documentInputStream) throws OxygenIntegrationException {

		try (InputStream document = documentInputStream) {
			return parseDocument(document);
		} catch (XMLStreamException | IOException e) {
			throw new OxygenIntegrationException(e);
		}

	}

	private static Map<String, String> parseDocument(InputStream document)
			throws XMLStreamException, IOException {

		AtomicInteger elementLevel = new AtomicInteger(0);
		List<AtomicInteger> xpathLevelList = new ArrayList<>();
		Map<String, String> rsuiteIDsToXpathMap = new HashMap<>();

		XMLEventReader xmlEventReader = xmlInputFactory
				.createXMLEventReader(document);

		while (xmlEventReader.hasNext()) {
			XMLEvent xmlEvent = xmlEventReader.nextEvent();

			handleStartElement(xmlEvent, rsuiteIDsToXpathMap, xpathLevelList,
					elementLevel);

			handleCloseElement(xmlEvent, xpathLevelList, elementLevel);

		}

		return rsuiteIDsToXpathMap;
	}

	private static void handleStartElement(XMLEvent xmlEvent,
			Map<String, String> rsuiteIDsToXpathMap,
			List<AtomicInteger> xpathLevelList, AtomicInteger elementLevel) {
		if (xmlEvent.isStartElement()) {

			elementLevel.incrementAndGet();

			setUpElementPosition(xpathLevelList, elementLevel);

			Attribute rsuiteId = getRSuiteIdAttribute(xmlEvent);

			if (rsuiteId != null) {
				String xpath = createXpath(xpathLevelList);
				rsuiteIDsToXpathMap.put(rsuiteId.getValue(), xpath);
			}

		}
	}

	private static Attribute getRSuiteIdAttribute(XMLEvent xmlEvent) {
		StartElement startElement = xmlEvent.asStartElement();
		Attribute rsuiteId = startElement.getAttributeByName(new QName(
				"http://www.rsuitecms.com/rsuite/ns/metadata", "rsuiteId"));
		return rsuiteId;
	}

	private static void setUpElementPosition(
			List<AtomicInteger> xpathLevelList, AtomicInteger elementLevel) {
		if (xpathLevelList.size() < elementLevel.get()) {
			AtomicInteger elementPostionAtSpecificLevel = new AtomicInteger(0);
			xpathLevelList.add(elementLevel.get() - 1,
					elementPostionAtSpecificLevel);
		}

		AtomicInteger elementPostionAtSpecificLevel = xpathLevelList
				.get(elementLevel.get() - 1);
		elementPostionAtSpecificLevel.incrementAndGet();
	}

	private static void handleCloseElement(XMLEvent xmlEvent,
			List<AtomicInteger> xpathLevelList, AtomicInteger elementLevel) {
		if (xmlEvent.isEndElement()) {
			if (elementLevel.get() < xpathLevelList.size()) {
				for (int i = elementLevel.get(); i < xpathLevelList.size(); i++) {
					xpathLevelList.get(i).set(0);
				}
			}

			elementLevel.decrementAndGet();
		}
	}

	private static String createXpath(List<AtomicInteger> xpathLevelList) {
		StringBuilder xpath = new StringBuilder();
		for (AtomicInteger position : xpathLevelList) {
			if (position.get() == 0){
				break;
			}
			xpath.append("/*[").append(position).append("]");
		}

		return xpath.toString();

	}
}
