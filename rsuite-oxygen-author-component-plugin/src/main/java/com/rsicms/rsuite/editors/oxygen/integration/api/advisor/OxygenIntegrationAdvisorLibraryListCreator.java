package com.rsicms.rsuite.editors.oxygen.integration.api.advisor;

import static com.rsicms.rsuite.editors.oxygen.integration.api.common.OxygenConstants.RSUITE_OXYGEN_PLUGIN_NAME;

import java.util.List;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.rsicms.rsuite.editors.oxygen.integration.api.common.OxygenConstants;
import com.rsicms.rsuite.editors.oxygen.integration.api.common.OxygenOptionalFeatures;
import com.rsicms.rsuite.editors.oxygen.integration.api.libraries.OxygenCustomJarManager;
import com.rsicms.rsuite.editors.oxygen.integration.api.libraries.OxygenIngegrationLibrary;
import com.rsicms.rsuite.editors.oxygen.integration.api.libraries.OxygenIntegrationLibraryManager;

public class OxygenIntegrationAdvisorLibraryListCreator {

	private ExecutionContext context;


	public OxygenIntegrationAdvisorLibraryListCreator(ExecutionContext context) {
		super();
		this.context = context;
	}

	public String createOxygenJarsList(IOxygenWebEditingContext webContext,
			List<String> customJars, List<OxygenOptionalFeatures> optionalFeatures) throws RSuiteException {

		OxygenCustomJarManager customJarManager = new OxygenCustomJarManager(context, webContext.getCustomPluginId(), customJars);
		
		OxygenIntegrationLibraryManager libraryManager = new OxygenIntegrationLibraryManager(
				context, optionalFeatures, customJarManager);
		List<OxygenIngegrationLibrary> integrationLibraries = libraryManager
				.getIntegrationLibraries();

		String fullPath = webContext.getHostAddress()
				+ OxygenConstants.RSUITE_REST_STATIC;

		StringBuilder jarList = new StringBuilder();
		for (OxygenIngegrationLibrary library : integrationLibraries) {

			if (isNotOxygenPlugin(library.getPluginId())) {
				jarList.append(fullPath).append(library.getRestPath());
			}else{
				jarList.append(library.getPath());	
			}

			jarList.append(",");

		}

		jarList.deleteCharAt(jarList.length() - 1);

		return jarList.toString();

	}

	private boolean isNotOxygenPlugin(String pluginId) {
		return !RSUITE_OXYGEN_PLUGIN_NAME.equals(pluginId);
	}
}
