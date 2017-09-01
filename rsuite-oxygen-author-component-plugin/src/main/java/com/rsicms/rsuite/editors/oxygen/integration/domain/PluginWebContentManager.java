package com.rsicms.rsuite.editors.oxygen.integration.domain;

public class PluginWebContentManager {

	private String RSUITE_OXYGEN_PLUGIN_NAME = "rsuite-oxygen-applet-integration";
	
	private String RSUITE_RESOURCE_PLUGIN_PREFIX = "rsuite:/res/plugin/" + RSUITE_OXYGEN_PLUGIN_NAME + "/";
	
	public String generatePluginPath(String pathToResource){
		return RSUITE_RESOURCE_PLUGIN_PREFIX + pathToResource;
	}
	
	public String getPluginId(){
		return RSUITE_OXYGEN_PLUGIN_NAME;
	}
}
