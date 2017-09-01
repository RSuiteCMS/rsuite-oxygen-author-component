package com.rsicms.rsuite.editors.oxygen.applet.parameters;

public enum OxygenAppletStartupParmatersNames {

	USER_NAME("userName"), PROJECT_NAME("projectName"), CUSTOMIZATION_CLASS ("customizationClass", false), BASE_URI("baseUri"),
	SESSION_KEY("sessionKey"), MO_ID_TO_OPEN("moIdToOpen", false);
	
	private String name;
	
	private boolean required = true;
	
	private OxygenAppletStartupParmatersNames(String name) {
		this.name = name;
	}
	
	private OxygenAppletStartupParmatersNames(String name, boolean optional) {
		this.name = name;
		this.required = optional;
	}

	public String getName() {
		return name;
	}

	public boolean isRequired() {
		return required;
	}
	
}
