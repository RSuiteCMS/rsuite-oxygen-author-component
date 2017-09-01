package com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite.tree.connector;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Logger;

import com.rsicms.rsuite.editors.oxygen.applet.common.utils.OxygenUtils;

public class RSuiteConnectorUtils {

	private static final String DAV_MARKER = "/dav/";
	
	private static final String DAV_SES_MARKER = "dav/ses=";
	
	private static final String DAV_RESOURCE_MARKER = "action=edit/";
	
	private static Logger logger = Logger.getLogger(RSuiteConnectorUtils.class);
	
	public static String getXml(String documentUri) {

		try {
			URL url = new URL(documentUri);
			HttpURLConnection httpCon = (HttpURLConnection) url
					.openConnection();
			httpCon.setDoOutput(true);
			httpCon.setRequestMethod("GET");

			InputStream is = httpCon.getInputStream();

			// is = c.openInputStream();
			int ch;
			StringBuilder sb = new StringBuilder();
			while ((ch = is.read()) != -1) {
				sb.append((char) ch);
			}
			is.close();
			httpCon.disconnect();

			return sb.toString();
		} catch (Exception e1) {
			OxygenUtils.handleException(logger, e1);
		}

		return null;
	}

	

	public static String getSessionIdFromDocumentUri(String documentUri) {

		String sessionId = null;
		int index = documentUri.indexOf(DAV_SES_MARKER);
		if (index > -1) {
			documentUri = documentUri.substring(index + DAV_SES_MARKER.length());
			index = documentUri.indexOf("/");
			if (index > -1) {
				sessionId = documentUri.substring(0, index);
			}

		}
		return sessionId;
	}

	public static String getBaseUriFromDocumentUri(String documentUri) {

		String baseUri = null;

		int index = documentUri.indexOf(DAV_MARKER);
		if (index > -1) {
			baseUri = documentUri.substring(0, index);

		}
		return baseUri;
	}
	
	public static String getEditedTopicIdFromDocumentUri(String documentUri){
		String sessionId = null;
		int index = documentUri.indexOf(DAV_RESOURCE_MARKER);
		if (index > -1) {
			documentUri = documentUri.substring(index + DAV_RESOURCE_MARKER.length());
			index = documentUri.indexOf("$");
			if (index > -1) {
				sessionId = documentUri.substring(0, index);
			}

		}
		return sessionId;
	}
}
