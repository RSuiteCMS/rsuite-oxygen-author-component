package com.rsicms.rsuite.editors.oxygen.applet.extension.configuration;


public class OxygenConfigurationProperty {

	private String name;
	
	private String value;
	
	private String type;

	public OxygenConfigurationProperty(String name, String value, String type) {
		this.name = name;
		this.value = value;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}

	public Object getTypedValue(){
		
		
		
		if ("boolean".equalsIgnoreCase(type)){
			return new Boolean(Boolean.parseBoolean(value));
		}else if ("integer".equalsIgnoreCase(type)){
			return new Integer(value);
		}
		
		return value;
	}
	

}
