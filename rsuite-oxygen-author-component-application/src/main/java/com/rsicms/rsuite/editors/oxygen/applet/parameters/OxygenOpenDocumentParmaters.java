package com.rsicms.rsuite.editors.oxygen.applet.parameters;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class OxygenOpenDocumentParmaters {

	private Map<OxygenOpenDocumentParmatersNames, String> startupParamaters = new HashMap<OxygenOpenDocumentParmatersNames, String>();

	public void addParameter(OxygenOpenDocumentParmatersNames parameter, String value){
		startupParamaters.put(parameter, value);
	}
	
	public String getParameterValue(OxygenOpenDocumentParmatersNames parameter){
		return startupParamaters.get(parameter);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		for (Entry<OxygenOpenDocumentParmatersNames, String> entry : startupParamaters.entrySet()){
			sb.append(entry.toString());
			sb.append("\n");
		}
		
		return sb.toString();
	}

}
