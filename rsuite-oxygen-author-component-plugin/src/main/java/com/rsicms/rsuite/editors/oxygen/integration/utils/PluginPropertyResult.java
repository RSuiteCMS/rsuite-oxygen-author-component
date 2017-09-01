package com.rsicms.rsuite.editors.oxygen.integration.utils;

import java.util.Properties;


public class PluginPropertyResult implements Comparable<PluginPropertyResult>{

	private String pluginId;
	
	private String propertyName;
	
	private String propertyValue;
	
	private int priority;
	
	private Properties pluginProperties;

	public PluginPropertyResult(String pluginId, Properties pluginProperties, String propertyName,
			String propertyValue, String priority) {
		this.pluginId = pluginId;
		this.propertyName = propertyName;
		this.propertyValue = propertyValue;
		this.pluginProperties = pluginProperties;
		
		this.priority = Integer.parseInt(priority);
	}

	public String getPluginId() {
		return pluginId;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public String getPropertyValue() {
		return propertyValue;
	}

	public int getPriority() {
		return priority;
	}

	@Override
	public int compareTo(PluginPropertyResult o) {
		
		if (o != null){
			return priority - o.getPriority();
		}
		
		return 1;
	}
	
	public String getPluginProperty(String propertyName){
		if (pluginProperties != null){
			return pluginProperties.getProperty(propertyName);
		}
		
		return null;
	}

	
}
