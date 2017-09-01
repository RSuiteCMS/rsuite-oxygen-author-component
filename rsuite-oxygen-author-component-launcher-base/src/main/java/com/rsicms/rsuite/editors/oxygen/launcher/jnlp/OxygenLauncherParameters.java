package com.rsicms.rsuite.editors.oxygen.launcher.jnlp;

import java.util.Map;

import com.rsicms.rsuite.editors.oxygen.launcher.common.OxygenApplicationException;

public class OxygenLauncherParameters {

	private String sessionKey;
	
	private String hostAddress;
	
	private String launcherVersion;

	private String baseURI;
	
	
	public OxygenLauncherParameters(String parameterList) throws OxygenApplicationException {
		
		Map<String, String> parsedParameters = OxygenParamererListParser.parseParameters(parameterList);
		sessionKey = parsedParameters.get("sessionKey");
		hostAddress = parsedParameters.get("hostAddress");
		launcherVersion = parsedParameters.get("launcherVersion");
		baseURI = parsedParameters.get("baseURI");
	}

	public String getSessionKey() {
		return sessionKey;
	}

	public String getHostAddress() {
		return hostAddress;
	}

	public String getLauncherVersion() {
		return launcherVersion;
	}

	public String getBaseURI() {
		return baseURI;
	}
	
}
