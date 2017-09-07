package com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite.save;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

public interface RSuiteDocumentParserHandler {

	void handleXmlElement(XMLEvent xmlEvent) throws XMLStreamException;
	
}
