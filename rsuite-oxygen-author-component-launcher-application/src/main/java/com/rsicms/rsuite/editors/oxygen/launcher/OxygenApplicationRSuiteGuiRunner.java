package com.rsicms.rsuite.editors.oxygen.launcher;

import java.io.IOException;
import java.io.InputStream;

import com.rsicms.rsuite.editors.oxygen.launcher.cms.rsuite.RSuiteLocalSession;
import com.rsicms.rsuite.editors.oxygen.launcher.cms.rsuite.RSuiteOxygenApplicationParametersFactory;
import com.rsicms.rsuite.editors.oxygen.launcher.cms.rsuite.RSuiteOxygenStartupArguments;
import com.rsicms.rsuite.editors.oxygen.launcher.cms.rsuite.RSuiteSessionManager;
import com.rsicms.rsuite.editors.oxygen.launcher.common.OxygenApplication;
import com.rsicms.rsuite.editors.oxygen.launcher.common.OxygenApplicationException;
import com.rsicms.rsuite.editors.oxygen.launcher.common.utils.io.IOUtils;
import com.rsicms.rsuite.editors.oxygen.launcher.jnlp.OxygenApplicationParameters;
import com.rsicms.rsuite.editors.oxygen.launcher.jnlp.OxygenLauncherParameters;
import com.rsicms.rsuite.editors.oxygen.launcher.launch.OxygenApplicationLauncher;
import com.rsicms.rsuite.editors.oxygen.launcher.launch.OxygenApplicationLauncherFactory;
import com.rsicms.rsuite.editors.oxygen.launcher.launch.OxygenApplicationLauncherGuiApp;
import com.rsicms.rsuite.editors.oxygen.launcher.launch.OxygenApplicationStartupArguments;
import com.rsicms.rsuite.editors.oxygen.launcher.session.CmsSessionChecker;

public class OxygenApplicationRSuiteGuiRunner {

	public static void main(String[] args) throws Exception {

		OxygenLauncherParameters launcherParameters = getLauncherParameters(args);

		OxygenApplication oxygenApplication = new OxygenApplication(launcherParameters.getBaseURI());
		RSuiteLocalSession localSession = new RSuiteLocalSession(oxygenApplication.getApplicationHomeFolder().getParentFile());
		RSuiteSessionManager sessionManager = new RSuiteSessionManager(localSession, 
				launcherParameters);

		OxygenApplicationLauncherGuiApp launcherApplicationGui = new OxygenApplicationLauncherGuiApp();
		CmsSessionChecker.checkCmsSession(launcherApplicationGui,
				sessionManager);

		try {
			RSuiteOxygenApplicationParametersFactory parameterFactory = new RSuiteOxygenApplicationParametersFactory(
					launcherParameters.getHostAddress(), sessionManager);
			OxygenApplicationParameters oxygenLaunchParmater = parameterFactory
					.createParamaters();

			checkIfLauncherIsUpToDate(launcherApplicationGui,
					launcherParameters.getLauncherVersion(),
					oxygenLaunchParmater.getLauncherVersion());

			OxygenApplicationLauncher applicationLauncher = OxygenApplicationLauncherFactory
					.createApplicationLauncher(sessionManager,
							oxygenLaunchParmater);

			OxygenApplicationStartupArguments startUpArguments = new RSuiteOxygenStartupArguments(
					oxygenLaunchParmater, sessionManager);

			launcherApplicationGui.launchOxygenApplication(applicationLauncher,
					startUpArguments);

		} catch (Exception e) {
			launcherApplicationGui.showWarningMessage(e.getLocalizedMessage());
			e.printStackTrace();
		}

	}

	private static OxygenLauncherParameters getLauncherParameters(String[] args)
			throws IOException, OxygenApplicationException {
		String launcherParameterList = getLauncherParametersList(args);

		OxygenLauncherParameters launcherParameters = new OxygenLauncherParameters(
				launcherParameterList);
		return launcherParameters;
	}

	private static void checkIfLauncherIsUpToDate(
			OxygenApplicationLauncherGuiApp launcherApplication,
			String launcherVersion, String serverLauncherVersion) {
		if (launcherVersion != null && serverLauncherVersion != null
				&& launcherVersion.trim().equals(serverLauncherVersion.trim())) {
			return;
		}

		launcherApplication
				.showWarningMessage("The launcher is not up to date. Please download a new version from RSuite");

	}

	private static String getLauncherParametersList(String[] args)
			throws IOException {

		InputStream launchArgumentStream = Thread.currentThread().getClass()
				.getResourceAsStream("/launcherParameters.txt");

		if (launchArgumentStream != null) {
			return IOUtils.toString(launchArgumentStream, "utf-8");
		}

		if (args.length != 1) {
			throw new IllegalArgumentException(
					"Invalid number of arguments. Expected 1");
		}

		String parameterList = args[0];
		return parameterList;
	}
}
