package com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite.save;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public class RSuiteIdToXpathMapper implements RSuiteDocumentParserHandler {

	private Map<String, String> rsuiteIDsToXpathMap = new HashMap<>();

	private AtomicInteger elementLevel = new AtomicInteger(0);
	private List<AtomicInteger> xpathLevelList = new ArrayList<>();

	@Override
	public void handleXmlElement(XMLEvent xmlEvent) throws XMLStreamException {

		handleStartElement(xmlEvent);

		handleCloseElement(xmlEvent);

	}

	private void handleStartElement(XMLEvent xmlEvent) {

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

	private void handleCloseElement(XMLEvent xmlEvent) {
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
			if (position.get() == 0) {
				break;
			}
			xpath.append("/*[").append(position).append("]");
		}

		return xpath.toString();

	}

	public Map<String, String> getRsuiteIDsToXpathMap() {
		return rsuiteIDsToXpathMap;
	}

}
