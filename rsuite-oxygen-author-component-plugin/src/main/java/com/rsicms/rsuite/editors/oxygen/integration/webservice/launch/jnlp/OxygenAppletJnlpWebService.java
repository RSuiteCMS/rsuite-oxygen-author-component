package com.rsicms.rsuite.editors.oxygen.integration.webservice.launch.jnlp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.remoteapi.CallArgumentList;
import com.reallysi.rsuite.api.remoteapi.DefaultRemoteApiHandler;
import com.reallysi.rsuite.api.remoteapi.RemoteApiDefinition;
import com.reallysi.rsuite.api.remoteapi.RemoteApiExecutionContext;
import com.reallysi.rsuite.api.remoteapi.RemoteApiResult;
import com.rsicms.rsuite.editors.oxygen.integration.domain.launch.jnlp.AppletLauncherJnlpGenerator;
import com.rsicms.rsuite.editors.oxygen.integration.domain.launch.standalone.BaseOxygenLaunchParameters;
import com.rsicms.rsuite.editors.oxygen.integration.domain.launch.standalone.BaseStandaloneOxygenParametersFactory;
import com.rsicms.rsuite.editors.oxygen.integration.webservice.result.JnlpRemoteApiResult;

/**
 * Custom RSuite web service to get information what oxygen jars need to be
 * updated.
 * 
 */
public class OxygenAppletJnlpWebService extends DefaultRemoteApiHandler {

	public static Log log = LogFactory.getLog(OxygenAppletJnlpWebService.class);

	@Override
	public RemoteApiResult execute(RemoteApiExecutionContext context,
			CallArgumentList args) throws RSuiteException {
		
		BaseOxygenLaunchParameters jnlpParameters = BaseStandaloneOxygenParametersFactory.createMoJnlpParameters(context, args);
		String jnpl = AppletLauncherJnlpGenerator.generateJnlp(context.getXmlApiManager(), jnlpParameters);
		
		RemoteApiResult result = new JnlpRemoteApiResult(jnpl);
		return result;

	}

	@Override
	public void initialize(RemoteApiDefinition arg0) {
	}

}
