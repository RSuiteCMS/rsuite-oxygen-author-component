package com.rsicms.rsuite.editors.oxygen.integration.webservice.launch.jar;

import java.io.ByteArrayOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.remoteapi.CallArgumentList;
import com.reallysi.rsuite.api.remoteapi.DefaultRemoteApiHandler;
import com.reallysi.rsuite.api.remoteapi.RemoteApiDefinition;
import com.reallysi.rsuite.api.remoteapi.RemoteApiExecutionContext;
import com.reallysi.rsuite.api.remoteapi.RemoteApiResult;
import com.rsicms.rsuite.editors.oxygen.integration.domain.launch.jar.OxygenLauncherJarCreator;
import com.rsicms.rsuite.editors.oxygen.integration.domain.launch.standalone.OxygenLauncherParameters;
import com.rsicms.rsuite.editors.oxygen.integration.utils.WebServiceUtils;
import com.rsicms.rsuite.editors.oxygen.integration.webservice.result.RemoteOctetStreamResult;

/**
 * Custom RSuite web service to get information what oxygen jars need to be
 * updated.
 * 
 */
public class OxygenLauncherJarWebService extends DefaultRemoteApiHandler {

	public static Log log = LogFactory
			.getLog(OxygenLauncherJarWebService.class);

	@Override
	public RemoteApiResult execute(RemoteApiExecutionContext context,
			CallArgumentList args) throws RSuiteException {

		
		OxygenLauncherParameters launcherParameters = new OxygenLauncherParameters(context, args);

		OxygenLauncherJarCreator launcherCreator = new OxygenLauncherJarCreator();
		ByteArrayOutputStream launcherJar = launcherCreator.createLauncherJar(
				launcherParameters);
		
		String normalizedHostName =  WebServiceUtils.createNormalizedHostName(launcherParameters.getHostAddress());

		return new RemoteOctetStreamResult("RSuite_oXygen_" + normalizedHostName + ".jar",
				launcherJar.toByteArray());
	}

	@Override
	public void initialize(RemoteApiDefinition arg0) {
	}

}
