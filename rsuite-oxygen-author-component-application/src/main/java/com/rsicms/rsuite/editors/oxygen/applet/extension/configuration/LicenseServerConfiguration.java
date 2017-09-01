package com.rsicms.rsuite.editors.oxygen.applet.extension.configuration;


public class LicenseServerConfiguration {

	private String url = "/oXygenLicenseServlet/license-servlet";
	
	private String user = "rsuite";
	
	private String password = "rsuite";

	public LicenseServerConfiguration() {
	}
	
	
	public LicenseServerConfiguration(String url, String user,
			String password) {
		this.url = getValue(url, this.url);
		this.user = getValue(user, this.user);
		this.password = getValue(password, this.password);
	}

	private String getValue(String value, String defaultValue){
		if (value != null){
			return value;
		}
		
		return defaultValue;
	}
	
	public String getUrl() {
		return url;
	}

	public String getUser() {
		return user;
	}

	public String getPassword() {
		return password;
	}
	
	
}
