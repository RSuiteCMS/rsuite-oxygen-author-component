package com.rsicms.rsuite.editors.oxygen.integration.utils.resolver;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.reallysi.rsuite.api.ConfigurationProperties;
import com.reallysi.rsuite.api.SchemaInfo;
import com.reallysi.rsuite.api.SchemaInfoCollection;
import com.reallysi.rsuite.api.extensions.ExecutionContext;

public class RSuiteOxygenEntityResolver implements EntityResolver {
	
	private Log log = LogFactory.getLog(getClass());
	
	private SchemaInfoCollection schemaInfoCollection;
	private String host;
	private String port;

	/**
	 * 
	 * @param schemaInfoCollection
	 */
	public RSuiteOxygenEntityResolver(ExecutionContext context) {
		
		schemaInfoCollection = context.getSchemaService();
		ConfigurationProperties confProperties =	context.getConfigurationProperties();
		
		this.host = confProperties.getProperty("rsuite.server.host", "localhost");
		this.port = confProperties.getProperty("rsuite.server.port","8080");
	}

	public InputSource resolveEntity(String publicId, String systemId)
			throws SAXException, IOException {

		if (systemId != null) {
			if (systemId.indexOf("/rsuite/schemas") > -1)
				return null;
		}

		URL url = replaceLocation(publicId, systemId);
		if (url == null) {
			// return null, use default resolveEntity
			return null;
		}

		return new InputSource(url.openStream());
	}

	public URL replaceLocation(String publicId, String systemId) {
		if (systemId != null && systemId.startsWith("file://")) {
			systemId = systemId.substring("file://".length());
			systemId = systemId.replace("%20", " ");
		}

		// KLUDGE ALERT
		// If the original document had a relative path for the the system ID,
		// for example: <!DOCTYPE PUBLIC "" "book.dtd">
		// then the systemId passed to this method will have an absolute path
		// that
		// is usually based on the current working directory of the JVM
		//
		// We are going to walk through our list of schemas and find the
		// first match based on the end of the systemId
		//

		SchemaInfo info = schemaInfoCollection.findMatchingSchemaInfo("DTD",
				publicId, systemId);
		if (info != null)
			return getSchemaResourceUrl(info);

		return null;
	}

	protected URL getSchemaResourceUrl(SchemaInfo schemaInfo) {
		if (schemaInfo == null)
			return null;


		URL url = null;
		try {

//TODO remove http
			url = new URL("http://" + host + ":" + port + "/rsuite/schemas/"
					+ schemaInfo.getSchemaId() + "."
					+ fileExtension(schemaInfo.getFileName()));
		} catch (MalformedURLException e) {
			log.warn("unable to calculate schema resource url", e);
		}
		return url;
	}

	protected static String fileExtension(String fileName) {
		int pos = fileName.lastIndexOf(".");
		if (pos > -1)
			return fileName.substring(pos + 1);
		else
			return fileName;
	}

}
