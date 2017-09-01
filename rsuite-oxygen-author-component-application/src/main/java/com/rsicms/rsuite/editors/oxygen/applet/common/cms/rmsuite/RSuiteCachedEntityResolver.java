package com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.rsicms.rsuite.editors.oxygen.applet.common.api.ICmsURI;
import com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite.helpers.SchemaCache;
import com.rsicms.rsuite.editors.oxygen.applet.common.utils.OxygenUtils;
import com.rsicms.rsuite.editors.oxygen.applet.common.utils.UriUtils;

public class RSuiteCachedEntityResolver implements EntityResolver {

	private static final String RSUITE_SCHEMAS = "/rsuite/schemas/";

	private ICmsURI cmsUri;

	private Map<String, String> publicSchemaMapping = new HashMap<String, String>();
	
	private Map<String, String> systemSchemaMapping = new HashMap<String, String>();

	private Logger logger = Logger.getLogger(this.getClass());

	private SchemaCache schemaCache;

	public RSuiteCachedEntityResolver(ICmsURI cmsUri, SchemaCache schemaCache) {
		this.cmsUri = cmsUri;
		this.schemaCache = schemaCache;
	}

	@Override
	public InputSource resolveEntity(String publicId, String systemId)
			throws SAXException, IOException {

		String rsuiteSchemaUid = null;

		String baseSystemId = FilenameUtils.getName(systemId);
		
		if (publicSchemaMapping.containsKey(publicId)) {
			systemId = cmsUri.getHostURI() + RSUITE_SCHEMAS
					+ publicSchemaMapping.get(publicId);

		} else if (systemId.contains("rsuite/schemas")) {

			URL url = new URL(systemId);
			systemId = cmsUri.getHostURI() + "/" + url.getPath();
		} else if (systemSchemaMapping.containsKey(baseSystemId)){
			systemId = cmsUri.getHostURI() + RSUITE_SCHEMAS
					+ systemSchemaMapping.get(baseSystemId);
		}

		String rsuiteSchemaUriPart = RSUITE_SCHEMAS;

		int index = systemId.indexOf(rsuiteSchemaUriPart);
		if (index > -1) {
			rsuiteSchemaUid = systemId.substring(index
					+ rsuiteSchemaUriPart.length());			

			byte[] bytes = schemaCache.getCachedSchema(rsuiteSchemaUid);
			if (bytes != null) {
				InputSource is = new InputSource(new ByteArrayInputStream(bytes));
				is.setSystemId(systemId);
				return is;
			}
		}

		return downloadSchemaFromRSuite(systemId, rsuiteSchemaUid);
	}
	
	private InputSource downloadSchemaFromRSuite(String systemId,
			String rsuiteSchemaUid) {

		try {

			systemId = UriUtils.addParameterToUri(systemId,
					cmsUri.getSessionKeyParam());
			
			URL url = new URL(systemId);
			
			URLConnection urlConnection = url
					.openConnection();
			
			if (urlConnection instanceof HttpURLConnection){
				HttpURLConnection httpCon = (HttpURLConnection)urlConnection;
				httpCon.setDoOutput(true);
				httpCon.setRequestMethod("GET");
				httpCon.setConnectTimeout(2000);
			}
			
			
			InputStream is = urlConnection.getInputStream();

			if (rsuiteSchemaUid != null) {
				byte[] schemaContent = IOUtils.toByteArray(is);
				schemaCache.cacheSchema(rsuiteSchemaUid, schemaContent);
				is = new ByteArrayInputStream(schemaContent);
			}
			
			InputSource source = new InputSource(is);
			source.setSystemId(systemId);
			return source;

		} catch (Exception e) {
			OxygenUtils.handleException(logger, e);
		}

		return null;
	}

	public void addPublicSchemaIdMapping(String publicSchemadId, String schemaId) {
		if (StringUtils.isNotEmpty(publicSchemadId)){
			publicSchemaMapping.put(publicSchemadId, schemaId);
		}		
	}
	
	public void addSystemSchemaIdMapping(String systemSchemadId, String schemaId) {
		if (StringUtils.isNotEmpty(systemSchemadId)){
			systemSchemaMapping.put(systemSchemadId, schemaId);
		}		
	}
}
