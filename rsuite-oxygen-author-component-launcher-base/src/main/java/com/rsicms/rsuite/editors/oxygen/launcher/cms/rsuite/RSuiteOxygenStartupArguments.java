package com.rsicms.rsuite.editors.oxygen.launcher.cms.rsuite;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.rsicms.rsuite.editors.oxygen.launcher.common.utils.Base64Coder;
import com.rsicms.rsuite.editors.oxygen.launcher.jnlp.OxygenApplicationParameters;
import com.rsicms.rsuite.editors.oxygen.launcher.launch.OxygenApplicationStartupArguments;

public class RSuiteOxygenStartupArguments implements
		OxygenApplicationStartupArguments {

	private RSuiteSessionManager sessionManager;

	private OxygenApplicationParameters launcherParameters;

	private String moId;

	public RSuiteOxygenStartupArguments(
			OxygenApplicationParameters launcherParameters,
			RSuiteSessionManager sessionManager) {
		this.sessionManager = sessionManager;
		this.launcherParameters = launcherParameters;
		this.moId = launcherParameters.getMoIdToOpen();

	}

	@Override
	public String getStartupCommandLineArguments() {

		List<String> arguments = new ArrayList<>();
		
		addArgument(arguments, "baseUri", launcherParameters.getBaseURI());
		
		addArgument(arguments, "sessionKey", sessionManager.getSessionKey());
		addArgument(arguments, "projectName", launcherParameters.getProjectName());
		addArgument(arguments, "userName", launcherParameters.getUserName());
		
		addArgument(arguments, "customizationClass", launcherParameters.getCustomizationClass());
		addArgument(arguments, "moIdToOpen", moId);

		return Base64Coder.encodeString(joinList(arguments, "&"));
	}
	
	private void addArgument(List<String> arguments, String argumentName, String value){
		if (value != null && !value.trim().isEmpty()){
			StringBuilder argumentEntry = new StringBuilder(argumentName);
			argumentEntry.append("=").append(value);
			arguments.add(argumentEntry.toString());
		}		
	}
	
	private String joinList(List<String> list, String separator){
		StringBuilder joinedList = new StringBuilder();
		Iterator<String> it = list.iterator();
		while (true) {
			joinedList.append(it.next());
			if (it.hasNext()){
				joinedList.append(separator);
			}else{
				break;
			}
		}
		
		return joinedList.toString();
	}

	@Override
	public String getDocumentIdToOpen() {
		return moId;
	}

}
