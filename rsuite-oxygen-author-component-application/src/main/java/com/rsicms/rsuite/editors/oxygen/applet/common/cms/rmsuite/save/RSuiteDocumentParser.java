package com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite.save;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import com.rsicms.rsuite.editors.oxygen.applet.common.OxygenIntegrationException;

public class RSuiteDocumentParser {

	private RSuiteDocumentParserHandler[] handlerList;
	
	public RSuiteDocumentParser(RSuiteDocumentParserHandler... handlers){
		handlerList = handlers;
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

			for (RSuiteDocumentParserHandler handler : handlerList){
				handler.handleXmlElement(xmlEvent);
			}
		}
	}
	
}
