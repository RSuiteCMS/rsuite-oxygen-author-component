package com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite;

import static com.rsicms.rsuite.editors.oxygen.applet.parameters.OxygenOpenDocumentParmatersNames.BASE_URI;
import static com.rsicms.rsuite.editors.oxygen.applet.parameters.OxygenOpenDocumentParmatersNames.DOCUMENT_URI;
import static com.rsicms.rsuite.editors.oxygen.applet.parameters.OxygenOpenDocumentParmatersNames.SCHEMA_ID;
import static com.rsicms.rsuite.editors.oxygen.applet.parameters.OxygenOpenDocumentParmatersNames.SCHEMA_PUBLIC_ID;
import static com.rsicms.rsuite.editors.oxygen.applet.parameters.OxygenOpenDocumentParmatersNames.SCHEMA_SYSTEM_ID;
import static com.rsicms.rsuite.editors.oxygen.applet.parameters.OxygenOpenDocumentParmatersNames.TITLE;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.rsicms.rsuite.editors.oxygen.applet.common.api.ICmsURI;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.IURLMapper;
import com.rsicms.rsuite.editors.oxygen.applet.common.utils.OxygenIOUtils;
import com.rsicms.rsuite.editors.oxygen.applet.common.utils.OxygenUtils;
import com.rsicms.rsuite.editors.oxygen.applet.parameters.OxygenOpenDocumentParmaters;

public class RsuiteDefaultURLMapper implements IURLMapper {

	private static Logger logger = Logger.getLogger(RsuiteDefaultURLMapper.class);

	private static Set<String> protocols = new HashSet<String>();
	
	static{
		protocols.add("http");
		protocols.add("https");
	}
	
	@Override
	public OxygenOpenDocumentParmaters mapURLToOpenParameters(URL urlToMap,
			ICmsURI cmsURI) {
		
		String link = urlToMap.toString();

		if (link.indexOf('#') > 0){
			link =  link.substring(0, link.indexOf('#'));
		}
		
		if (urlToMap.getProtocol().equalsIgnoreCase("file")) {
			link = link.replace("file:" + new File("").getAbsolutePath() +"/", "");
		}else if (protocols.contains(urlToMap.getProtocol().toLowerCase())){
			link = extractNameFromLink(link);
		}

		String infoUrl = cmsURI.getHostURI()
				+ "/rsuite/rest/v1/api/rsuite.oxygen.mo.info" + "?";

		infoUrl += cmsURI.getSessionKeyParam();

		if (link.startsWith("rsuite:/res/alias/")) {
			link = link.replace("rsuite:/res/alias/", "");
		} else if (link.startsWith("rsuite:/res/id/")) {
			link = link.replace("rsuite:/res/id/", "");
		} else if (link.startsWith("/rsuite/rest/v1/content/alias/")){
			link = link.replace("/rsuite/rest/v1/content/alias/", "");
		}else{
			link = mapLinkValueToRsuiteAliasOrId(link);
		}

		if (StringUtils.isNumeric(link)) {
			infoUrl += "&rsuiteId";
		} else {
			infoUrl += "&alias";
		}

		infoUrl += "=" + link;

		return obtainOpenDocumentParameters(infoUrl, cmsURI);
	}

	private String extractNameFromLink(String link) {
		try {
			URI uri = new URI(link);
			link = FilenameUtils.getName(uri.getPath());
		} catch (URISyntaxException e) {
			OxygenUtils.handleExceptionUI(logger, e);
		}
		return link;
	}

	public static OxygenOpenDocumentParmaters obtainOpenDocumentParameters(String url, ICmsURI cmsUri) {

		OxygenOpenDocumentParmaters parameters = new OxygenOpenDocumentParmaters();

		try {
			InputStream inputStream = OxygenIOUtils.loadContentFromURL(url);
			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			XMLEventReader reader = inputFactory
					.createXMLEventReader(inputStream);

			while (reader.hasNext()) {
				XMLEvent event = (XMLEvent) reader.next();

				if (event.isStartElement()) {
					StartElement element = event.asStartElement();

					Attribute documentUri = element
							.getAttributeByName(new QName(DOCUMENT_URI.getName()));
					parameters.addParameter(
							DOCUMENT_URI,
							documentUri.getValue());

					Attribute schemaId = element.getAttributeByName(new QName(
							SCHEMA_ID.getName()));
					parameters.addParameter(
							SCHEMA_ID,
							schemaId.getValue());

					Attribute publicId = element.getAttributeByName(new QName(
							SCHEMA_PUBLIC_ID.getName()));
					parameters.addParameter(
							SCHEMA_PUBLIC_ID,
							publicId.getValue());

					Attribute systemID = element.getAttributeByName(new QName(
							SCHEMA_SYSTEM_ID.getName()));
					parameters.addParameter(
							SCHEMA_SYSTEM_ID,
							systemID.getValue());
					
					Attribute title = element.getAttributeByName(new QName(
							TITLE.getName()));
					parameters.addParameter(
							TITLE,
							title.getValue());

					parameters.addParameter(
							BASE_URI,
							cmsUri.getBaseURI());
				}

			}
		} catch (Exception e) {
			OxygenUtils.handleException(logger, e);
		}

		return parameters;
	}

	
	protected String mapLinkValueToRsuiteAliasOrId(String link){
		return link;
	}
}
