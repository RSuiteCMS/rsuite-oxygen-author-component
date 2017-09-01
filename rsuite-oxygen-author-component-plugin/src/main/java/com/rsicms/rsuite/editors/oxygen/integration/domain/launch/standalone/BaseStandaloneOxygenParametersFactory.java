package com.rsicms.rsuite.editors.oxygen.integration.domain.launch.standalone;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.remoteapi.CallArgumentList;
import com.reallysi.rsuite.api.remoteapi.RemoteApiExecutionContext;
import com.rsicms.rsuite.editors.oxygen.integration.advisor.OxygenWebEditingContext;
import com.rsicms.rsuite.editors.oxygen.integration.api.advisor.IOxygenIntegrationAdvisor;
import com.rsicms.rsuite.editors.oxygen.integration.domain.MoToLaunch;
import com.rsicms.rsuite.editors.oxygen.integration.utils.OxygenLaunchHelper;
import com.rsicms.rsuite.editors.oxygen.integration.webservice.lookup.ALookupWebService;

public class BaseStandaloneOxygenParametersFactory {

	private BaseStandaloneOxygenParametersFactory(){
	}
	
	public static BaseOxygenLaunchParameters createMoJnlpParameters(
			RemoteApiExecutionContext context, CallArgumentList args)
			throws RSuiteException {

		OxygenLaunchHelper helper = new OxygenLaunchHelper(context, args);
		MoToLaunch moToLaunch = helper.getMoToLaunch();
		OxygenWebEditingContext oxygenWebEditingContext = helper
				.getOxygenWebEditingContext();
		IOxygenIntegrationAdvisor advisor = helper.initializeAndGetAdvisor(moToLaunch.getManagedObject());
		ALookupWebService.registerLookupHandler(advisor.getLookupHandler());

		BaseOxygenLaunchParameters jnlpParameters = new BaseOxygenLaunchParameters(
				oxygenWebEditingContext, advisor, moToLaunch.getId());
		return jnlpParameters;
	}
	
	public static BaseOxygenLaunchParameters createBaseJnlpParameters(
			RemoteApiExecutionContext context, CallArgumentList args)
			throws RSuiteException {

		OxygenLaunchHelper helper = new OxygenLaunchHelper(context, args);
		
		OxygenWebEditingContext oxygenWebEditingContext = helper
				.getOxygenWebEditingContext();
		IOxygenIntegrationAdvisor advisor = helper.initializeAndGetAdvisor(null);
		
		ALookupWebService.registerLookupHandler(advisor.getLookupHandler());

		BaseOxygenLaunchParameters jnlpParameters = new BaseOxygenLaunchParameters(
				oxygenWebEditingContext, advisor);
		return jnlpParameters;
	}
}
