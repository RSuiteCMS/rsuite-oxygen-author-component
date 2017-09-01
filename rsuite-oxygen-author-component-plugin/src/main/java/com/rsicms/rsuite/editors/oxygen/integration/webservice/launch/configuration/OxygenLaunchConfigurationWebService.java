package com.rsicms.rsuite.editors.oxygen.integration.webservice.launch.configuration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.remoteapi.CallArgumentList;
import com.reallysi.rsuite.api.remoteapi.DefaultRemoteApiHandler;
import com.reallysi.rsuite.api.remoteapi.RemoteApiDefinition;
import com.reallysi.rsuite.api.remoteapi.RemoteApiExecutionContext;
import com.reallysi.rsuite.api.remoteapi.RemoteApiResult;
import com.reallysi.rsuite.api.remoteapi.result.XmlRemoteApiResult;
import com.rsicms.rsuite.editors.oxygen.integration.api.advisor.OxygenLaunchMethod;
import com.rsicms.rsuite.editors.oxygen.integration.domain.launch.configuration.OxygenLaunchConfigurator;

/**
 * Custom RSuite web service to get information what oxygen jars need to be
 * updated.
 * 
 */
public class OxygenLaunchConfigurationWebService extends DefaultRemoteApiHandler {

	public static Log log = LogFactory.getLog(OxygenLaunchConfigurationWebService.class);

	@Override
	public RemoteApiResult execute(RemoteApiExecutionContext context,
			CallArgumentList args) throws RSuiteException {

		OxygenLaunchConfigurator launcherConfigurator = new OxygenLaunchConfigurator();
		OxygenLaunchMethod launchMethod = launcherConfigurator.getOxygenLaunchMethod(context, context.getSession().getUser());
		
		RemoteApiResult result = new XmlRemoteApiResult("<configuration launchType='" + launchMethod.toString().toLowerCase()  +"' />");
		return result;

	}

	@Override
	public void initialize(RemoteApiDefinition arg0) {
	}

}
