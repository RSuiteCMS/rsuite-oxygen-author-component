package com.rsicms.rsuite.editors.oxygen.launcher.session;

import javax.swing.JFrame;

import com.rsicms.rsuite.editors.oxygen.launcher.common.OxygenApplicationException;

public class CmsSessionChecker {

	public static void checkCmsSession(JFrame launcherApplication,
			CmsSessionManager sessionManager) throws OxygenApplicationException {
		if (!sessionManager.isValidSession()) {
			LoginDialogManager.showLoginDialog(launcherApplication,
					sessionManager);
		}
	}

}
