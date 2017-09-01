package com.rsicms.rsuite.editors.oxygen.integration.api.common;

public enum OxygenProperites {

	PROPERTY_OXYGEN_CONFIGURATION_FILE("oxygen.configuration.file"),
	PROPERTY_OXYGEN_CONFIGURATION_PRIORITY("oxygen.configuration.priority"),
	PROPERTY_OXYGEN_ADVISOR_CLASS("oxygen.advisor.class"),
	PROPERTY_OXYGEN_ADVISOR_PRIORITY("oxygen.advisior.priority"),
	PROPERTY_OXYGEN_LICENSE_KEY_FILE("oxygen.license.key.file"),
	PROPERTY_OXYGEN_FRAMEWORK_PLUGIN_OVERWRITE("oxygen.framework.plugin.overwrite"),
	PROPERTY_OXYGEN_FRAMEWORK_PLUGIN_ID("oxygen.framework.id"),										 
	PROPERTY_OXYGEN_FRAMEWORK("oxygen.framework.jar");
	
	
	
	
	
	private String property;
	
	private OxygenProperites(String property) {
		this.property = property;
	}

	public String getProperty() {
		return property;
	}
	
	
}
