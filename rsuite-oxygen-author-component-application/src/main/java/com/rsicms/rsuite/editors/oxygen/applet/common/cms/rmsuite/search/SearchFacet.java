package com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite.search;

import java.io.StringWriter;
import java.util.List;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public class SearchFacet {

	private String text;
	
	private List<String> elements;
	
	private String type;

	private XMLOutputFactory xmlof = XMLOutputFactory.newInstance();

	public SearchFacet(String text, List<String> elements, String type) {
		super();
		this.text = text;
		this.elements = elements;
		this.type = type;
	}

	public String getText() {
		return text;
	}

	public List<String> getElements() {
		return elements;
	}

	public String getType() {
		return type;
	}

	public String generateFacetedQuery()
			throws Exception {

		StringWriter sw = new StringWriter();
		// Create an XML stream writer
		XMLStreamWriter xmlw = xmlof.createXMLStreamWriter(sw);

		xmlw.writeStartElement("faceted-query");

		xmlw.writeStartElement("facets");

		xmlw.writeStartElement("facet");

		writeElementWithValue(xmlw, "name", getType());

		xmlw.writeStartElement("values");

		for (String elementName : getElements()) {
			writeElementWithValue(xmlw, "value", elementName);
		}

		xmlw.writeEndElement();

		xmlw.writeEndElement();

		xmlw.writeEndElement();

		writeElementWithValue(xmlw, "query-text", getText());

		xmlw.writeEndElement();
		xmlw.writeEndDocument();

		xmlw.flush();
		sw.flush();

		return sw.toString();

	}
	
	private static void writeElementWithValue(XMLStreamWriter xmlw,
			String elementName, String value) throws XMLStreamException {
		xmlw.writeStartElement(elementName);
		xmlw.writeCharacters(value);
		xmlw.writeEndElement();
	}
	
	public boolean isRSuiteIdSearch(){
		if (text != null && text.trim().matches("^id:[0-9]+")){
			return true;
		}
		
		return false;
	}
	
	public String getRSuiteIdToSearch(){
		if (text == null){
			return null;
		}
		
		return text.trim().substring(3);
	}
}
