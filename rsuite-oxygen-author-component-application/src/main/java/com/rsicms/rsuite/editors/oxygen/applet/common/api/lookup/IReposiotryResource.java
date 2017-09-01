package com.rsicms.rsuite.editors.oxygen.applet.common.api.lookup;

import java.util.Set;

public interface IReposiotryResource {

	String getDisplayText();
	
	boolean hasChilds();
	
	boolean isContainer();
	
	boolean isXml();
	
	String getCMSid();
	
	String getCMSlink();
	
	String getId();
	
	@Override
	public String toString();
	
	String getCustomMetadata(String metadataName);
	
	Set<String> getCustomMetadataNames();
	
	String getIconName();
}
