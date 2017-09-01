package com.rsicms.rsuite.editors.oxygen.integration.api.advisor;

import java.util.List;

import com.rsicms.rsuite.editors.oxygen.integration.api.common.OxygenOptionalFeatures;

public interface IOxygenIntegrationAdvisor {

	void initialize(IOxygenWebEditingContext webContext);
	
	List<String> getCustomJars();
	
	List<String> getCustomRunArguments();

	String getProjectName();
	
	String getOxygenCustomizationClass();
	
	ILookupHandler getLookupHandler();
	
	IOxygenWebEditingContext getWebContext();
	
	List<OxygenOptionalFeatures> getOptionalFeatures();
}