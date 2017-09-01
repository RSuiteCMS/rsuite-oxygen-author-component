package com.rsicms.rsuite.editors.oxygen.applet.parameters;

import java.util.HashMap;
import java.util.Map;

public class OxygenAppletStartupParmaters {

	private Map<OxygenAppletStartupParmatersNames, String> startupParamaters = new HashMap<OxygenAppletStartupParmatersNames, String>();

	public void addParameter(OxygenAppletStartupParmatersNames parameter, String value){
		startupParamaters.put(parameter, value);
	}
	
	public String getParameterValue(OxygenAppletStartupParmatersNames parameter){
		return startupParamaters.get(parameter);
	}

}
