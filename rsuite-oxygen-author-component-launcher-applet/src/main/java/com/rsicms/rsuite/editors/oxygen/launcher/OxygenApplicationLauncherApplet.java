package com.rsicms.rsuite.editors.oxygen.launcher;

import java.applet.Applet;


import com.rsicms.rsuite.editors.oxygen.launcher.cms.rsuite.RSuiteOxygenStartupArguments;
import com.rsicms.rsuite.editors.oxygen.launcher.cms.rsuite.RSuiteSessionManager;
import com.rsicms.rsuite.editors.oxygen.launcher.common.OxygenApplication;
import com.rsicms.rsuite.editors.oxygen.launcher.common.OxygenApplicationException;
import com.rsicms.rsuite.editors.oxygen.launcher.jnlp.OxygenApplicationParameters;
import com.rsicms.rsuite.editors.oxygen.launcher.launch.OxygenApplicationLauncher;
import com.rsicms.rsuite.editors.oxygen.launcher.launch.OxygenApplicationLauncherPanel;
import com.rsicms.rsuite.editors.oxygen.launcher.launch.OxygenApplicationStartupArguments;
import com.rsicms.rsuite.editors.oxygen.launcher.update.OxygenApplicationRSuiteUpdater;
import com.rsicms.rsuite.editors.oxygen.launcher.update.OxygenApplicationUpdater;

public class OxygenApplicationLauncherApplet extends Applet {

	private static final long serialVersionUID = 981924069909302411L;

	private JavaScriptConnector jsConnector;
	
	@Override
	public void init() {

		this.setSize(440, 150);

		jsConnector = createJavaScriptConnector();
		OxygenApplicationLauncherPanel launcherPanel = new OxygenApplicationLauncherPanel();
		this.add(launcherPanel);
		runLauncher(launcherPanel);
	}
	
	protected JavaScriptConnector createJavaScriptConnector() {
		return new JavaScriptConnector(getAppletContext());
	}

	private void runLauncher(final OxygenApplicationLauncherPanel launcherPanel) {
		new Thread() {
			@Override
			public void run() {
				try {													     
					String parameterList = getParameter("parameterLists");
					
					OxygenApplicationParameters launcherParameters = new OxygenApplicationParameters(
							parameterList);
					
					OxygenApplication oxygenApplication = new OxygenApplication(
							launcherParameters.getBaseURI());

					RSuiteSessionManager sessionManager = new RSuiteSessionManager(
							launcherParameters);

					OxygenApplicationRSuiteUpdater cmsUpdater = new OxygenApplicationRSuiteUpdater(
							launcherParameters.getBaseURI(), sessionManager);
					OxygenApplicationStartupArguments startUpArguments = new RSuiteOxygenStartupArguments(launcherParameters, 
							sessionManager);

					OxygenApplicationUpdater applicationUpdater = new OxygenApplicationUpdater(
							oxygenApplication.getApplicationHomeFolder(),
							cmsUpdater);

					OxygenApplicationLauncher launcher = new OxygenApplicationLauncher(
							oxygenApplication,
							applicationUpdater, sessionManager);
					launcher.launchOxygenApplication(launcherPanel, startUpArguments);					
					jsConnector.closeLauncherDialog();
				} catch (OxygenApplicationException e) {
					e.printStackTrace();
				}
			}
		}.run();
	}

}
