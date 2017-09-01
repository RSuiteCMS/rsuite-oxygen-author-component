package com.rsicms.rsuite.editors.oxygen.integration.search.result;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.apache.commons.io.IOUtils;

public class MoResultParser {

	private static SimpleDateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

	public static List<MoResult> parseResults(String[] results)
			throws XMLStreamException, ParseException, IOException {

		List<MoResult> resultList = new ArrayList<MoResult>();
		XMLInputFactory inputFactory = XMLInputFactory.newInstance();

		for (String xml : results) {
			XMLEventReader eventReader = inputFactory.createXMLEventReader(
					IOUtils.toInputStream(xml, "utf-8"), "utf-8");
			resultList.add(parseMoResultObject(eventReader));
		}

		return resultList;
	}

	private static  MoResult parseMoResultObject(XMLEventReader eventReader)
			throws XMLStreamException, ParseException {

		MoResult moResult = new MoResult();

		String elementName = "";
		String namespace = "";
		Map<String, String> aliases = new HashMap<String, String>();

		while (eventReader.hasNext()) {

			XMLEvent event = eventReader.nextEvent();

			if (event.getEventType() == XMLStreamConstants.START_ELEMENT) {
				StartElement startElement = event.asStartElement();
				String localName = startElement.getName().getLocalPart();

				if ("item".equalsIgnoreCase(localName)) {
					Attribute ancestorsAtt = startElement
							.getAttributeByName(new QName("ancestorIds"));

					if (ancestorsAtt != null) {
						String ancestorsIds = ancestorsAtt.getValue();
						String[] ids = ancestorsIds.split("\\s+");
						if (ids.length > 0) {
							moResult.setParentId(ids[0]);
						}

					}
				}

				if ("id".equalsIgnoreCase(localName)) {
					String id = eventReader.getElementText();
					moResult.setId(id);
				} else if ("dtcr".equalsIgnoreCase(localName)) {
					String createdDate = eventReader.getElementText();
					moResult.setDateCreated(parseDate(createdDate));
				} else if ("dtmd".equalsIgnoreCase(localName)) {
					String modifiedDate = eventReader.getElementText();
					moResult.setDateModified(parseDate(modifiedDate));
				} else if ("ln".equalsIgnoreCase(localName)) {
					elementName = eventReader.getElementText();
					moResult.setLocalName(elementName);
				} else if ("dn".equalsIgnoreCase(localName)) {
					String dn = eventReader.getElementText();
					moResult.setDisplayName(dn);
				} else if ("ns".equalsIgnoreCase(localName)) {
					namespace = eventReader.getElementText();

				} else if ("alias".equalsIgnoreCase(localName)) {
					Attribute typeAtt = startElement
							.getAttributeByName(new QName(
									"http://www.rsuitecms.com/rsuite/ns/metadata",
									"type"));
					String type = "";
					if (typeAtt != null) {
						type = typeAtt.getValue();
					}

					String alias = eventReader.getElementText();

					aliases.put(alias, type);
				}
			}

			if (event.getEventType() == XMLStreamConstants.END_ELEMENT) {
				EndElement endElement = event.asEndElement();
				String localName = endElement.getName().getLocalPart();
				if ("v".equals(localName)) {

					if ("http://www.rsuitecms.com/rsuite/ns/metadata"
							.equals(namespace) && "nonxml".equals(elementName)) {
						moResult.setXml(false);
					}

					moResult.setAliases(aliases);

					break;
				}
			}
		}

		return moResult;
	}

	private static String parseDate(String dateValue) throws ParseException {
		Date date = dateFormat.parse(dateValue);
		return String.valueOf(date.getTime() / 1000);
	}
}
