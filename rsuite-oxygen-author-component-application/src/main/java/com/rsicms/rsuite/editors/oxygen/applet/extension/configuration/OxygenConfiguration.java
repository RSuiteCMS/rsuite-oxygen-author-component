package com.rsicms.rsuite.editors.oxygen.applet.extension.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import com.rsicms.rsuite.editors.oxygen.applet.common.OxygenIntegrationException;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.ICmsURI;
import com.rsicms.rsuite.editors.oxygen.applet.common.utils.OxygenIOUtils;

public class OxygenConfiguration {

	private ICmsURI cmsUri;

	private Map<String, Object> oxygenConfigurationPropertyMap = new HashMap<String, Object>();

	private Map<String, List<String>> oxygenConfigurationMap = new HashMap<String, List<String>>();

	private LicenseServerConfiguration licenseServerConfiguration = new LicenseServerConfiguration();
	
	private MathFlowConfiguration mathFlowConfiguration;
	
	public OxygenConfiguration(ICmsURI cmsUri)
			throws OxygenIntegrationException {

		try {
			this.cmsUri = cmsUri;
			loadConfiguration();
		} catch (IOException e) {
			new OxygenIntegrationException("Unable to parse the configuration",
					e);
		} catch (XMLStreamException e) {
			new OxygenIntegrationException("Unable to parse the configuration",
					e);
		}
	}

	private void loadConfiguration() throws IOException, XMLStreamException {
		InputStream inputStream = OxygenIOUtils.loadContentFromURL(cmsUri
				.getConfigurationURI());

		XMLInputFactory inputFactory = XMLInputFactory.newInstance();
		XMLEventReader reader = inputFactory.createXMLEventReader(inputStream);

		String configurationName = null;
		List<String> configurationValues = null;
		StringBuilder valueText = null;

		while (reader.hasNext()) {
			XMLEvent event = (XMLEvent) reader.next();

			if (event.isStartElement()) {
				StartElement element = event.asStartElement();
				String elementName = element.getName().getLocalPart();

				if (elementName.equals("authorComponentProperty")) {
					String type = getAttributeValue(element, "type");
					String name = getAttributeValue(element, "name");
					String value = getAttributeValue(element, "value");

					oxygenConfigurationPropertyMap.put(name,
							new OxygenConfigurationProperty(name, value, type).getTypedValue());
				}

				if (elementName.equals("configuration")) {
					configurationName = getAttributeValue(element, "name");
					configurationValues = new ArrayList<String>();
				}

				if (elementName.equals("value")) {
					valueText = new StringBuilder();
				}
				
				parseLicenseServer(element, elementName);
				parseMathFlow(element, elementName);
			}

			if (event.isCharacters() && valueText != null) {
				valueText.append(event.asCharacters().getData());
			}

			if (event.isEndElement()) {
				EndElement endElement = event.asEndElement();
				String elementName = endElement.getName().getLocalPart();

				if (elementName.equals("configuration")) {
					oxygenConfigurationMap.put(configurationName,
							configurationValues);
				}

				if (elementName.equals("value")) {
					configurationValues.add(valueText.toString());
					valueText = null;
				}
			}
		}
	}

	private void parseMathFlow(StartElement element, String elementName) {
		if (elementName.equals("mathFlow")) {
			String editorLicenseKey = getAttributeValue(element, "editorLicenseKey");
			String composerLicenseKey = getAttributeValue(element, "composerLicenseKey");
			

			mathFlowConfiguration = new MathFlowConfiguration(editorLicenseKey, composerLicenseKey);
		}
	}
	
	private void parseLicenseServer(StartElement element, String elementName) {
		
		if (elementName.equals("licenseServer")) {
			String url = getAttributeValue(element, "url");
			String user = getAttributeValue(element, "user");
			String password = getAttributeValue(element, "password");
			licenseServerConfiguration = new LicenseServerConfiguration(url, user, password);
		}
	}

	private String getAttributeValue(StartElement element, String attributeName) {
		String attributeValue = null;
		Attribute attribute = element.getAttributeByName(new QName(
				attributeName));
		if (attribute != null) {
			attributeValue = attribute.getValue();
		}
		return attributeValue;
	}

	public Map<String, Object> getOxygenConfigurationPropertyMap() {
		return oxygenConfigurationPropertyMap;
	}

	public List<String> getOxygenConfigurationValueList(String configurationName) {
		return oxygenConfigurationMap.get(configurationName);
	}
	
	public String getOxygenConfigurationValue(String configurationName) {
		List<String> valueList = oxygenConfigurationMap.get(configurationName);
		if (valueList != null && valueList.size() > 0){
			return  valueList.get(0);
		}
		
		return null;
	}

	public LicenseServerConfiguration getLicenseServerConfiguration() {
		return licenseServerConfiguration;
	}

	public MathFlowConfiguration getMathFlowConfiguration() {
		return mathFlowConfiguration;
	}
	
}
