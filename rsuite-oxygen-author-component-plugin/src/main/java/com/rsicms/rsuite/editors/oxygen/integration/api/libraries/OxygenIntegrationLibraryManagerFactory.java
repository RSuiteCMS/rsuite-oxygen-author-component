package com.rsicms.rsuite.editors.oxygen.integration.api.libraries;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.rsicms.rsuite.editors.oxygen.integration.api.advisor.IOxygenIntegrationAdvisor;
import com.rsicms.rsuite.editors.oxygen.integration.domain.launch.FindOxygenAdvisorResult;
import com.rsicms.rsuite.editors.oxygen.integration.domain.launch.OxygenIntegrationAdvisorFactory;

public class OxygenIntegrationLibraryManagerFactory {

	public OxygenIntegrationLibraryManager createOxygenIntegrationLibraryManager(
			ExecutionContext context) throws RSuiteException {
		OxygenIntegrationAdvisorFactory advisorFactory = new OxygenIntegrationAdvisorFactory();
		FindOxygenAdvisorResult advisorResult = advisorFactory
				.findAndCreateOxygenIntegrationAdvisor(context);

		IOxygenIntegrationAdvisor advisor = advisorResult
				.getOxygenIntegrationAdvisor();

		OxygenCustomJarManager customJarManager = new OxygenCustomJarManager(context, advisorResult.getCustomPluginId(), advisor.getCustomJars());
		
		OxygenIntegrationLibraryManager libraryManager = new OxygenIntegrationLibraryManager(
				context, advisor.getOptionalFeatures(), customJarManager);

		return libraryManager;
	}
}
