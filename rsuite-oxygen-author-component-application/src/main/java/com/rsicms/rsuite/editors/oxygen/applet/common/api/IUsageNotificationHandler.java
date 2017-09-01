package com.rsicms.rsuite.editors.oxygen.applet.common.api;

public interface IUsageNotificationHandler {

	void login(ICmsURI cmsURI, String user, String project);
	
	void logout(ICmsURI cmsURI, String user, String project);
}
