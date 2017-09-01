package com.rsicms.rsuite.editors.oxygen.integration.webservice.launch.jnlp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.remoteapi.CallArgumentList;
import com.reallysi.rsuite.api.remoteapi.DefaultRemoteApiHandler;
import com.reallysi.rsuite.api.remoteapi.RemoteApiDefinition;
import com.reallysi.rsuite.api.remoteapi.RemoteApiExecutionContext;
import com.reallysi.rsuite.api.remoteapi.RemoteApiResult;
import com.rsicms.rsuite.editors.oxygen.integration.domain.launch.jnlp.ApplicationLauncherJnlpGenerator;
import com.rsicms.rsuite.editors.oxygen.integration.domain.launch.standalone.OxygenLauncherParameters;
import com.rsicms.rsuite.editors.oxygen.integration.utils.WebServiceUtils;
import com.rsicms.rsuite.editors.oxygen.integration.webservice.result.RemoteOctetStreamResult;

/**
 * Custom RSuite web service to get information what oxygen jars need to be
 * updated.
 * 
 */
public class OxygenLauncherApplicationJnlpWebService extends DefaultRemoteApiHandler {

	public static Log log = LogFactory.getLog(OxygenLauncherApplicationJnlpWebService.class);

	@Override
	public RemoteApiResult execute(RemoteApiExecutionContext context,
			CallArgumentList args) throws RSuiteException {
		
		OxygenLauncherParameters launcherParameters = new OxygenLauncherParameters(context, args);

		String jnlp = ApplicationLauncherJnlpGenerator.generateJnlp(context.getXmlApiManager(), launcherParameters);
		
		String normalizedHostName =  WebServiceUtils.createNormalizedHostName(launcherParameters.getHostAddress());
		
		RemoteApiResult result = new RemoteOctetStreamResult("RSuite_oXygen_" + normalizedHostName + ".jnlp", jnlp);
		return result;

	}
	
	@Override
	public void initialize(RemoteApiDefinition arg0) {
	}

}
