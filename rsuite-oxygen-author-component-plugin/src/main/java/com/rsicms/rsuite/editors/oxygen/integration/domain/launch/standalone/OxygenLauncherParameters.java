package com.rsicms.rsuite.editors.oxygen.integration.domain.launch.standalone;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.remoteapi.CallArgumentList;
import com.reallysi.rsuite.api.remoteapi.RemoteApiExecutionContext;
import com.rsicms.rsuite.editors.oxygen.integration.domain.launch.configuration.OxygenLauncherInfo;
import com.rsicms.rsuite.editors.oxygen.integration.utils.WebServiceUtils;

public class OxygenLauncherParameters {

	private String hostAddress;

	private String sessionKey;
	
	private String launcherVersion;
	
	private String baseURI;

	public OxygenLauncherParameters(RemoteApiExecutionContext context,
			CallArgumentList args) throws RSuiteException {
		OxygenLauncherInfo launcherInfo = new OxygenLauncherInfo();
		
		launcherVersion = launcherInfo.getLauncherVersion();
		sessionKey = context.getSession().getKey();
		hostAddress = WebServiceUtils.getHostFromWsArguments(args);
		baseURI = hostAddress + "/rsuite/rest/v2/static/rsuite/oxygen/applet/integration";
	}

	public String getLauncherParameters() throws RSuiteException {
		StringBuilder launcherParmaters = new StringBuilder("hostAddress=");
		launcherParmaters.append(encodeParmater(hostAddress));
		launcherParmaters.append("&baseURI=");
		launcherParmaters.append(encodeParmater(baseURI));
		launcherParmaters.append("&sessionKey=");
		launcherParmaters.append(encodeParmater(sessionKey));
		launcherParmaters.append("&launcherVersion=");
		launcherParmaters.append(encodeParmater(launcherVersion));

		return launcherParmaters.toString();
	}

	private String encodeParmater(String parameter) throws RSuiteException {
		try {
			return URLEncoder.encode(parameter, "utf-8");
		} catch (UnsupportedEncodingException e) {
			throw new RSuiteException(0, e.getLocalizedMessage(), e);
		}
	}

	public String getHostAddress() {
		return hostAddress;
	}		
}
