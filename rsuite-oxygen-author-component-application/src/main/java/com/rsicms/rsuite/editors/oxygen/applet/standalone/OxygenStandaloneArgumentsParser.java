package com.rsicms.rsuite.editors.oxygen.applet.standalone;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import com.rsicms.rsuite.editors.oxygen.applet.common.OxygenIntegrationException;
import com.rsicms.rsuite.editors.oxygen.applet.common.utils.Base64Coder;
import com.rsicms.rsuite.editors.oxygen.applet.parameters.OxygenAppletStartupParmaters;
import com.rsicms.rsuite.editors.oxygen.applet.parameters.OxygenAppletStartupParmatersNames;
import com.rsicms.rsuite.editors.oxygen.applet.parameters.OxygenOpenDocumentParmaters;
import com.rsicms.rsuite.editors.oxygen.applet.parameters.OxygenOpenDocumentParmatersNames;


public class OxygenStandaloneArgumentsParser {

	public static Map<String, String> parseStartupArgument(String encodedArguments)
			throws OxygenIntegrationException {
		String decodedArguments = Base64Coder.decodeString(encodedArguments);
		String[] argumentsArray = decodedArguments.split("&");

		Map<String, String> argumentsMap = new HashMap<String, String>();

		try {
			for (String argument : argumentsArray) {
				String[] parsedArgument = parseArgument(argument);
				argumentsMap.put(parsedArgument[0], parsedArgument[1]);
			}
		} catch (UnsupportedEncodingException e) {
			throw new OxygenIntegrationException(
					"Unable to parse startup arguments", e);
		}
		
		return argumentsMap;

	}

	private static String[] parseArgument(String argument)
			throws UnsupportedEncodingException {
		String[] argumentArray = argument.split("=");
		argumentArray[1] = URLDecoder.decode(argumentArray[1], "utf-8");
		return argumentArray;
	}
	
	public static OxygenAppletStartupParmaters parseStartupParameters(Map<String, String> argumentsMap) throws OxygenIntegrationException{
	
		OxygenAppletStartupParmaters startupParameters = new OxygenAppletStartupParmaters();
		
		for (OxygenAppletStartupParmatersNames parameter : OxygenAppletStartupParmatersNames.values()){
			 String value = argumentsMap.get(parameter.getName());
			 
			 if (value == null && parameter.isRequired()){
				 throw new OxygenIntegrationException("Missing required startup paramter: " + parameter.getName());
			 }
			 
			 startupParameters.addParameter(parameter, value);
		}
		
		return startupParameters;
	}
	
	public static OxygenAppletStartupParmaters parseStartupParameters(String[] arguments){
		String sessionKey = arguments[0];
		String baseUri = arguments[1];
		String projectName = arguments[2];
		String userName = arguments[3];

		
		OxygenAppletStartupParmaters startupParameters = new OxygenAppletStartupParmaters();
		startupParameters.addParameter(
				OxygenAppletStartupParmatersNames.BASE_URI, baseUri);
		startupParameters.addParameter(
				OxygenAppletStartupParmatersNames.SESSION_KEY, sessionKey);
		startupParameters.addParameter(
				OxygenAppletStartupParmatersNames.PROJECT_NAME, projectName);
		startupParameters.addParameter(
				OxygenAppletStartupParmatersNames.USER_NAME, userName);
		
		return startupParameters;
	}
	
	public static OxygenOpenDocumentParmaters parseDocumentParameters(Map<String, String> argumentsMap) throws OxygenIntegrationException{
		
		OxygenOpenDocumentParmaters documentParameters = new OxygenOpenDocumentParmaters();
		
		for (OxygenOpenDocumentParmatersNames parameter : OxygenOpenDocumentParmatersNames.values()){
			 String value = argumentsMap.get(parameter.getName());
			 
			 
			 documentParameters.addParameter(parameter, value);
		}
		
		return documentParameters;
	}
}
