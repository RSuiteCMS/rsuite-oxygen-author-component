package com.rsicms.rsuite.editors.oxygen.integration.domain.launch.standalone;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.lang.StringUtils;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.Session;
import com.rsicms.rsuite.editors.oxygen.integration.advisor.OxygenWebEditingContext;
import com.rsicms.rsuite.editors.oxygen.integration.api.advisor.IOxygenIntegrationAdvisor;
import com.rsicms.rsuite.editors.oxygen.integration.domain.launch.configuration.OxygenLauncherInfo;

public class BaseOxygenLaunchParameters {

	private String sessionKey;

	private String baseURI;

	private String projectName;

	private String userName;

	private String customizationClass;

	private String hostAddress;
	
	private String moIdToOpen;
	
	public BaseOxygenLaunchParameters(String hostAddress, String sessionKey){
		this.sessionKey = sessionKey;
		this.hostAddress = hostAddress;
	}
	
	public BaseOxygenLaunchParameters(OxygenWebEditingContext oxygenWebEditingContext,
			IOxygenIntegrationAdvisor advisor, String moId) {
		this(oxygenWebEditingContext, advisor);
		this.moIdToOpen = moId;
	}
	
	public BaseOxygenLaunchParameters(OxygenWebEditingContext oxygenWebEditingContext,
			IOxygenIntegrationAdvisor advisor) {
		Session session = oxygenWebEditingContext.getSession();
		sessionKey = session.getKey();
		userName = session.getUser().getUserId();

		customizationClass = advisor.getOxygenCustomizationClass();
		projectName = advisor.getProjectName();

		baseURI = oxygenWebEditingContext.getHostAddress()
				+ "/rsuite/rest/v2/static/rsuite/oxygen/applet/integration";
		
		hostAddress = oxygenWebEditingContext.getHostAddress();	
	}

	public String createJnlpParameterList()
			throws RSuiteException {
		
		StringBuilder parameters = new StringBuilder();

		addParamIfNotEmpty(parameters, "sessionKey", sessionKey);
		addParamIfNotEmpty(parameters, "baseUri", baseURI);
		addParamIfNotEmpty(parameters, "projectName", projectName);
		addParamIfNotEmpty(parameters, "userName", userName);
		
		addParamIfNotEmpty(parameters, "hostAddress", hostAddress);
		addParamIfNotEmpty(parameters, "customizationClass", customizationClass);
		addParamIfNotEmpty(parameters, "moIdToOpen", moIdToOpen);
		
		OxygenLauncherInfo launcherInfo = new OxygenLauncherInfo();
		addParamIfNotEmpty(parameters, "launcherVersion", launcherInfo.getLauncherVersion());
		
		if(parameters.length() > 0){
			parameters = parameters.deleteCharAt(0);
		}

		return parameters.toString();
	}
	
	private void addParamIfNotEmpty(StringBuilder parameters, String parameterName, String value) throws RSuiteException{
		if (StringUtils.isNotBlank(value)){
			parameters.append("&").append(parameterName).append("=").append(encodeValue(value));
		}
	}
	
	public String getHostAddress() {
		return hostAddress;
	}

	private String encodeValue(String value) throws RSuiteException {
		try {
			return URLEncoder.encode(value, "utf-8");
		} catch (UnsupportedEncodingException e) {
			throw new RSuiteException(0, e.getLocalizedMessage(), e);
		}
	}

}
