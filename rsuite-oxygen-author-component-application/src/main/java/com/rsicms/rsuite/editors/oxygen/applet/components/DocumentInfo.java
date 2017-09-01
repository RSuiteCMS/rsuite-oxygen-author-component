package com.rsicms.rsuite.editors.oxygen.applet.components;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import com.rsicms.rsuite.editors.oxygen.applet.common.DocumentCustomizationFactory;
import com.rsicms.rsuite.editors.oxygen.applet.common.OxygenIntegrationException;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.ICustomizationFactory;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.IDocumentCustomization;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.IDocumentURI;
import com.rsicms.rsuite.editors.oxygen.applet.parameters.OxygenOpenDocumentParmaters;
import com.rsicms.rsuite.editors.oxygen.applet.parameters.OxygenOpenDocumentParmatersNames;

public class DocumentInfo {

	private String schemaId;

	private String publicSchemaId;
	
	private String systemSchemaId;

	private QName rootElement;

	private List<Attribute> attributes;

	private ICustomizationFactory customizationFactory;

	private IDocumentURI documentURI;

	private IDocumentCustomization customization;

	private String document;

	private OxygenMainComponent mainComponent;

	private OxygenOpenDocumentParmaters parameters;

	public DocumentInfo(OxygenMainComponent mainComponent,
			OxygenOpenDocumentParmaters parameters) throws OxygenIntegrationException {

		this.mainComponent = mainComponent;
		this.parameters = parameters;

		documentURI = mainComponent.getCustomizationFactory()
				.createDocumentURI(parameters);

		customizationFactory = mainComponent.getCustomizationFactory();

		customization = DocumentCustomizationFactory.createCustomization(
				customizationFactory, documentURI, parameters);

		document = customizationFactory.getCmsCustomization().getCmsActions().loadDocument(customization, documentURI);

		try{
		XMLInputFactory inputFactory = XMLInputFactory.newInstance();
		
		XMLEventReader reader = inputFactory
				.createXMLEventReader(new StringReader(document));

		while (reader.hasNext()) {

			XMLEvent event = (XMLEvent) reader.next();

			if (event.isStartElement()) {

				
				attributes = new ArrayList<Attribute>();
				StartElement startElement = event.asStartElement();
				Iterator<Attribute> attributesIterator = startElement
						.getAttributes();
				while (attributesIterator.hasNext()) {
					Attribute attribute = (Attribute) attributesIterator.next();
					attributes.add(attribute);
				}
				
				rootElement =  startElement.getName();
				
				schemaId = parameters.getParameterValue(OxygenOpenDocumentParmatersNames.SCHEMA_ID);
				publicSchemaId = parameters.getParameterValue(OxygenOpenDocumentParmatersNames.SCHEMA_PUBLIC_ID);

				break;
			}
		}

		reader.close();
		
		}catch(XMLStreamException e){
			throw new OxygenIntegrationException(e);
		}

	}

	public DocumentInfo(String schemaId, String systemSchemaId, String publicSchemaId,
			QName rootElement, List<Attribute> attributes) {
		super();
		this.schemaId = schemaId;
		this.publicSchemaId = publicSchemaId;
		this.rootElement = rootElement;
		this.attributes = attributes;
		this.systemSchemaId = systemSchemaId; 
	}

	public String getSchemaId() {
		return schemaId;
	}

	public String getPublicSchemaId() {
		return publicSchemaId;
	}
		

	public String getSystemSchemaId() {
		return systemSchemaId;
	}

	public QName getRootElement() {
		return rootElement;
	}

	public List<Attribute> getAttributes() {
		return attributes;
	}

	public ICustomizationFactory getCustomizationFactory() {
		return customizationFactory;
	}

	public IDocumentURI getDocumentURI() {
		return documentURI;
	}

	public IDocumentCustomization getCustomization() {
		return customization;
	}

	public String getDocument() {
		return document;
	}

	public OxygenMainComponent getMainComponent() {
		return mainComponent;
	}

	public OxygenOpenDocumentParmaters getParameters() {
		return parameters;
	}
}

