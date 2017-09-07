package com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite.save;

import java.io.StringWriter;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.ProcessingInstruction;
import javax.xml.stream.events.XMLEvent;

import com.rsicms.rsuite.editors.oxygen.applet.common.OxygenIntegrationException;

public class ChangeTrackingUpdater implements RSuiteDocumentParserHandler {

	private StringWriter xmlWrirter = new StringWriter();
	
	private XMLEventWriter xmlEventWrirter;
	
	private static XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newInstance();
	
	private static XMLEventFactory xmlEventFactory = XMLEventFactory.newInstance();
	
	public ChangeTrackingUpdater() throws OxygenIntegrationException{
		try {
			xmlEventWrirter = xmlOutputFactory.createXMLEventWriter(xmlWrirter);
		} catch (XMLStreamException e) {
			throw new OxygenIntegrationException("Unable to create writer", e);
		}
	}
	
	@Override
	public void handleXmlElement(XMLEvent xmlEvent) throws XMLStreamException {
		if (isDeleteProcessingInstruction(xmlEvent)){
			ProcessingInstruction pi = (ProcessingInstruction)xmlEvent;			
			String updatedPI = removeRSuiteId(pi.getData());			
			xmlEvent = xmlEventFactory.createProcessingInstruction(pi.getTarget(), updatedPI);
		}
		xmlEventWrirter.add(xmlEvent);
	}

	private boolean isDeleteProcessingInstruction(XMLEvent xmlEvent) {
		if (!xmlEvent.isProcessingInstruction()){
			return false;
		}
		
		ProcessingInstruction pi = (ProcessingInstruction)xmlEvent;
		if("oxy_delete".equalsIgnoreCase(pi.getTarget())){
			return true;
		}
		
		return false;
	}
	
	private String removeRSuiteId(String data) {
		return data.replaceAll("r:rsuiteId=&quot;[0-9]+&quot;", "");
	}

	public String getUpdatedDocument() {
		return xmlWrirter.toString();
	}

}
