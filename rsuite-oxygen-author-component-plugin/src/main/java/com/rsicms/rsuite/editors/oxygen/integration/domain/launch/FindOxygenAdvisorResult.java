package com.rsicms.rsuite.editors.oxygen.integration.domain.launch;

import com.rsicms.rsuite.editors.oxygen.integration.api.advisor.IOxygenIntegrationAdvisor;

public class FindOxygenAdvisorResult {

	private String customPluginId;
	
	private IOxygenIntegrationAdvisor oxygenIntegrationAdvisor;

	public FindOxygenAdvisorResult(String customPluginId,
			IOxygenIntegrationAdvisor oxygenIntegrationAdvisor) {
		super();
		this.customPluginId = customPluginId;
		this.oxygenIntegrationAdvisor = oxygenIntegrationAdvisor;
	}

	public String getCustomPluginId() {
		return customPluginId;
	}

	public IOxygenIntegrationAdvisor getOxygenIntegrationAdvisor() {
		return oxygenIntegrationAdvisor;
	}
	
	
}
