package com.rsicms.rsuite.editors.oxygen.launcher.session;

import com.rsicms.rsuite.editors.oxygen.launcher.common.OxygenApplicationException;

public interface CmsSessionManager {

	boolean isValidSession() throws OxygenApplicationException;
	
	boolean createNewSession(String projectName, String userName, String password) throws OxygenApplicationException;
}
