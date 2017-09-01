package com.rsicms.rsuite.editors.oxygen.launcher.launch;

import com.rsicms.rsuite.editors.oxygen.launcher.cms.rsuite.RSuiteSessionManager;
import com.rsicms.rsuite.editors.oxygen.launcher.common.OxygenApplication;
import com.rsicms.rsuite.editors.oxygen.launcher.common.OxygenApplicationException;
import com.rsicms.rsuite.editors.oxygen.launcher.jnlp.OxygenApplicationParameters;
import com.rsicms.rsuite.editors.oxygen.launcher.update.OxygenApplicationRSuiteUpdater;
import com.rsicms.rsuite.editors.oxygen.launcher.update.OxygenApplicationUpdater;

public class OxygenApplicationLauncherFactory {

	private OxygenApplicationLauncherFactory(){}
	
	public static OxygenApplicationLauncher createApplicationLauncher(
			RSuiteSessionManager sessionManager,
			OxygenApplicationParameters oxygenLaunchParmater)
			throws OxygenApplicationException {
		OxygenApplication oxygenApplication = new OxygenApplication(
				oxygenLaunchParmater.getBaseURI());

		OxygenApplicationRSuiteUpdater cmsUpdater = new OxygenApplicationRSuiteUpdater(
				oxygenLaunchParmater.getBaseURI(), sessionManager);

		
		OxygenApplicationUpdater applicationUpdater = new OxygenApplicationUpdater(
				oxygenApplication.getApplicationHomeFolder(), cmsUpdater);
		
		OxygenApplicationLauncher applicationLauncher = new OxygenApplicationLauncher(oxygenApplication, applicationUpdater, sessionManager);
		return applicationLauncher;
	}
}
