package com.rsicms.rsuite.editors.oxygen.integration.utils;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.lang.StringUtils;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.remoteapi.CallArgumentList;

public class WebServiceUtils {

	public static String getHostFromWsArguments(CallArgumentList args)
			throws RSuiteException {
		String requestIdentifier = args.getFirstValue("_request:identifier");
		return getHostFromRequestIdentifier(requestIdentifier);
	}
	
	private static String getHostFromRequestIdentifier(String requestIdentifier)
			throws RSuiteException {
		try {
			URL urlObj = new URL(requestIdentifier);

			String path = urlObj.getPath();
			if (!StringUtils.isBlank(path)
					&& requestIdentifier.indexOf(path) > -1) {
				return requestIdentifier.substring(0,
						requestIdentifier.indexOf(path));
			}
		} catch (MalformedURLException e) {
			throw new RSuiteException("Unable to retrieve host address from "
					+ requestIdentifier);
		}
		return requestIdentifier;
	}

	public static String createNormalizedHostName(String host) {
		String normalizedHostName = host.replaceAll("https?://", "");
	    normalizedHostName = normalizedHostName.replaceAll(":[0-9]+", "");
	    normalizedHostName = normalizedHostName.replaceAll("[^a-zA-Z0-9]+", "_");
	    
		if (normalizedHostName.endsWith("_")){
			normalizedHostName = normalizedHostName.substring(0, normalizedHostName.length() -1);
		}
		return normalizedHostName;
	}
}
