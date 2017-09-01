package com.rsicms.rsuite.editors.oxygen.integration.api.advisor;

import java.util.ArrayList;
import java.util.List;

import com.rsicms.rsuite.editors.oxygen.integration.api.common.OxygenConstants;
import com.rsicms.rsuite.editors.oxygen.integration.api.common.OxygenOptionalFeatures;

public class OxygenIntegrationAdvisorDefault implements
		IOxygenIntegrationAdvisor, OxygenConstants

{
	private IOxygenWebEditingContext webContext;

	public OxygenIntegrationAdvisorDefault() {
	}

	public List<OxygenOptionalFeatures> getOptionalFeatures() {
		List<OxygenOptionalFeatures> optionalFeatures = new ArrayList<>();
		return optionalFeatures;
	}


	@Override
	public List<String> getCustomJars() {
		return null;
	}

	@Override
	public String getProjectName() {
		return "Rsuite";
	}

	@Override
	public String getOxygenCustomizationClass() {
		return null;
	}

	@Override
	public List<String> getCustomRunArguments() {
		return null;
	}


	@Override
	public ILookupHandler getLookupHandler() {
		return null;
	}

	@Override
	public void initialize(IOxygenWebEditingContext webContext) {
		this.webContext = webContext;

	}

	@Override
	public IOxygenWebEditingContext getWebContext() {
		return webContext;
	}
}
