package com.rsicms.rsuite.editors.oxygen.integration.api.libraries;

import java.util.ArrayList;
import java.util.List;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.extensions.ExecutionContext;

public class OxygenCustomJarManager {

	private String advisorPluginId;
	
	private List<String> customJars = new ArrayList<>();
	
	private ExecutionContext context;
	
	public OxygenCustomJarManager(ExecutionContext context, String advisorPluginId,
			List<String> customJars) {
		this.advisorPluginId = advisorPluginId;
		this.customJars = customJars;
		this.context = context;
	}
	

	public List<OxygenIngegrationLibrary> getJarsFromCustomRSuitePlugin() throws RSuiteException {

		List<OxygenIngegrationLibrary> integrationLibraries = new ArrayList<>();
		
		if (customJars == null || advisorPluginId == null) {
			return integrationLibraries;
		}
		
		for (String advisorJar : customJars) {
			OxygenIngegrationLibrary ingegrationLibrary = OxygenIntegrationLibraryFinder.getOxygenIngegrationLibrary(context, advisorPluginId, advisorJar);
			integrationLibraries.add(ingegrationLibrary);
		}
		
		return integrationLibraries;
	}
	
}
