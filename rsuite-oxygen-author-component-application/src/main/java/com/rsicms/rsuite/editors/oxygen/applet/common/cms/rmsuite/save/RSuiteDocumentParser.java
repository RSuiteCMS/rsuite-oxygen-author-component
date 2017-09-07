package com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite.save;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import org.apache.commons.lang.StringUtils;

import com.rsicms.rsuite.editors.oxygen.applet.common.OxygenIntegrationException;

public class RSuiteDocumentParser {

	private RSuiteDocumentParserHandler[] handlerList;
	
	private String doctypeContent = null;
	
	private static XMLEventFactory xmlEventFactory = XMLEventFactory.newFactory();
	
	public RSuiteDocumentParser(RSuiteDocumentParserHandler... handlers){
		handlerList = handlers;
	}
	
	public RSuiteDocumentParser(String doctypeContent, RSuiteDocumentParserHandler... handlers){
		this(handlers);
		this.doctypeContent = doctypeContent;
	}
	
	private static XMLInputFactory xmlInputFactory = XMLInputFactory
			.newInstance();

	public void parseRSuiteDocument(
			InputStream documentInputStream) throws OxygenIntegrationException {

		try (InputStream document = documentInputStream) {
			parseDocument(document);
		} catch (XMLStreamException | IOException e) {
			throw new OxygenIntegrationException(e);
		}

	}

	private void parseDocument(InputStream document)
			throws XMLStreamException, IOException {		

		XMLEventReader xmlEventReader = xmlInputFactory
				.createXMLEventReader(document);

		while (xmlEventReader.hasNext()) {
			XMLEvent xmlEvent = xmlEventReader.nextEvent();

			if (isDTD(xmlEvent)){
				xmlEvent = fixDTDEvent(xmlEvent);
			}
			
			for (RSuiteDocumentParserHandler handler : handlerList){
				handler.handleXmlElement(xmlEvent);
			}
		}
	}

	private XMLEvent fixDTDEvent(XMLEvent xmlEvent) {
		if (StringUtils.isNotBlank(doctypeContent)){
			return xmlEventFactory.createDTD(doctypeContent);
		}
		
		return xmlEvent;
	}

	private boolean isDTD(XMLEvent xmlEvent) {
		return xmlEvent.getEventType() == XMLEvent.DTD;
	}
	
}
