package com.rsicms.rsuite.editors.oxygen.applet.common.api;

public interface ICmsURI {

	public  String getHostURI();

	public String getSessionKey();

	public String getLookUpUri();

	public String getSessionKeyParam();

	public String getImagePreviewUri(String imageId);
	
	public String getBaseURI();
	
	public String getConfigurationURI();

}