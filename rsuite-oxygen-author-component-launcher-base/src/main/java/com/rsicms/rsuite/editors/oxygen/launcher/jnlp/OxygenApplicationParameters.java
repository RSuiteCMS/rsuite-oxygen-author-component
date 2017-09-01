package com.rsicms.rsuite.editors.oxygen.launcher.jnlp;

import java.util.Map;

import com.rsicms.rsuite.editors.oxygen.launcher.common.OxygenApplicationException;

public class OxygenApplicationParameters {

	private String sessionKey;

	private String baseUri;

	private String projectName;

	private String userName;

	private String customizationClass;

	private String hostAddress;

	private String moIdToOpen;
	
	private String launcherVersion;

	public OxygenApplicationParameters(String parameterList)
			throws OxygenApplicationException {
		Map<String, String> parsedParameters = OxygenParamererListParser.parseParameters(parameterList);
		sessionKey = parsedParameters.get("sessionKey");
		baseUri = parsedParameters.get("baseUri");
		projectName = parsedParameters.get("projectName");
		userName = parsedParameters.get("userName");
		customizationClass = parsedParameters.get("customizationClass");
		hostAddress = parsedParameters.get("hostAddress");
		moIdToOpen = parsedParameters.get("moIdToOpen");
		launcherVersion = parsedParameters.get("launcherVersion");
	}

	public String getSessionKey() {
		return sessionKey;
	}

	public String getBaseURI() {
		return baseUri;
	}

	public String getProjectName() {
		return projectName;
	}

	public String getUserName() {
		return userName;
	}

	public String getCustomizationClass() {
		return customizationClass;
	}

	public String getHostAddress() {
		return hostAddress;
	}

	public String getMoIdToOpen() {
		return moIdToOpen;
	}

	public String getLauncherVersion() {
		return launcherVersion;
	}	
}
